import org.w3c.xhr.XMLHttpRequest
import org.w3c.dom.events.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import vision.gears.webglmath.Geometry

@Serializable
data class JsonMesh(
	val vertices : Array<Float>,
	val normals : Array<Float>,
	val texturecoords : Array<Array<Float>>,
	val faces : Array<Array<Short>>)

@Serializable
data class JsonModel(
	val meshes : Array<JsonMesh>)

class JsonLoader(){
  fun load(gl : WebGL2RenderingContext,
      jsonModelFileUrl : String) : Array<Geometry> {
    val request = XMLHttpRequest()
    request.overrideMimeType("application/json")
    request.open("GET", jsonModelFileUrl, false)
    var geometries : Array<Geometry>? = null
    request.onloadend = { 
      val json = Json { ignoreUnknownKeys=true }
      val jsonModel = json.decodeFromString(
                       JsonModel.serializer(), request.responseText)
      geometries = Array<Geometry>(jsonModel.meshes.size) {
        i -> SubmeshGeometry(gl, jsonModel.meshes[i])
      }
      Unit
    }
    request.send()
    return geometries!!
  }
}