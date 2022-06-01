#version 300 es

//fragment shader

precision highp float;

out vec4 fragmentColor; 
in vec4 worldPosition;
in vec4 modelPosition;
//in vec4 color;


void main(void) {
  
  fragmentColor = vec4(fract(atan(modelPosition.x,modelPosition.y)*2.0f),0,0,1); 
  
  
  

  
  
}