#version 300 es

precision highp float;

out vec4 fragmentColor;

uniform struct{
  samplerCube environment;
} material;

uniform struct {
  mat4 rayDirMatrix;
  vec3 position;
} camera;

in vec3 rayDir;

void main(void) {
  fragmentColor = texture(material.environment, normalize(rayDir));
}
