package com.laka.live.gpuimage;

import android.opengl.GLES20;

/**
 * Created by luwies on 16/6/30.
 */
public class GPUImageBeautifyFilter extends GPUImageFilter {

    private static final String BEAUTY_VERTEX_SHADER = "" +
            "attribute vec4 position;\n" +
            " attribute vec4 inputTextureCoordinate;\n" +
            " \n" +
            " const int GAUSSIAN_SAMPLES = 9;\n" +
            " \n" +
            " uniform float texelWidthOffset;\n" +
            " uniform float texelHeightOffset;\n" +
            " \n" +
            " varying vec2 textureCoordinate;\n" +
            " varying vec2 blurCoordinates[GAUSSIAN_SAMPLES];\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     gl_Position = position;\n" +
            "     textureCoordinate = inputTextureCoordinate.xy;\n" +
            "     \n" +
            "     // Calculate the positions for the blur\n" +
            "     int multiplier = 0;\n" +
            "     vec2 blurStep;\n" +
            "     vec2 singleStepOffset = vec2(texelWidthOffset, texelHeightOffset);\n" +
            "     \n" +
            "     for (int i = 0; i < GAUSSIAN_SAMPLES; i++)\n" +
            "     {\n" +
            "         multiplier = (i - ((GAUSSIAN_SAMPLES - 1) / 2));\n" +
            "         // Blur in x (horizontal)\n" +
            "         blurStep = float(multiplier) * singleStepOffset;\n" +
            "         blurCoordinates[i] = inputTextureCoordinate.xy + blurStep;\n" +
            "     }\n" +
            " }";

    private static final String BEAUTY_FRAGMENT_SHADER = "" +
            "uniform sampler2D inputImageTexture;\n" +
            " \n" +
            " const lowp int GAUSSIAN_SAMPLES = 9;\n" +
            " \n" +
            " varying highp vec2 textureCoordinate;\n" +
            " varying highp vec2 blurCoordinates[GAUSSIAN_SAMPLES];\n" +
            " \n" +
            " uniform mediump float distanceNormalizationFactor;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     lowp vec4 centralColor;\n" +
            "     lowp float gaussianWeightTotal;\n" +
            "     lowp vec4 sum;\n" +
            "     lowp vec4 sampleColor;\n" +
            "     lowp float distanceFromCentralColor;\n" +
            "     lowp float gaussianWeight;\n" +
            "     \n" +
            "     \n" +
            "     lowp float colorparam = 40000.0;\n" +
            "     lowp float colordistance;\n" +
            "     lowp float colorweight;\n" +
            "     \n" +
            "     /////////////////////////9 spacesigma=5////////////////////\n" +
            "     \n" +
            "     centralColor = texture2D(inputImageTexture, blurCoordinates[4]);\n" +
            "     gaussianWeightTotal = 0.188493;\n" +
            "     sum = centralColor * 0.188493;\n" +
            "     \n" +
            "     sampleColor = texture2D(inputImageTexture, blurCoordinates[0]);\n" +
            "     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
            "     gaussianWeight = 0.017261 * (1.0 - distanceFromCentralColor);\n" +
            "     gaussianWeightTotal += gaussianWeight;\n" +
            "     sum += sampleColor * gaussianWeight;\n" +
            "     \n" +
            "     sampleColor = texture2D(inputImageTexture, blurCoordinates[1]);\n" +
            "     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
            "     gaussianWeight = 0.095437 * (1.0 - distanceFromCentralColor);\n" +
            "     gaussianWeightTotal += gaussianWeight;\n" +
            "     sum += sampleColor * gaussianWeight;\n" +
            "     \n" +
            "     sampleColor = texture2D(inputImageTexture, blurCoordinates[2]);\n" +
            "     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
            "     gaussianWeight = 0.142134 * (1.0 - distanceFromCentralColor);\n" +
            "     gaussianWeightTotal += gaussianWeight;\n" +
            "     sum += sampleColor * gaussianWeight;\n" +
            "     \n" +
            "     sampleColor = texture2D(inputImageTexture, blurCoordinates[3]);\n" +
            "     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
            "     gaussianWeight = 0.150921 * (1.0 - distanceFromCentralColor);\n" +
            "     gaussianWeightTotal += gaussianWeight;\n" +
            "     sum += sampleColor * gaussianWeight;\n" +
            "     \n" +
            "     sampleColor = texture2D(inputImageTexture, blurCoordinates[5]);\n" +
            "     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
            "     gaussianWeight = 0.150921 * (1.0 - distanceFromCentralColor);\n" +
            "     gaussianWeightTotal += gaussianWeight;\n" +
            "     sum += sampleColor * gaussianWeight;\n" +
            "     \n" +
            "     sampleColor = texture2D(inputImageTexture, blurCoordinates[6]);\n" +
            "     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
            "     gaussianWeight = 0.142134 * (1.0 - distanceFromCentralColor);\n" +
            "     gaussianWeightTotal += gaussianWeight;\n" +
            "     sum += sampleColor * gaussianWeight;\n" +
            "     \n" +
            "     sampleColor = texture2D(inputImageTexture, blurCoordinates[7]);\n" +
            "     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
            "     gaussianWeight = 0.095437 * (1.0 - distanceFromCentralColor);\n" +
            "     gaussianWeightTotal += gaussianWeight;\n" +
            "     sum += sampleColor * gaussianWeight;\n" +
            "     \n" +
            "     sampleColor = texture2D(inputImageTexture, blurCoordinates[8]);\n" +
            "     distanceFromCentralColor = min(distance(centralColor, sampleColor) * distanceNormalizationFactor, 1.0);\n" +
            "     gaussianWeight = 0.017261 * (1.0 - distanceFromCentralColor);\n" +
            "     gaussianWeightTotal += gaussianWeight;\n" +
            "     sum += sampleColor * gaussianWeight;\n" +
            "     \n" +
            "     gl_FragColor = sum / gaussianWeightTotal;\n" +
            "     \n" +
            " }";

    private int mDistanceLocation;

    private int mTexelWidthOffsetLocation;

    private int mTexelHeightOffsetLocation;

    private float mDistance;

    private float mTexelWidthOffset;

    private float mTexelHeightOffset;

    public GPUImageBeautifyFilter(float level) {
        super(BEAUTY_VERTEX_SHADER, BEAUTY_FRAGMENT_SHADER);
        mDistance = level;
    }

    @Override
    public void onInit() {
        super.onInit();
        mDistanceLocation = GLES20.glGetUniformLocation(getProgram(), "distanceNormalizationFactor");
        mTexelWidthOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "texelWidthOffset");
        mTexelHeightOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "texelHeightOffset");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setDistance(mDistance);
        setTexelWidthOffset(mTexelWidthOffset);
        setTexelHeightOffset(mTexelHeightOffset);
    }

    @Override
    public void onOutputSizeChanged(final int width, final int height) {
        super.onOutputSizeChanged(width, height);
        setTexelWidthOffset(2.f / width);
        setTexelHeightOffset(2.f / height);
    }

    public void setDistance(float distance) {
        mDistance = distance;
        setFloat(mDistanceLocation, mDistance);
    }

    public void setTexelWidthOffset(float widthOffset) {
        this.mTexelWidthOffset = widthOffset;
        setFloat(mTexelWidthOffsetLocation, widthOffset);
    }

    public void setTexelHeightOffset(float heightOffset) {
        this.mTexelHeightOffset = heightOffset;
        setFloat(mTexelHeightOffsetLocation, heightOffset);
    }
}
