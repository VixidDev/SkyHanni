// Textured Ultimate Chroma Fragment Shader

#version 120

uniform float chromaSize;
uniform float timeOffset;
uniform float saturation;
uniform bool forwardDirection;

uniform vec3 hues;
uniform vec3 saturations;
uniform vec3 brightnesses;

uniform sampler2D outTexture;

varying vec2 outTextureCoords;
varying vec4 outColor;

float rgb2b(vec3 rgb) {
    return max(max(rgb.r, rgb.g), rgb.b);
}

vec3 hsb2rgb_smooth(vec3 c) {
    vec3 rgb = clamp(abs(mod(c.x * 6.0 + vec3(0.0, 4.0, 2.0), 6.0) - 3.0) - 1.0, 0.0, 1.0);
    rgb = rgb * rgb * (3.0 - 2.0 * rgb); // Cubic smoothing
    return c.z * mix(vec3(1.0), rgb, c.y);
}

void main() {
    vec4 originalColor = texture2D(outTexture, outTextureCoords) * outColor;

    // Determine the direction chroma moves
    float fragCoord;
    if (forwardDirection) {
        fragCoord = gl_FragCoord.x - gl_FragCoord.y;
    } else {
        fragCoord = gl_FragCoord.x + gl_FragCoord.y;
    }

    float progress = mod((fragCoord / chromaSize) - timeOffset, 1.0) * 3;
    int lowerIndex = int(mod(progress, 3));
    int higherIndex = int(mod(lowerIndex + 1, 3));
    float innerProgress = mod(progress, 1);

    float hue = mix(hues[lowerIndex], hues[higherIndex], innerProgress);
    float saturationMul = mix(saturations[lowerIndex], saturations[higherIndex], innerProgress);
    float brightness = mix(brightnesses[lowerIndex], brightnesses[higherIndex], innerProgress);

    // The hue takes in account the position, chroma settings, and time
    // float hue = mod(((fragCoord) / chromaSize) - timeOffset, 1.0);

    // Set the color to use the new hue & original saturation/value/alpha values
    gl_FragColor = vec4(hsb2rgb_smooth(vec3(hue, saturation * saturationMul, rgb2b(originalColor.rgb) * brightness)), originalColor.a);
}
