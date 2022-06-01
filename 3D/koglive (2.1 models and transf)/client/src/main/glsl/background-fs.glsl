#version 300 es

precision highp float;

out vec4 fragmentColor;

uniform struct{
  samplerCube environment;
} material;

in vec3 rayDir;

void main(void) {
    fragmentColor = texture(material.environment, normalize(rayDir));
}
