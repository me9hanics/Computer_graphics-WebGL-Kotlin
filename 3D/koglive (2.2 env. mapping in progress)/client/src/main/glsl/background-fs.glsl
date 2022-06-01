#version 300 es

precision highp float;

out vec4 fragmentColor;

uniform struct{
  samplerCube environment;
} material;

in vec3 rayDir;

void main(void) {
//  if(rayDir.y < -0.0 ) {
 //   fragmentColor = vec4(1, 0, 1, 1);
//    } else { 
    fragmentColor = texture(material.environment, normalize(rayDir));
//  }
}
