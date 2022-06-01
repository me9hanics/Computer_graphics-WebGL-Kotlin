#version 300 es

in vec4 vertexPosition;
in vec4 vertexTexCoord;
uniform struct{
	mat4 modelMatrix;
} gameObject;

uniform struct{
	mat4 viewProjMatrix;
} camera;

out vec4 tex;
out vec4 modelPosition;
out vec4 worldPosition;

void main(void) {
  modelPosition = vertexPosition;
  worldPosition = vertexPosition * gameObject.modelMatrix;
  gl_Position = worldPosition * camera.viewProjMatrix;
  tex = vertexTexCoord;
}