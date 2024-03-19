package com.example.plantzone;

import android.content.Context;
import android.content.res.AssetManager;
import org.tensorflow.lite.Interpreter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

public class TensorFlowLiteModelLoader {

    private final Context context;

    public TensorFlowLiteModelLoader(Context context) {
        this.context = context;
    }

    public Interpreter loadModel(String modelFilename) throws IOException {
        ByteBuffer tfliteModel = loadModelFile(modelFilename);
        Interpreter.Options options = new Interpreter.Options();
        return new Interpreter(tfliteModel, options);
    }

    public List<String> loadLabelList(String labelFilename) throws IOException {
        List<String> labelList = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(labelFilename);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                labelList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return labelList;
    }

    private ByteBuffer loadModelFile(String modelFilename) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(modelFilename);
        int modelFileSize = inputStream.available();
        ByteBuffer buffer = ByteBuffer.allocateDirect(modelFileSize);
        try (ReadableByteChannel channel = Channels.newChannel(inputStream)) {
            while (channel.read(buffer) > 0) {
                // Do nothing, read data into buffer
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer.flip();
        return buffer;
    }
}
