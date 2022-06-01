#version 300 es

precision highp float;

out vec4 fragmentColor; 

void main(void) {
  fragmentColor = vec4(0, 0, 0, 1); //#1, 1, 0, 1# solid yellow
}
