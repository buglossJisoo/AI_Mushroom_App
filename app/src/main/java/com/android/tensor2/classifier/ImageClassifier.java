package com.android.tensor2.classifier;


import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.image.ops.Rot90Op;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Tensor flow 모델을 이용한 이미지 분류 class
 */
public class ImageClassifier {

    /**
     * Quantized MobileNet models requires additional dequantization to the output probability.
     */
    private static final float PROBABILITY_MEAN = 0.0f;
    private static final float PROBABILITY_STD = 255.0f;

    /**
     * The quantized model does not require normalization, thus set mean as 0.0f, and std as 1.0f to
     * bypass the normalization.
     */
    private static final float IMAGE_STD = 1.0f;
    private static final float IMAGE_MEAN = 0.0f;

    /*
    *  리스트뷰에 몇개 뜨게 할건지 정하는 변수
    */
    private static final int MAX_SIZE = 1;

    /**
     * image size width
     */
    private final int imageResizeX;
    /**
     * image size height
     */
    private final int imageResizeY;

    /**
     *  label.txt 리스트
     */
    private final List<String> labels;

    /**
     * Tensorflow lite 모델을 해석할 Interpreter
     */
    private final Interpreter tensorClassifier;
    /**
     * Input image TensorBuffer.
     */
    private TensorImage inputImageBuffer;
    /**
     * Output probability TensorBuffer.
     */
    private final TensorBuffer probabilityImageBuffer;
    /**
     * Processer to apply post processing of the output probability.
     */
    private final TensorProcessor probabilityProcessor;

    /**
     * Creates a classifier
     *
     * @param activity the current activity
     * @throws IOException
     */
    public ImageClassifier(Activity activity) throws IOException {
        /*
         *  이미지 분류 TensorFlow Lite 모델 불러오기
         */
        MappedByteBuffer classifierModel = FileUtil.loadMappedFile(activity,
                "mobilenetmodel_quant_10_ephocs50.tflite");
        // labels.txt 파일 불러오기
        labels = FileUtil.loadLabels(activity, "labels_10.txt");

        // TensorFlow Lite 모델 파일 로딩
        tensorClassifier = new Interpreter(classifierModel, null);

        // Reads type and shape of input and output tensors, respectively. [START]
        int imageTensorIndex = 0; // input
        int probabilityTensorIndex = 0;// output

        // input shape ex> [1][224][224][3]
        int[] inputImageShape = tensorClassifier.getInputTensor(imageTensorIndex).shape();
        // input의 data type
        DataType inputDataType = tensorClassifier.getInputTensor(imageTensorIndex).dataType();

        // output shape ex> (1, 10)
        int[] outputImageShape = tensorClassifier.getOutputTensor(probabilityTensorIndex).shape();
        // output의 data type
        DataType outputDataType = tensorClassifier.getOutputTensor(probabilityTensorIndex).dataType();

        // input shape width 224 size
        imageResizeX = inputImageShape[1];
        // input shape height 224 size
        imageResizeY = inputImageShape[2];
        // Reads type and shape of input and output tensors, respectively. [END]


        // Creates the input tensor.(선택된 image --> input tensor )
        inputImageBuffer = new TensorImage(inputDataType);

        // Creates the output tensor and its processor. (image를 가지고 확률을 output)
        probabilityImageBuffer = TensorBuffer.createFixedSize(outputImageShape, outputDataType);

        // Creates the post processor for the output probability.
        probabilityProcessor = new TensorProcessor.Builder().add(new NormalizeOp(PROBABILITY_MEAN, PROBABILITY_STD))
                .build();
    }

    /**
     * method runs the inference and returns the classification results
     *
     * @param bitmap            the bitmap of the image
     * @param sensorOrientation orientation of the camera
     * @return classification results
     */
    public List<Recognition> recognizeImage(final Bitmap bitmap, final int sensorOrientation) {
        List<Recognition> recognitions = new ArrayList<>();

        inputImageBuffer = loadImage(bitmap, sensorOrientation);
        tensorClassifier.run(inputImageBuffer.getBuffer(), probabilityImageBuffer.getBuffer().rewind());

        // Gets the map of label and probability.
        Map<String, Float> labelledProbability = new TensorLabel(labels,
                probabilityProcessor.process(probabilityImageBuffer)).getMapWithFloatValue();

        for (Map.Entry<String, Float> entry : labelledProbability.entrySet()) {
            recognitions.add(new Recognition(entry.getKey(), entry.getValue()));
        }

        // Find the best classifications by sorting predicitons based on confidence
        Collections.sort(recognitions);
        // returning top 5 predicitons
        return recognitions.subList(0, MAX_SIZE);
    }

    /**
     * loads the image into tensor input buffer and apply pre processing steps
     *
     * @param bitmap            the bit map to be loaded
     * @param sensorOrientation the sensor orientation
     * @return the image loaded tensor input buffer
     */
    private TensorImage loadImage(Bitmap bitmap, int sensorOrientation) {
        // TensorImage를 bitmap으로 load
        inputImageBuffer.load(bitmap);

        int noOfRotations = sensorOrientation / 90;
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());

        // Creates processor for the TensorImage.
        // pre processing steps are applied here
        ImageProcessor imageProcessor = new ImageProcessor.Builder()
                .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                .add(new ResizeOp(imageResizeX, imageResizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                .add(new Rot90Op(noOfRotations))
                .add(new NormalizeOp(IMAGE_MEAN, IMAGE_STD))
                .build();
        return imageProcessor.process(inputImageBuffer);
    }

    /**
     * An immutable result returned by a Classifier describing what was recognized.
     */
    public class Recognition implements Comparable {
        /**
         * Display name for the recognition.
         */
        private String name;
        /**
         * A sortable score for how good the recognition is relative to others. Higher should be better.
         */
        private float confidence;

        public Recognition() {
        }

        public Recognition(String name, float confidence) {
            this.name = name;
            this.confidence = confidence;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getConfidence() {
            return confidence;
        }

        public void setConfidence(float confidence) {
            this.confidence = confidence;
        }

        @Override
        public String toString() {
            return "Recognition{" +
                    "name='" + name + '\'' +
                    ", confidence=" + confidence+
                    '}';
        }

        @Override
        public int compareTo(Object o) {
            return Float.compare(((Recognition) o).confidence, this.confidence);
        }
    }
}
