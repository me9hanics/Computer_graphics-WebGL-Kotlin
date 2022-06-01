#version 300 es

precision highp float;

out vec4 fragmentColor; 
in vec4 modelPosition;
in vec4 worldPosition;

uniform struct {
  vec3 solidColor;
} material;

void main(void) {
  fragmentColor = vec4(material.solidColor, 1); //vec4(1, 0.5, 0.65, 1);
}
