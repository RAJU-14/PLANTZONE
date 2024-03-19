package com.example.plantzone;
import org.tensorflow.lite.Interpreter;

import android.graphics.Bitmap;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Classifier {

    private final Interpreter interpreter;
    private final int inputSize;
    private final List<String> labels;

    private static final int PIXEL_SIZE = 3;
    private static final int IMAGE_MEAN = 0;
    private static final float IMAGE_STD = 255.0f;
    private static final int MAX_RESULTS = 3;
    private static final float THRESHOLD = 0.4f;
    private static final int OUTPUT_SIZE = 4;

    public Classifier(Interpreter interpreter, int inputSize, List<String> labels) {
        this.interpreter = interpreter;
        this.inputSize = inputSize;
        this.labels = labels;
    }

    public List<Recognition> recognizeImage(Bitmap bitmap) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, false);
        ByteBuffer byteBuffer = convertBitmapToByteBuffer(scaledBitmap);
        float[][] result = new float[1][OUTPUT_SIZE];
        interpreter.run(byteBuffer, result);
        return getSortedResult(result);
    }

    private List<Recognition> getSortedResult(float[][] labelProbArray) {
        PriorityQueue<Recognition> pq =
                new PriorityQueue<>(
                        MAX_RESULTS,
                        new Comparator<Recognition>() {
                            @Override
                            public int compare(Recognition lhs, Recognition rhs) {
                                return Float.compare(rhs.getConfidence(), lhs.getConfidence());
                            }
                        });

        for (int i = 0; i < labelProbArray[0].length; ++i) {
            float confidence = labelProbArray[0][i];
            if (confidence > THRESHOLD) {
                String label = labels.get(i);
                pq.add(new Recognition(label, confidence));
            }
        }

        final ArrayList<Recognition> recognitions = new ArrayList<>();
        int recognitionsSize = Math.min(pq.size(), MAX_RESULTS);
        for (int i = 0; i < recognitionsSize; ++i) {
            recognitions.add(pq.poll());
        }
        return recognitions;
    }

    public static class Recognition {
        private final String title;
        private final float confidence;

        public Recognition(String title, float confidence) {
            this.title = title;
            this.confidence = confidence;
        }

        public String getTitle() {
            return title;
        }

        public float getConfidence() {
            return confidence;
        }
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * PIXEL_SIZE);
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues = new int[inputSize * inputSize];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int pixel = 0;
        for (int i = 0; i < inputSize; ++i) {
            for (int j = 0; j < inputSize; ++j) {
                int value = intValues[pixel++];
                byteBuffer.putFloat((((value >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                byteBuffer.putFloat((((value >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                byteBuffer.putFloat(((value & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
            }
        }
        return byteBuffer;
    }
}
