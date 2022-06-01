import org.khronos.webgl.WebGLRenderingContext as GL
import org.khronos.webgl.Float32Array
import org.khronos.webgl.Uint16Array
import kotlin.collections.ArrayList.*
import kotlin.math.*
import vision.gears.webglmath.Geometry

class EggGeometry(val gl : WebGL2RenderingContext) : Geometry() {
  var eggVertexes = arrayListOf<Float>()
  init{
    eggVertexes.add(0.0f)
    eggVertexes.add(0.0f)
    eggVertexes.add(0.0f)
    for(i in 1..699){    //
      eggVertexes.add(((cos( (i).toFloat()*2*(PI).toFloat() /699.0f)).toFloat()/999.0f + ((cos((i).toFloat()*2*(PI).toFloat() /699.0f)).toFloat() *0.2f))* (cos((i).toFloat()*2*(PI).toFloat() /699.0f)).toFloat()*2.2f )          
      eggVertexes.add((sin((i).toFloat()*2*(PI).toFloat() /699.0f)).toFloat()* ( ((cos((i).toFloat()*2*(PI).toFloat() /699.0f)).toFloat() *0.2f)* (cos((i).toFloat()*2*(PI).toFloat() /699.0f)).toFloat()*2.2f))
      eggVertexes.add(0.00f)
  }
  }

  val vertexBuffer = gl.createBuffer()
  init{
    gl.bindBuffer(GL.ARRAY_BUFFER, vertexBuffer)
    gl.bufferData(GL.ARRAY_BUFFER,Float32Array((eggVertexes).toTypedArray()) , GL.STATIC_DRAW) 
  }

  val eggColor = arrayListOf<Float>()
  init{
    for(i in 1..100){    
      eggColor.add(0.6f)      
      eggColor.add(0.0f)
      eggColor.add(0.8f)
      eggColor.add(0.30f)      
      eggColor.add(0.0f)
      eggColor.add(0.53f)
      eggColor.add(0.0f)      
      eggColor.add(0.0f)
      eggColor.add(0.99f)
      eggColor.add(0.0f)      
      eggColor.add(0.99f)
      eggColor.add(0.0f)
      eggColor.add(0.255f)      
      eggColor.add(0.255f)
      eggColor.add(0.0f)
      eggColor.add(0.99f)      
      eggColor.add(0.5f)
      eggColor.add(0.0f)
      eggColor.add(0.99f)      
      eggColor.add(0.0f)
      eggColor.add(0.0f)
    }
  }
  

  val vertexBufferColor = gl.createBuffer()
  init{
    gl.bindBuffer(GL.ARRAY_BUFFER, vertexBufferColor)
    gl.bufferData(GL.ARRAY_BUFFER,Float32Array((eggColor).toTypedArray()),GL.STATIC_DRAW)
  }
  
  val eggIndex = arrayListOf<Short>()
  init{
    for(i in 1..698){
      
      eggIndex.add((0).toShort())
      eggIndex.add((i).toShort())
      eggIndex.add((i+(1)).toShort())
    }
    eggIndex.add((0).toShort())
    eggIndex.add((699).toShort())
    eggIndex.add((1).toShort())
  }

  val indexBuffer = gl.createBuffer()
  init{
    gl.bindBuffer(GL.ELEMENT_ARRAY_BUFFER, indexBuffer)
    gl.bufferData(GL.ELEMENT_ARRAY_BUFFER,Uint16Array((eggIndex).toTypedArray()),GL.STATIC_DRAW)
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

    gl.drawElements(GL.TRIANGLES, 2097, GL.UNSIGNED_SHORT, 0) 
  }  
}
