package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;

public class MainActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void runHuffman(View view) throws IOException {
        EditText edit_text_input = (EditText)findViewById(R.id.edit_text_input);
        String input = edit_text_input.getText().toString();

        HuffmanCoder huffmanCoder = new HuffmanCoder(input);

        TextView text_view_output = (TextView)findViewById(R.id.text_view_output);
        text_view_output.setText(huffmanCoder.getResult());

        File outputFile = new File(this.getFilesDir(), "output.txt");
        outputFile.createNewFile();

        FileWriter fileWriter = new FileWriter(outputFile);
        fileWriter.write(huffmanCoder.getCodes().toString() + "\n");
        fileWriter.write(huffmanCoder.getResult());
        fileWriter.close();

        int bitmapWidth = 1024, bitmapHeight = 512, bitmapBorder = bitmapWidth / 20, textBorder = bitmapBorder * 2, lineWidth = 1;

        Bitmap histogram = Bitmap.createBitmap(bitmapWidth,bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(histogram);
        Paint paint = new Paint();
        paint.setTextSize(64);

        int i = 0, maxFrequency = Collections.max(huffmanCoder.getFrequencies().values()), alphabetSize = huffmanCoder.getFrequencies().size(), columnWidth = (bitmapWidth - 2 * bitmapBorder) / alphabetSize;

        for (Map.Entry<Character,Integer> entry : huffmanCoder.getFrequencies().entrySet())
        {
            paint.setColor(Color.BLACK);
            canvas.drawText(entry.getKey().toString(), bitmapBorder + i * columnWidth, bitmapHeight - bitmapBorder, paint);
            canvas.drawRect(bitmapBorder + i * columnWidth, bitmapBorder + (bitmapHeight - textBorder - bitmapBorder) * (maxFrequency - entry.getValue()) / maxFrequency, bitmapBorder + (i + 1) * columnWidth, bitmapHeight - textBorder, paint);
            paint.setColor(Color.GREEN);
            canvas.drawRect(bitmapBorder + i * columnWidth + lineWidth, bitmapBorder + (bitmapHeight - textBorder - bitmapBorder) * (maxFrequency - entry.getValue()) / maxFrequency + lineWidth, bitmapBorder + (i + 1) * columnWidth - lineWidth, bitmapHeight - textBorder - lineWidth, paint);
            ++i;
        }

        ImageView image_view_histogram = (ImageView)findViewById(R.id.image_view_histogram);
        image_view_histogram.setImageBitmap(histogram);
    }
}