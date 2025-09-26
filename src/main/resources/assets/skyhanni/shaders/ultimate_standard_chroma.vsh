// Ultimate Chroma Vertex Shader

#version 120

varying vec4 originalColor;

void main() {
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;

    // Pass original color to fragment
    originalColor = gl_Color;
}
