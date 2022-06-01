#version 300 es

precision highp float;

in vec4 tex;
in vec4 modelPosition;
in vec4 worldPosition;
out vec4 fragmentColor;

//uniform struct {
//} material;


void main(void) {
  
  fragmentColor = vec4((sin(modelPosition.xyz * 10.0f) + 1.0f) * 0.5f, 1);
}