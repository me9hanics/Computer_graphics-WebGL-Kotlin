#version 300 es

precision highp float;

in vec4 tex;
in vec4 modelPosition;
in vec4 worldPosition;
out vec4 fragmentColor;

//uniform struct {
//} material;

float snoise(vec3 r) {
  vec3 s = vec3(7502, 22777, 4767);
  float f = 0.0;
  for(int i=0; i<16; i++) {
    f += sin( dot(s - vec3(32768, 32768, 32768), r)
                                 / 65536.0);
    s = mod(s, 32768.0) * 2.0 + floor(s / 32768.0);
  }
  return f / 32.0 + 0.5;
}


void main(void) {
  //fragmentColor = fract(modelPosition.x*3.0f)* vec4(0.21, 0.11, 0, 1);
  //fragmentColor += (1.0f-fract(modelPosition.x*3.0f))* vec4(0.86, 0.47, 0.14, 1);  Simple wood, but not realistic enough
  float w = fract( modelPosition.x + 0.01*sin(modelPosition.y *13.0) + sin(modelPosition.z*3.0)*0.005 + pow(snoise(modelPosition.xyz * 4.2),10.4)* 5.6);
  vec3 color = mix( vec3(0.21, 0.11, 0), vec3(0.86, 0.47, 0.14), w); //lightbrown and darkbrown
  fragmentColor = vec4(color, 1);
}