package com.example.photocollage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import java.io.FileOutputStream;

public class AccessCamera extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 0);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bm = (Bitmap) data.getExtras().get("data");

        String fileName = "image";
        try {
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fo);
            fo.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }
}