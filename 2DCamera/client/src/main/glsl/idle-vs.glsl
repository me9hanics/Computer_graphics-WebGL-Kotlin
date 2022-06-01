#version 300 es

in vec4 vertexColor;
in vec4 vertexPosition; //#vec4# A four-element vector [x,y,z,w].; We leave z and w alone.; They will be useful later for 3D graphics and transformations. #vertexPosition# attribute fetched from vertex buffer according to input layout spec

uniform struct{
  vec3 position;
  float scale;
} gameObject;

out vec4 modelPosition;
out vec4 worldPosition;

void main(void) {
  modelPosition = vertexPosition;
  gl_Position = vertexPosition; //#gl_Position# built-in output, required
  gl_Position.xy *= gameObject.scale;  
  gl_Position.xyz += gameObject.position;
  worldPosition = gl_Position;
}