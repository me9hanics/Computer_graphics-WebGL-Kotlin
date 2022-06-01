#version 300 es

precision highp float;

in vec2 texCoord;

uniform struct {
  sampler2D colorTexture; 
} material;

out vec4 fragmentColor;

void main(void) {
  fragmentColor = texture(material.colorTexture, texCoord); 
}