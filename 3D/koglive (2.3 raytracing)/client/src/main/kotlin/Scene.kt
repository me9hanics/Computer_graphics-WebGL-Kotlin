import org.w3c.dom.HTMLCanvasElement
import org.khronos.webgl.WebGLRenderingContext as GL
import org.khronos.webgl.Float32Array
import vision.gears.webglmath.UniformProvider
import vision.gears.webglmath.Vec3
import vision.gears.webglmath.Mat4
import kotlin.js.Date

class Scene (
  val gl : WebGL2RenderingContext) : UniformProvider("scene") {

  val vsQuad = Shader(gl, GL.VERTEX_SHADER, "quad-vs.glsl")
  val fsTrace = Shader(gl, GL.FRAGMENT_SHADER, "trace-fs.glsl")    
  val traceProgram = Program(gl, vsQuad, fsTrace)  
  val quadGeometry = TexturedQuadGeometry(gl)

  val traceMaterial = Material(traceProgram)

  val environmentTextureCube =       TextureCube(gl, 
        "media/posx512.jpg",
        "media/negx512.jpg",
        "media/posy512.jpg",
        "media/negy512.jpg",
        "media/posz512.jpg",
        "media/negz512.jpg"
        )

  init{
    traceMaterial["environment"]?.set(environmentTextureCube)
  }
  val traceMesh = Mesh(traceMaterial, quadGeometry)

  val camera = PerspectiveCamera(*Program.all)

  val timeAtFirstFrame = Date().getTime()
  var timeAtLastFrame =  timeAtFirstFrame

  init{
    addComponentsAndGatherUniforms(*Program.all)
  }

  val lights = Array<Light>(2) { Light(it, *Program.all) }
  init{
    lights[0].position.set(1.0f, 1.0f, 1.0f, 0.0f).normalize();
    lights[0].powerDensity.set(0.0f, 0.0f, 1.0f);
    lights[1].position.set(10.0f, 10.0f, -5.0f, 1.0f);
    lights[1].powerDensity.set(100.0f, 100.0f, 0.0f);
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

    camera.move(dt, keysPressed)

    // clear the screen
    gl.clearColor(0.3f, 0.0f, 0.3f, 1.0f)
    gl.clearDepth(1.0f)
    gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)

    traceMesh.draw( camera )
  }
}
