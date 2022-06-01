import vision.gears.webglmath.UniformProvider
import vision.gears.webglmath.*


class GameObject(mesh : Mesh) : UniformProvider("gameObject") {

  val position = Vec3()
  var roll = 0.0f 
  val scale = Vec3(1.0f, 1.0f, 1.0f)  

  val modelMatrix by Mat4()

  init {
    addComponentsAndGatherUniforms(mesh)
  }

  fun update(){
    modelMatrix.set().scale(scale).rotate(roll).translate(position)  	
  }
}