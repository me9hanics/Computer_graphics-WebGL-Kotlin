#version 300 es

precision highp float;

in vec4 tex;
in vec4 modelPosition;
in vec4 worldPosition;
out vec4 fragmentColor;

//uniform struct {
//} material;
float randomish(vec2 co){
    return fract(sin(dot(co, vec2(12.9898, 78.233)))/2.0f ); //Easy randomising function for Marbles I found on the internet for GLSL. Was too strong, so I took out the * 43758.5453, divided by 2
} //Not dividing by 2 makes it more interesting though


void main(void) {
  vec3 color1=vec3(1.0,0.0,0.0);
  vec3 color2=vec3(1.0,1.0,1.0);
  vec4 noise=vec4(randomish(modelPosition.xy),randomish(modelPosition.yz),randomish(modelPosition.xz),randomish(modelPosition.xy));
  float diff=abs(noise.x-0.5)+abs(noise.y-0.5)+abs(noise.z-0.5)+abs(noise.a-0.5);
  diff=clamp(diff,0.0,1.0); 
  float marblemix=sin(modelPosition.x*6.0+diff*12.0)*0.5+0.2*sin(modelPosition.y*6.0)+0.5;
  vec3 color=mix(color1,color2,marblemix)*1.0;
  fragmentColor=vec4(color,1.0);
}
