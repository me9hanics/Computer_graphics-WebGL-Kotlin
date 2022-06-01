#version 300 es

precision highp float;

in vec2 tex;
in vec4 modelPosition;
in vec4 worldPosition;
in vec3 worldNormal;
out vec4 fragmentColor;

uniform struct {
  samplerCube environment;
} material;


void main(void) {
  fragmentColor = texture(material.environment, normalize(worldNormal));
//  fragmentColor = vec4(normalize(worldNormal), 1);
}
