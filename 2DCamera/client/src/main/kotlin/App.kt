import kotlin.browser.document
import kotlin.browser.window
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.*
import org.w3c.dom.Window

class App(val canvas : HTMLCanvasElement, val overlay : HTMLDivElement) {

  val gl = (canvas.getContext("webgl2", object{val alpha = false}) ?: throw Error("Browser does not support WebGL2")) as WebGL2RenderingContext 

  val scene = Scene(gl)
  init {
    resize()
  }

  fun resize() {
    canvas.width = canvas.clientWidth
    canvas.height = canvas.clientHeight
    scene.resize(gl, canvas)
  }

  
  var mouse : Boolean = false
  var mouseX : Int = 0
  var mouseY : Int = 0 
  val keysPressed : MutableSet<String> = HashSet<String>()
  val press : MutableSet<String> = HashSet<String>()

  @Suppress("UNUSED_PARAMETER")
  fun registerEventHandlers() {
    document.onkeydown =  { //#{# locally defined function
      event : KeyboardEvent ->
      keysPressed.add( keyNames[ event.keyCode ] )
      press.add( keyNames[ event.keyCode ] )
    }

    document.onkeyup = { 
      event : KeyboardEvent ->
      keysPressed.remove( keyNames[ event.keyCode ] )
    }

    canvas.onmousedown = { 
      event : Event ->
      console.log("lenyom")
      mouse=true
      
      event
    }

    canvas.onmousemove = { 
      event : MouseEvent ->
      mouseX = event.x.toInt()
      mouseY = event.y.toInt()
      event.stopPropagation()
    }

    canvas.onmouseup = { 
      event : Event ->
      mouse = false
      console.log("elenged onmouseUP")
      event // This line is a placeholder for event handling code. It has no effect, but avoids the "unused parameter" warning.
    }

    canvas.onmouseout = { 
      event : Event ->
      console.log("mouse out")
      event // This line is a placeholder for event handling code. It has no effect, but avoids the "unused parameter" warning.
    }

    window.onresize = {
      event : Event ->
      resize()
    }

    window.requestAnimationFrame {//#requestAnimationFrame# trigger rendering
      update()//#update# this method is responsible; for drawing a frame
    }
  }  

  fun update() {
    scene.update(gl, keysPressed, press, mouseX, mouseY, mouse)   
this.overlay.innerHTML = 
"""<div style=
"position:absolute;left:${mouseX}px;bottom:-${mouseY}px"> ${keysPressed.toString()} """
    window.requestAnimationFrame { update() }
  }
}

fun main() {
  val canvas = document.getElementById("canvas") as HTMLCanvasElement
  val overlay = document.getElementById("overlay") as HTMLDivElement
  overlay.innerHTML = """<font color="red">WebGL</font>"""

  try{
    val app = App(canvas, overlay)//#app# from this point on,; this object is responsible; for handling everything
    app.registerEventHandlers()//#registerEventHandlers# we implement this; to make sure the app; knows when there is; something to do
  } catch(e : Error) {
    console.error(e.message)
  }
}