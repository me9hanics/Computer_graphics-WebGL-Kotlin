import org.w3c.dom.HTMLCanvasElement
import org.khronos.webgl.WebGLRenderingContext as GL
import org.khronos.webgl.Float32Array
import vision.gears.webglmath.UniformProvider
import vision.gears.webglmath.Vec3
import vision.gears.webglmath.Mat4
import kotlin.js.Date

class Scene (
  val gl : WebGL2RenderingContext) : UniformProvider("scene") {

  val vsTrafo = Shader(gl, GL.VERTEX_SHADER, "trafo-vs.glsl")
  val fsTextured = Shader(gl, GL.FRAGMENT_SHADER, "textured-fs.glsl")  
  val fsWood = Shader(gl, GL.FRAGMENT_SHADER, "wood-fs.glsl")
  val vsQuad = Shader(gl, GL.VERTEX_SHADER, "quad-vs.glsl")
  val fsBackground = Shader(gl, GL.FRAGMENT_SHADER, "background-fs.glsl")    
  val texturedProgram = Program(gl, vsTrafo, fsTextured)
  val woodProgram = Program(gl, vsTrafo, fsWood)  
  val backgroundProgram = Program(gl, vsQuad, fsBackground)  
  val quadGeometry = TexturedQuadGeometry(gl)

  val woodMaterial = Material(woodProgram)
  val backgroundMaterial = Material(backgroundProgram)

  val environmentTextureCube =       TextureCube(gl, 
        "media/posx512.jpg",
        "media/negx512.jpg",
        "media/posy512.jpg",
        "media/negy512.jpg",
        "media/posz512.jpg",
        "media/negz512.jpg"
        )

  init{
    backgroundMaterial["environment"]?.set(environmentTextureCube)
  }
  val backgroundMesh = Mesh(backgroundMaterial, quadGeometry)

  val slowpokeMaterial = Material(texturedProgram)
  val slowpokeEyeMaterial = Material(texturedProgram)  
  init{
    slowpokeMaterial["colorTexture"]?.set(Texture2D(gl, "media/slowpoke/YadonDh.png"))
    slowpokeEyeMaterial["colorTexture"]?.set(Texture2D(gl, "media/slowpoke/YadonEyeDh.png"))    
    woodMaterial["environment"]?.set(environmentTextureCube)
  }

  val gameObjects = ArrayList<GameObject>()
  val jsonLoader = JsonLoader()
  val geometries = jsonLoader.load(gl, "media/slowpoke/Slowpoke.json")
  val slowpokeMultiMesh = MultiMesh(
    arrayOf<Material>(slowpokeMaterial, slowpokeEyeMaterial),
    geometries)
  val woodenSlowpokeMultiMesh = MultiMesh(
    arrayOf<Material>(woodMaterial, woodMaterial),
    geometries)  

  val groundMesh = Mesh(slowpokeMaterial, GroundGeometry(gl))

  val avatar = GameObject(woodenSlowpokeMultiMesh)
  val ground = GameObject(groundMesh)
  init{
    //LABTODO: add object from JSON
    gameObjects.add(avatar)
    avatar.position.set(5.0f, -0.5f, 0.0f)
    avatar.scale.set(0.1f, 0.1f, 0.1f)
    avatar.yaw = 1.5f

    gameObjects.add( GameObject(slowpokeMultiMesh))    

    ground.pitch = 1.57f;
    gameObjects.add( ground )        
    //LABTODO: add background object
  }

  val camera = PerspectiveCamera(*Program.all)

  val timeAtFirstFrame = Date().getTime()
  var timeAtLastFrame =  timeAtFirstFrame

  init{
    //LABTODO: depth test
    gl.enable(GL.DEPTH_TEST)
    addComponentsAndGatherUniforms(*Program.all)
  }

  fun resize(gl : WebGL2RenderingContext, canvas : HTMLCanvasElement) {
    gl.viewport(0, 0, canvas.width, canvas.height)
    camera.setAspectRatio(canvas.width.toFloat() / canvas.height.toFloat())
  }

  @Suppress("UNUSED_PARAMETER")
  fun update(gl : WebGL2RenderingContext, keysPressed : Set<String>) {

    val timeAtThisFrame = Date().getTime() 
    val dt = (timeAtThisFrame - timeAtLastFrame).toFloat() / 1000.0f
    val t  = (timeAtThisFrame - timeAtFirstFrame).toFloat() / 1000.0f    
    timeAtLastFrame = timeAtThisFrame

//    avatar.yaw += dt

    //LABTODO: move camera
    camera.move(dt, keysPressed)

    // clear the screen
    gl.clearColor(0.3f, 0.0f, 0.3f, 1.0f)
    gl.clearDepth(1.0f)
    gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)

    val spawn = ArrayList<GameObject>()
    val killList = ArrayList<GameObject>()    
    gameObjects.forEach { 
      if(!it.move(t, dt, keysPressed, gameObjects, spawn)){
        killList.add(it)
      }
    }
    killList.forEach{ gameObjects.remove(it) }
    spawn.forEach{ gameObjects.add(it) }
  
    gameObjects.forEach { it.update() }
    backgroundMesh.draw( camera )


    gameObjects.forEach { it.draw( camera ) }
  }
}
