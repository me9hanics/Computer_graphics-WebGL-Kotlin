#version 300 es

precision highp float;

in vec4 tex;
out vec4 fragmentColor;

uniform struct {
	sampler2D colorTexture;
} material;


void main(void) {
  fragmentColor = texture(material.colorTexture, tex.xy/tex.w);
}
