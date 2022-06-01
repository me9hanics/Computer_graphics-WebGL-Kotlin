import org.khronos.webgl.WebGLRenderingContext as GL
import org.khronos.webgl.Float32Array
import org.khronos.webgl.Uint16Array
import kotlin.math.*
import vision.gears.webglmath.Geometry

class DonutGeometry(val gl : WebGL2RenderingContext) : Geometry() {
  var donutVertexes = arrayListOf<Float>()
  init{
  
    for(i in 0..499){
      donutVertexes.add((cos((i).toFloat()*2*(PI).toFloat() /499.0f)).toFloat()/5.0f)
      donutVertexes.add((sin((i).toFloat()*2*(PI).toFloat() /499.0f)).toFloat() /5.0f)
      donutVertexes.add(0.00f)
      donutVertexes.add((cos((i).toFloat()*2*(PI).toFloat()/499.0f)).toFloat()/8.0f)
      donutVertexes.add((sin((i).toFloat()*2*(PI).toFloat()/499.0f)).toFloat()/8.0f)
      donutVertexes.add(0.00f)
  }
  }

  val vertexBuffer = gl.createBuffer()
  init{
    gl.bindBuffer(GL.ARRAY_BUFFER, vertexBuffer)
    gl.bufferData(GL.ARRAY_BUFFER,Float32Array((donutVertexes).toTypedArray()) , GL.STATIC_DRAW) //donutVertexes
  }

  val donutColor = arrayListOf<Float>()
  init{
    for(i in 1..1000){    
      donutColor.add(0.781f)
      donutColor.add(0.082f)
      donutColor.add(0.522f)
    }
  }
  

  val vertexBufferColor = gl.createBuffer()
  init{
    gl.bindBuffer(GL.ARRAY_BUFFER, vertexBufferColor)
    gl.bufferData(GL.ARRAY_BUFFER,Float32Array((donutColor).toTypedArray()),GL.STATIC_DRAW)
  }
  
  val donutIndex = arrayListOf<Short>()
  init{
    for(i in 0..999){
      val si=i.toShort()
      donutIndex.add(si)
      donutIndex.add((si+(1)).toShort())
      donutIndex.add((si+(2)).toShort())
    }
    donutIndex.add(998)
    donutIndex.add(999)
    donutIndex.add(0)
    donutIndex.add(999)
    donutIndex.add(0)
    donutIndex.add(1)
  }

  val indexBuffer = gl.createBuffer()
  init{
    gl.bindBuffer(GL.ELEMENT_ARRAY_BUFFER, indexBuffer)
    gl.bufferData(GL.ELEMENT_ARRAY_BUFFER,Uint16Array((donutIndex).toTypedArray()),GL.STATIC_DRAW)
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
    gl.enableVertexAttribArray(1)                 //ha jol ertem, az attribute az a glsl vertexben?
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

    gl.drawElements(GL.TRIANGLES, 3000, GL.UNSIGNED_SHORT, 0) //3000: 1000db 3szog
  }

}
