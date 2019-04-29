package com.example.photocollage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.photocollage.PhotoFilter;

import java.io.IOException;

public class CollageCreator extends Activity {
    BottomNavigationView botnav;
    PaintView pview;
    SeekBar seekbar;
    PhotoFilter photoFilter;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage_creator);
        pview = findViewById(R.id.paintView);
        botnav = findViewById(R.id.bottom_navigation);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blank);
        pview.init(bitmap);
        botnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_camera:
                        Intent camera = new Intent(CollageCreator.this, AccessCamera.class);
                        startActivity(camera);
                        break;
                    case R.id.action_add:
                        Intent photointent = new Intent(Intent.ACTION_PICK);
                        photointent.setType("image/*");
                        startActivityForResult(photointent, IMAGE_GALLERY_REQUEST);
                        break;
                    case R.id.action_draw:
                        LinearGradient test = new LinearGradient(0.f, 0.f, 680.f, 0.0f,
                                new int[] { 0xFF000000, 0xFF0000FF, 0xFF00FF00, 0xFF00FFFF,
                                        0xFFFF0000, 0xFFFF00FF, 0xFFFFFF00, 0xFFFFFFFF},
                                null, Shader.TileMode.CLAMP);
                        ShapeDrawable shape = new ShapeDrawable(new RectShape());
                        shape.getPaint().setShader(test);
                        seekbar = findViewById(R.id.paintselect);
                        seekbar.setProgressDrawable(shape);
                        pview.setBrushColor();
                        shape.getPaint().setShader(null);
                        break;
                    case R.id.action_filters:

                        break;
                    case R.id.action_rotate:
                        Intent rotate = new Intent(CollageCreator.this, ChangeSizeExampleActivity.class);
                        startActivity(rotate);
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

