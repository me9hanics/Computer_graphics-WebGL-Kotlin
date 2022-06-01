import org.khronos.webgl.WebGLRenderingContext as GL

abstract class VertexArray {}

abstract external class WebGL2RenderingContext : GL { //#external# Extend Kotlin's built-in WebGL context interface with WebGL2 methods. We know these are there in JS, and now the Kotlin2JS transpiler knows, too. ˙HUN˙ Kiterjesztjük a Kotlin beépített WebGL interfészét WebGL2-es metódusokkal. Tudjuk, hogy JS-ben ezek megvannak, és most más a Kotlin2JS fordító is tudja.
  fun drawBuffers(buffers: IntArray)
  fun drawElementsInstanced(mode: Int, count: Int, type: Int, offset: Int, instanceCount: Int)
  fun readBuffer(src: Int)
  fun renderbufferStorageMultisample(target: Int, samples: Int, internalformat: Int, width: Int, height: Int)
  fun vertexAttribDivisor(index: Int, divisor: Int)
  fun vertexAttribIPointer(index: Int, size: Int, type: Int, stride: Int, offset: Int)
  fun createVertexArray() : VertexArray
  fun bindVertexArray(vertexArray: VertexArray?)
}

