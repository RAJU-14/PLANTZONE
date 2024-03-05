// TensorFlowLiteModelLoader.java

package com.example.plantzone;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import org.tensorflow.lite.Interpreter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class TensorFlowLiteModelLoader {

    private Context context;

    public TensorFlowLiteModelLoader(Context context) {
        this.context = context;
    }

    public Interpreter loadModel(String modelFilename) throws IOException {
        MappedByteBuffer tfliteModel = loadModelFile(modelFilename);
        Interpreter.Options options = new Interpreter.Options();
        return new Interpreter(tfliteModel, options);
    }

    private MappedByteBuffer loadModelFile(String modelFilename) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(modelFilename);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}
