package com.example.collagecreator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.os.Bundle;
import android.widget.SeekBar;

import com.mukesh.imageproccessing.OnProcessingCompletionListener;
import com.mukesh.imageproccessing.PhotoFilter;
import com.mukesh.imageproccessing.filters.AutoFix;

import java.io.IOException;

public class MainActivity extends Activity {
    BottomNavigationView botnav;
    PaintView pview;
    SeekBar seekbar;
    PhotoFilter photoFilter;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pview = findViewById(R.id.paintView);
        botnav = findViewById(R.id.bottom_navigation);
        LinearGradient test = new LinearGradient(0.f, 0.f, 680.f, 0.0f,
                new int[] { 0xFF000000, 0xFF0000FF, 0xFF00FF00, 0xFF00FFFF,
                        0xFFFF0000, 0xFFFF00FF, 0xFFFFFF00, 0xFFFFFFFF},
                null, Shader.TileMode.CLAMP);
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setShader(test);
        seekbar = findViewById(R.id.paintselect);
        seekbar.setProgressDrawable(shape);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blank);
        pview.init(bitmap);
        botnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add:
                        Intent photointent = new Intent(Intent.ACTION_PICK);
                        photointent.setType("image/*");
                        startActivityForResult(photointent, IMAGE_GALLERY_REQUEST);
                        break;
                    case R.id.action_draw:
                        pview.setBrushColor();
                        break;
                    case R.id.action_filters:

                        break;
                }
                return true;
            }
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                pview.clear();
                pview.init(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        }
    }

