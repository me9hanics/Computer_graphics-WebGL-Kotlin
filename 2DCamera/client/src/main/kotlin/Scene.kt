import org.w3c.dom.HTMLCanvasElement
import org.khronos.webgl.WebGLRenderingContext as GL //# GL# we need this for the constants declared ˙HUN˙ a constansok miatt kell
import kotlin.js.Date
import kotlin.math.*
import vision.gears.webglmath.*
import kotlin.Enum.*

class Scene (
  val gl : WebGL2RenderingContext) : UniformProvider("scene"){
  //Shaders
  val vsTransform = Shader(gl, GL.VERTEX_SHADER, "transform-vs.glsl")
  val fsSolid = Shader(gl, GL.FRAGMENT_SHADER, "solid-fs.glsl")
  val fsATAN = Shader(gl, GL.FRAGMENT_SHADER, "atan-fs.glsl")

  //Program
  val solidProgram = Program(gl, vsTransform, fsSolid, Program.PC)
  val atanProgram = Program(gl, vsTransform, fsATAN, Program.PC)
  
  //Camera
  val camera = OrthoCamera(*Program.all)

  //Material
  val greenMaterial = Material(solidProgram)
  init{
    greenMaterial["solidColor"]?.set(0.5f, 1.0f, 0.5f)
  }
  val navyBlueMaterial = Material(solidProgram)
  init{
    navyBlueMaterial["solidColor"]?.set(0.5f, 0.5f, 1.0f)
  }  
  val whiteMaterial = Material(solidProgram)
  init{
    whiteMaterial["solidColor"]?.set(0.95f, 0.95f, 1.0f)
  }    
  val yellowMaterial = Material(solidProgram)  
  init{
    yellowMaterial["solidColor"]?.set(1.0f,0.8f,0.1f)
  }
  val redMaterial = Material(solidProgram) 
  init{
    redMaterial["solidColor"]?.set(1.0f,0.0f,0.0f)
  }
  val pinkMaterial = Material(solidProgram)
  init{
    pinkMaterial["solidColor"]?.set(0.78f, 0.08f, 0.52f)
  }
  val atanMaterial = Material(atanProgram)
  init{}

  //Geometry
  val triangleGeometry = TriangleGeometry(gl)
  val quadGeometry = QuadGeometry(gl)
  val heartGeometry = HeartGeometry(gl) 
  val donutGeometry = DonutGeometry(gl)    
  val eggGeometry = EggGeometry(gl)

  //Mesh
  val greenTriangle = Mesh(greenMaterial, triangleGeometry)
  val navyBlueTriangle = Mesh(navyBlueMaterial, triangleGeometry)  
  val redTriangle = Mesh(redMaterial, triangleGeometry)
  val whiteHeart = Mesh(whiteMaterial, heartGeometry)
  val yellowHeart = Mesh(yellowMaterial, heartGeometry)
  val pinkDonut = Mesh(pinkMaterial,donutGeometry)
  //val funnyEgg = Mesh(atanMaterial,eggGeometry)
  val whiteEgg = Mesh(whiteMaterial,eggGeometry)

  //GameObject
  val gameObjects = ArrayList<GameObject>()
  init {
    gameObjects.add(GameObject(greenTriangle))
    gameObjects.add(GameObject(navyBlueTriangle))
    gameObjects.add(GameObject(redTriangle))
    gameObjects.add(GameObject(whiteHeart))
    gameObjects.add(GameObject(yellowHeart))
    gameObjects.add(GameObject(pinkDonut))
    gameObjects.add(GameObject(whiteEgg))
    gameObjects[0].position.set(Vec3(2.2f, 1.8f,0.0f))
    gameObjects[1].position.set(Vec3(-2.1f,-2.0f,0.0f))
    gameObjects[2].position.set(Vec3(-1.8f,1.7f,0.0f))
    gameObjects[3].position.set(Vec3(0.4f,-2.1f,0.0f))
    gameObjects[4].position.set(Vec3(2.6f,-1.6f,0.0f))
    gameObjects[5].position.set(Vec3(1.2f, 2.2f,0.0f))
    gameObjects[6].position.set(Vec3())  

    gl.enable(GL.BLEND)
    gl.blendFunc(GL.SRC_ALPHA, GL.ONE_MINUS_SRC_ALPHA)
  }
  val selectedgameObjects = ArrayList<GameObject>()
  init{
    selectedgameObjects.add(gameObjects[5])
  }


  //Other
  var canvasX = 1
  var canvasY = 1 
  //val timeAtFirstFrame = Date().getTime()
  //var timeAtLastFrame =  timeAtFirstFrame
  //val time by Vec1()
  var mouseworld = Vec4()
  var pivot = Vec4()
  var mouseAtLastFrame = mouseworld
  
  init{
    addComponentsAndGatherUniforms(*Program.all)
  }
  

  //Functions
  fun resize(gl : WebGL2RenderingContext, canvas : HTMLCanvasElement) {
    gl.viewport(0, 0, canvas.width, canvas.height)//#viewport# tell the rasterizer which part of the canvas to draw to ˙HUN˙ a raszterizáló ide rajzoljon
    camera.setAspectRatio(canvas.width.toFloat() / canvas.height.toFloat())
    canvasX=canvas.width
    canvasY=canvas.height
  }

  fun MouseNDC(xpos : Int, ypos: Int ){
    var mouseYNDC=(canvasY.toFloat()-ypos.toFloat())/(canvasY.toFloat())*2.0f-1.0f
    var mouseXNDC=((xpos).toFloat()/(canvasX.toFloat()))*2.0f - 1.0f
    mouseworld = Vec4(mouseXNDC, mouseYNDC, 0.0f, 1.0f) * camera.viewProjMatrix.clone().invert()
  }
  

  @Suppress("UNUSED_PARAMETER")
  fun update(gl : WebGL2RenderingContext, keysPressed : MutableSet<String>, press : MutableSet<String> ,mouseX : Int = 0, mouseY : Int = 0 , mouse : Boolean = false) {
    //Variables  
    val timeAtThisFrame = Date().getTime()
    //val dt = (timeAtThisFrame - timeAtLastFrame).toFloat() / 1000.0f
    //val t  = (timeAtThisFrame - timeAtFirstFrame).toFloat() / 1000.0f      
    //time.set(t)
    //timeAtLastFrame = timeAtThisFrame
    val mouseAtLastFrame = mouseworld
    val pivot = Vec4().set()*(camera.viewProjMatrix.clone().invert())
    
    MouseNDC(mouseX,mouseY) //to get mouseworld coordinates

    val mouseAtThisFrame =mouseworld
    val ds = mouseAtThisFrame-mouseAtLastFrame
    val dtheta = (atan2(mouseAtThisFrame.y - pivot.y, mouseAtThisFrame.x - pivot.x) - atan2(mouseAtLastFrame.y - pivot.y, mouseAtLastFrame.x - pivot.x)).toFloat()
    

    //Set values (if needed)
    if (gameObjects.lastIndex > 0){
      gameObjects[gameObjects.lastIndex].roll += 0.2f
    }


    //Settings
    gl.clearColor(0.99f, 0.36f, 0.01f, 1.0f)
    gl.clearDepth(1.0f)
    gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)


    //Camera settings
    if(press.contains("Z") ){
      if(camera.windowSize.x > 0.5f && camera.windowSize.y > 0.5f )
      camera.windowSize -= Vec2(0.5f,0.5f)
      camera.updateViewProjMatrix()
      press.remove("Z")
    }
    if(press.contains("X")  ){  //&& ("X" !in keysPressedBefore)
      camera.windowSize += Vec2(0.5f,0.5f)
      camera.updateViewProjMatrix()
      press.remove("X")
    }
    if(mouse && keysPressed.contains("T")){
      camera.position -= Vec2(2.0f*ds.x/camera.windowSize.x , 2.0f* ds.y /camera.windowSize.y)
      camera.updateViewProjMatrix()
    }


    //Selection + settings
    if(mouse && (keysPressed.contains("W"))){
      var s=0
      gameObjects.forEach { 
        if( (Vec4(it.position.xy, 0.0f, 1.0f)- mouseworld ).length() < 0.05f * (camera.windowSize.x+camera.windowSize.y)){ 
          if(it !in selectedgameObjects){
            selectedgameObjects.add(it)
          }
          s += 1
        }
       }
      if(s==0){
        gameObjects.forEach{
          selectedgameObjects.remove(it)
          }
      } 
    }

    if(mouse && (keysPressed.contains("M"))){
      selectedgameObjects.forEach { 
       it.position += ds.xyz
      } 
    }
    if(mouse && (keysPressed.contains("R"))){
      selectedgameObjects.forEach { 
       it.roll += dtheta
      } 
    }
    
    if(keysPressed.contains("DELETE")){
      selectedgameObjects.forEach{
        selectedgameObjects.remove(it)
        gameObjects.remove(it)
      }
    }

    //if(press.contains("D")){
      //selectedgameObjects.forEach{
        //gameObjects.add(it.clone()))
        //gameObjects[gameObjects.lastIndex].position += Vec3(1.0f,1.0f,0.0f)
      //}
      //press.remove("D")
    //}

    if(press.contains("N")  && keysPressed.contains("P")){ //&& ("N" !in keysPressedBefore)
      //this didnt work because it returns a uniform instead of gameObject, how to revolve around this? i wanted to keep the idea: gameObjects.add( GameObject(yellowHeart).position.set(mouseworld.xyz) )
      gameObjects.add(GameObject(yellowHeart))
      gameObjects[gameObjects.lastIndex].position.set(mouseworld.xyz)
      press.remove("N")
    }

    if(press.contains("N")  && keysPressed.contains("C")){ 
      gameObjects.add(GameObject(yellowHeart))
      gameObjects[gameObjects.lastIndex].position.set(pivot.xyz)
      press.remove("N")
    }

    
    //Draw
    gameObjects.forEach{
      it.update()
    }
    gameObjects.forEach{
      it.draw( camera, this )
    }
    selectedgameObjects.forEach{
      it.update()
    }
    selectedgameObjects.forEach{
      it.using(atanMaterial).draw( camera, this )
    }

    
  }
}
