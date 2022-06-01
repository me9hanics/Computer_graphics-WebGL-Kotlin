import org.w3c.dom.HTMLCanvasElement
import org.khronos.webgl.WebGLRenderingContext as GL
import org.khronos.webgl.Float32Array
import vision.gears.webglmath.UniformProvider
import vision.gears.webglmath.Vec3
import vision.gears.webglmath.Mat4
import kotlin.math.*
import kotlin.js.Date

class Scene (
  val gl : WebGL2RenderingContext) : UniformProvider("scene") {

  val vsTrafo = Shader(gl, GL.VERTEX_SHADER, "trafo-vs.glsl")
  val vsShadow = Shader(gl, GL.VERTEX_SHADER, "shadow-vs.glsl")
  val fsTextured = Shader(gl, GL.FRAGMENT_SHADER, "textured-fs.glsl")  
  val fsWood = Shader(gl, GL.FRAGMENT_SHADER, "wood-fs.glsl")
  val vsQuad = Shader(gl, GL.VERTEX_SHADER, "quad-vs.glsl")
  val fsBackground = Shader(gl, GL.FRAGMENT_SHADER, "background-fs.glsl")    
  val fsFun = Shader(gl, GL.FRAGMENT_SHADER, "fract-fs.glsl")
  val fsMarble = Shader(gl, GL.FRAGMENT_SHADER, "marble-fs.glsl")
  val fsShadow = Shader(gl, GL.FRAGMENT_SHADER, "shader-fs.glsl")
  val fsSolid = Shader(gl, GL.FRAGMENT_SHADER, "solid-fs.glsl")

  val texturedProgram = Program(gl, vsTrafo, fsTextured)
  val woodProgram = Program(gl, vsTrafo, fsWood)  
  val backgroundProgram = Program(gl, vsQuad, fsBackground)  
  val quadGeometry = TexturedQuadGeometry(gl)
  val funProgram = Program(gl, vsTrafo, fsFun)
  val marbleProgram = Program(gl, vsTrafo, fsMarble)
  val shadowProgram = Program(gl, vsShadow, fsShadow)
  val solidProgram = Program(gl, vsTrafo, fsSolid)
  //val groundGeometry = Program(gl, vertexShader, fragmentShader)

  val solidMaterial = Material(solidProgram)
  val woodMaterial = Material(woodProgram)
  val backgroundMaterial = Material(backgroundProgram)
  init{
    backgroundMaterial["environment"]?.set(
      TextureCube(gl, 
        "media/posx512.jpg",
        "media/negx512.jpg",
        "media/posy512.jpg",
        "media/negy512.jpg",
        "media/posz512.jpg",
        "media/negz512.jpg"
        )
      )
  }
  val funMaterial = Material(funProgram)
  val marbleMaterial = Material(marbleProgram)
  val shadowMaterial = Material(shadowProgram)

  val backgroundMesh = Mesh(backgroundMaterial, quadGeometry)

  val slowpokeMaterial = Material(texturedProgram)
  val tileMaterial = Material(texturedProgram)
  val slowpokeEyeMaterial = Material(texturedProgram)  
  init{
    slowpokeMaterial["colorTexture"]?.set(Texture2D(gl, "media/slowpoke/YadonDh.png"))
    slowpokeEyeMaterial["colorTexture"]?.set(Texture2D(gl, "media/slowpoke/YadonEyeDh.png"))  
    tileMaterial["colorTexture"]?.set(Texture2D(gl, "media/rainbow.jpg"))
  }

  val gameObjects = ArrayList<GameObject>()
  val noShadowGameObjects = ArrayList<GameObject>()
  val jsonLoader = JsonLoader()
  val geometries = jsonLoader.load(gl, "media/slowpoke/Slowpoke.json")
  val slowpokeMultiMesh = MultiMesh(
    arrayOf<Material>(slowpokeMaterial, slowpokeEyeMaterial),
    geometries)
  val woodenSlowpokeMultiMesh = MultiMesh(
    arrayOf<Material>(woodMaterial, woodMaterial),
    geometries)  
  val funSlowpokeMultiMesh= MultiMesh(arrayOf<Material>(funMaterial, funMaterial), geometries)
  val marbleSlowpokeMultiMesh = MultiMesh(
    arrayOf<Material>(marbleMaterial, marbleMaterial),
    geometries) 
  val groundMesh = Mesh(tileMaterial, GroundGeometry(gl))

  val avatar = GameObject(woodenSlowpokeMultiMesh)
  val funavatar = GameObject(funSlowpokeMultiMesh)
  val marble = GameObject(marbleSlowpokeMultiMesh)
  val ground = GameObject(groundMesh)
  init{
    //LABTODO: add object from JSON
    gameObjects.add(avatar)
    funavatar.position.set(5.0f, -0.5f, 0.0f)
    funavatar.scale.set(0.1f, 0.1f, 0.1f)
    funavatar.yaw = 1.5f
    gameObjects.add(funavatar)
    avatar.scale.set(0.3f,0.3f,0.3f)
    avatar.position.set(-5.5f, 4.0f, 0.0f)
    gameObjects.add(marble)
    marble.position.set(8.0f, 0.0f, 8.0f)

    gameObjects.add( GameObject(slowpokeMultiMesh)) 
    gameObjects.add(ground)
    ground.pitch = 1.57f
    //LABTODO: add background object
    noShadowGameObjects.add(ground)
    //noShadowGameObjects.add()
  }
  

  val camera = PerspectiveCamera(*Program.all)
  val shadowMatrix = Mat4().scale(1.0f,0.0f,1.0f).translate(0.0f,0.01f,0.0f) //this is for plane projection shadow

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

    avatar.yaw += dt

    //LABTODO: move camera
    camera.move(dt, keysPressed)
    if(keysPressed.contains("P")){
      //third person camera
      camera.yaw=avatar.yaw //+3.14f
      camera.position -= camera.position //nullify
      camera.position += avatar.position-Vec3(15.0f*cos(avatar.yaw),0.0f,15.0f*sin(avatar.yaw)) //+3.14f
    }

    if(keysPressed.contains("L")) //point-light projection shadow
    {
      val lightpos=Vec3(30.0f,40.0f,30.0f) //single light direction: [1 0 0 0; a 0 c 0; 0 0 1 0; 0 0.01 0 1] where for a and c, lightdirX+a*lightdirY=0, lightdirZ+c*lightdirY=0
      //shadowMatrix= Mat4(1.0f,0.0f,0.0f,0.0f,-lightpos.x/lightpos.y, 0.0f, -lightpos.z/lightpos.y, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.01f, 0.0f, 1.0f) this is for directional light 
      //shadowMatrix=Mat4().translate(-lightpos)*Mat4(1.0f,0.0f,0.0f,0.0f,-lightpos.x/lightpos.y, 0.0f, -lightpos.z/lightpos.y, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -lightpos.y, 0.0f, 1.0f)
      //shadowMatrix.translate(lightpos) 
      //point-light
    }


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
    gameObjects.forEach{
      //if(!it.noShadowGameObjects){  //! noShadowGameObjects.contains(it)
        it.using(shadowMaterial).draw(this, this.camera)
      //}
    }
  }
}
