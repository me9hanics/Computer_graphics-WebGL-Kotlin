import org.khronos.webgl.WebGLRenderingContext as GL
import vision.gears.webglmath.*

class OrthoCamera(vararg programs : Program) : UniformProvider("camera") {
  val position = Vec2(0.0f, 0.0f) 
  val roll = 0.0f 
  val windowSize = Vec2(5.0f, 5.0f) 
  
  val viewProjMatrix by Mat4()
  init{
    updateViewProjMatrix()
    addComponentsAndGatherUniforms(*programs)
  }
  fun updateViewProjMatrix() { 
    viewProjMatrix.set(). 
      scale(0.5f, 0.5f). 
      scale(windowSize). 
      rotate(roll). 
      translate(position). 
      invert()
  }
  fun setAspectRatio(ar : Float) { 
  	console.log(ar)
    windowSize.x = windowSize.y * ar
    updateViewProjMatrix()
  }
}