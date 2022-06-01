import org.khronos.webgl.WebGLRenderingContext as GL
import org.khronos.webgl.Float32Array
import org.khronos.webgl.Uint16Array
import kotlin.collections.ArrayList.*
import kotlin.math.*
import vision.gears.webglmath.Geometry

class HeartGeometry(val gl : WebGL2RenderingContext) : Geometry() {
  var heartVertexes = arrayListOf<Float>()
  init{
    heartVertexes.add(0.0f)
    heartVertexes.add(0.0f)
    heartVertexes.add(0.0f)
    for(i in 0..498){    //
      heartVertexes.add(((sin((i).toFloat()*2*(PI).toFloat() /498.0f)).toFloat().pow(3.0f) /4.0f))           
      heartVertexes.add((cos((i).toFloat()*2*(PI).toFloat() /498.0f)).toFloat() *13.0f /64.0f - (cos((2*i).toFloat()*2*(PI).toFloat() /498.0f)).toFloat() *5.0f /64.0f - (cos((3*i).toFloat()*2*(PI).toFloat() /498.0f)).toFloat() *2.0f /64.0f - (cos((4*i).toFloat()*2*(PI).toFloat() /498.0f)).toFloat() /64.0f)
      heartVertexes.add(0.00f)
  }
  }

  val vertexBuffer = gl.createBuffer()
  init{
    gl.bindBuffer(GL.ARRAY_BUFFER, vertexBuffer)
    gl.bufferData(GL.ARRAY_BUFFER,Float32Array((heartVertexes).toTypedArray()) , GL.STATIC_DRAW) 
  }

  val heartColor = arrayListOf<Float>()
  init{
    for(i in 1..1000){    
      heartColor.add(0.887f)      
      heartColor.add(0.105f)
      heartColor.add(0.137f)
    }
  }
  

  val vertexBufferColor = gl.createBuffer()
  init{
    gl.bindBuffer(GL.ARRAY_BUFFER, vertexBufferColor)
    gl.bufferData(GL.ARRAY_BUFFER,Float32Array((heartColor).toTypedArray()),GL.STATIC_DRAW)
  }
  
  val heartIndex = arrayListOf<Short>()
  init{
    for(i in 1..498){
      
      heartIndex.add((0).toShort())
      heartIndex.add((i).toShort())
      heartIndex.add((i+(1)).toShort())
    }
    heartIndex.add((0).toShort())
    heartIndex.add((499).toShort())
    heartIndex.add((1).toShort())
  }

  val indexBuffer = gl.createBuffer()
  init{
    gl.bindBuffer(GL.ELEMENT_ARRAY_BUFFER, indexBuffer)
    gl.bufferData(GL.ELEMENT_ARRAY_BUFFER,Uint16Array((heartIndex).toTypedArray()),GL.STATIC_DRAW)
  }

  val inputLayout = gl.createVertexArray()
  init{
    gl.bindVertexArray(inputLayout)

    gl.bindBuffer(GL.ARRAY_BUFFER, vertexBuffer)
    gl.enableVertexAttribArray(0)
    gl.vertexAttribPointer(0,
      3, GL.FLOAT, //< three pieces of float
      false, //< do not normalize (make unit length)
      0, //< tightly packed
      0 //< data starts at array start
    )
    gl.bindBuffer(GL.ARRAY_BUFFER, vertexBufferColor)
    gl.enableVertexAttribArray(1)                 //ha jol ertem, az attribute az a glsl vertexben? //programban van attribute
    gl.vertexAttribPointer(1,                     
      3, GL.FLOAT, //< three pieces of float
      false, //< do not normalize (make unit length)
      0, //< tightly packed
      0 //< data starts at array start
    )
    gl.bindVertexArray(null)
  }

  override fun draw() {

    gl.bindVertexArray(inputLayout)
    gl.bindBuffer(GL.ELEMENT_ARRAY_BUFFER, indexBuffer)  

    gl.drawElements(GL.TRIANGLES, 1497, GL.UNSIGNED_SHORT, 0) 
  }

}
