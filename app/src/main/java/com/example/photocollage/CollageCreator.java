package com.example.photocollage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uvstudio.him.photofilterlibrary.PhotoFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.makeText;

// MAKE BAR FONT WHITE
public class CollageCreator extends Activity {
    BottomNavigationView botnav;
    PaintView pview;
    SeekBar seekbar;
    SeekBar seekbar2;
    int width;
    int picturecounter = 0;
    PhotoFilter photoFilter;
    TableLayout table;
    int link;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    public static final int CAMERA_REQUEST = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage_creator);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
        pview = findViewById(R.id.paintView);
        botnav = findViewById(R.id.bottom_navigation);

        link = getIntent().getIntExtra("image",-1);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), link);
        pview.init(bitmap);
        pview.setBitmap(bitmap);

        //seekbar stuff
        seekbar = findViewById(R.id.paintselect);
        seekbar.setVisibility(View.INVISIBLE);
        seekbar.setMax(256*7-1);
        seekbar.setProgress(0);

        //seekbar2 stuff
        seekbar2 = findViewById(R.id.thicknessselect);
        seekbar2.setVisibility(View.INVISIBLE);
        seekbar2.setMax(256*7-1);
        seekbar2.setProgress(0);

        //tablelayout for filters stuff
        table = findViewById(R.id.table_layout);


        botnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_camera:
                        pview.resetBrush();
                        if (seekbar.getVisibility()==View.VISIBLE) {
                            seekbar.setVisibility(View.INVISIBLE);
                            seekbar2.setVisibility(View.INVISIBLE);
                        }
                        if (table.getVisibility()==View.VISIBLE) {
                            table.setVisibility(View.INVISIBLE);
                        }
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        break;
                    case R.id.action_add:
                        pview.resetBrush();
                        if (seekbar.getVisibility()!= View.VISIBLE) {
                            seekbar.setVisibility(View.INVISIBLE);
                            seekbar2.setVisibility(View.INVISIBLE);
                        }
                        if (table.getVisibility()==View.VISIBLE) {
                            table.setVisibility(View.INVISIBLE);
                        }
                        Intent photointent = new Intent(Intent.ACTION_PICK);
                        photointent.setType("image/*");
                        startActivityForResult(photointent, IMAGE_GALLERY_REQUEST);
                        break;
                    case R.id.action_draw:
                        if (table.getVisibility()==View.VISIBLE) {
                            table.setVisibility(View.INVISIBLE);
                        }
                        seekbar.setVisibility(View.VISIBLE);
                        pview.setBrushColor(0,0,0);
                        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if (fromUser) {
                                    int r = 0;
                                    int g = 0;
                                    int b = 0;

                                    if (progress < 256) {
                                        b = progress;
                                    } else if (progress < 256 * 2) {
                                        g = progress % 256;
                                        b = 256 - progress % 256;
                                    } else if (progress < 256 * 3) {
                                        g = 255;
                                        b = progress % 256;
                                    } else if (progress < 256 * 4) {
                                        r = progress % 256;
                                        g = 256 - progress % 256;
                                        b = 256 - progress % 256;
                                    } else if (progress < 256 * 5) {
                                        r = 255;
                                        g = 0;
                                        b = progress % 256;
                                    } else if (progress < 256 * 6) {
                                        r = 255;
                                        g = progress % 256;
                                        b = 256 - progress % 256;
                                    } else if (progress < 256 * 7) {
                                        r = 255;
                                        g = 255;
                                        b = progress % 256;
                                    }

                                    pview.setBrushColor(r,g,b);
                                }
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });

                        seekbar2.setVisibility(View.VISIBLE);

                        seekbar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if (fromUser) {
                                    int bsize = 1;
                                    if (progress < 256) {
                                        bsize = 10;
                                    } else if (progress < 256 * 2) {
                                        bsize = 20;
                                    } else if (progress < 256 * 3) {
                                        bsize = 30;
                                    } else if (progress < 256 * 4) {
                                        bsize = 40;
                                    } else if (progress < 256 * 5) {
                                        bsize = 50;
                                    } else if (progress < 256 * 6) {
                                        bsize = 60;
                                    } else if (progress < 256 * 7) {
                                        bsize = 70;
                                    }

                                    pview.setBrushThickness(bsize);
                                }
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                        break;
                    case R.id.action_filters:
                        pview.resetBrush();
                        if (seekbar.getVisibility()==View.VISIBLE) {
                            seekbar.setVisibility(View.INVISIBLE);
                            seekbar2.setVisibility(View.INVISIBLE);
                        }
                        table.setVisibility(View.VISIBLE);
                        photoFilter = new PhotoFilter ();
                        for (int i = 0; i < table.getChildCount(); i++) {
                            TableRow row = (TableRow) table.getChildAt(i);
                            for (int j = 0; j < row.getChildCount(); j++) {
                                ImageView iv = (ImageView) row.getChildAt(j);
                                iv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (v.getId() == R.id.frame1)
                                            pview.setBitmap(photoFilter.five(getApplicationContext(), pview.getBitmap()));
                                        else if (v.getId() == R.id.frame2)
                                            pview.setBitmap(photoFilter.six(getApplicationContext(), pview.getBitmap()));
                                        else if (v.getId() == R.id.frame3)
                                            pview.setBitmap(photoFilter.seven(getApplicationContext(), pview.getBitmap()));
                                        else if (v.getId() == R.id.frame4)
                                            pview.setBitmap(photoFilter.eight(getApplicationContext(), pview.getBitmap()));
                                        else if (v.getId() == R.id.frame5)
                                            pview.setBitmap(photoFilter.two(getApplicationContext(), pview.getBitmap()));
                                        else if (v.getId() == R.id.frame6)
                                            pview.setBitmap(photoFilter.ten(getApplicationContext(), pview.getBitmap()));
                                        else if (v.getId() == R.id.frame7)
                                            pview.setBitmap(photoFilter.twelve(getApplicationContext(), pview.getBitmap()));
                                        else
                                            pview.setBitmap(photoFilter.sixteen(getApplicationContext(), pview.getBitmap()));
                                    }
                                });
                            }
                        }
                        break;
                    case R.id.action_save:
                        pview.resetBrush();
                        if (seekbar.getVisibility()==View.VISIBLE) {
                            seekbar.setVisibility(View.INVISIBLE);
                            seekbar2.setVisibility(View.INVISIBLE);
                        }
                        if (table.getVisibility()==View.VISIBLE) {
                            table.setVisibility(View.INVISIBLE);
                        }
                        Bitmap bm = pview.getBitmap();
                        storeImage(bm);
                        Toast toast = Toast.makeText(getApplicationContext(),"Your collage was successfully saved!", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 825);
                        toast.show();
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
                if (link == R.drawable.bmp1)
                    pview.frameinit(bitmap,47, 47, width - 50, width - 50);
                else if (link == R.drawable.bmp2) {
                    if (picturecounter == 0) {
                        picturecounter++;
                        pview.frameinit(bitmap, 47, 47, width - 50, width/2 - 25);
                    }
                    else {
                        picturecounter = 0;
                        pview.frameinit(bitmap, 47, width / 2 + 23, width - 50, width - 50);
                    }
                }
                else if (link == R.drawable.bmp3) {
                    if (picturecounter == 0){
                        picturecounter++;
                        pview.frameinit(bitmap, 47, 47, width/2 - 25, width - 50);
                    }
                    else {
                        picturecounter =0;
                        pview.frameinit(bitmap, width / 2 + 23, 47, width - 50, width - 50);
                    }
                }
                else if (link == R.drawable.bmp4)
                {
                    if (picturecounter == 0){
                        picturecounter++;
                        pview.frameinit(bitmap, 47, 47, width/2 - 25, width/2 - 25);
                    }
                    else if (picturecounter == 1){
                        picturecounter++;
                        pview.frameinit(bitmap, width/2 + 23, 47, width - 50, width/2 - 25);
                    }
                    else if (picturecounter == 2)
                    {
                        picturecounter++;
                        pview.frameinit(bitmap, 47, width/2 + 23, width/2 - 25, width - 50);
                    }
                    else if(picturecounter == 3)
                    {
                        picturecounter=0;
                        pview.frameinit(bitmap, width/2 + 23, width/2 + 23, width - 50, width - 50);
                    }
                }
                else if (link == R.drawable.bmp5) {
                    if (picturecounter == 0) {
                        picturecounter++;
                        pview.frameinit(bitmap, 47, 47, width / 2 - 25, width / 2 - 25);
                    } else if (picturecounter == 1) {
                        picturecounter++;
                        pview.frameinit(bitmap, width / 2 + 23, 47, width - 50, width / 2 - 25);
                    }
                    else if (picturecounter == 2) {
                        picturecounter=0;
                        pview.frameinit(bitmap, 47 , width/2 + 23, width - 50, width - 50);
                    }
                }
                else if (link == R.drawable.bmp6){
                    if (picturecounter == 0){
                        picturecounter++;
                        pview.frameinit(bitmap, 47, 47, width - 50, width/2 - 25);
                    }
                    else if (picturecounter == 1)
                    {
                        picturecounter++;
                        pview.frameinit(bitmap, 47, width/2 + 23, width/2 - 25, width - 50);
                    }
                    else if(picturecounter == 2)
                    {
                        picturecounter=0;
                        pview.frameinit(bitmap, width/2 + 23, width/2 + 23, width - 50, width - 50);
                    }
                }
                else if (link == R.drawable.bmp7){
                    if (picturecounter == 0){
                        picturecounter++;
                        pview.frameinit(bitmap, 47, 47, width/2 - 25, width/2 - 25);
                    }
                    else if (picturecounter == 1) {
                        picturecounter++;
                        pview.frameinit(bitmap, width / 2 + 23, 47, width - 50, width - 50);
                    }
                    else if (picturecounter == 2)
                    {
                        picturecounter=0;
                        pview.frameinit(bitmap, 47, width/2 + 23, width/2 - 25, width - 50);
                    }
                }
                else if (link == R.drawable.bmp8){
                    if (picturecounter == 0){
                        picturecounter++;
                        pview.frameinit(bitmap, 47, 47, width/2 - 25, width - 50);
                    }
                    else if (picturecounter == 1){
                        picturecounter++;
                        pview.frameinit(bitmap, width/2 + 23, 47, width - 50, width/2 - 25);
                    }
                    else if(picturecounter == 2)
                    {
                        picturecounter=0;
                        pview.frameinit(bitmap, width/2 + 23, width/2 + 23, width - 50, width - 50);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if (link == R.drawable.bmp1)
                pview.frameinit(bitmap,47, 47, width - 50, width - 50);
            else if (link == R.drawable.bmp2) {
                if (picturecounter == 0) {
                    picturecounter++;
                    pview.frameinit(bitmap, 47, 47, width - 50, width/2 - 25);
                }
                else {
                    picturecounter = 0;
                    pview.frameinit(bitmap, 47, width / 2 + 23, width - 50, width - 50);
                }
            }
            else if (link == R.drawable.bmp3) {
                if (picturecounter == 0){
                    picturecounter++;
                    pview.frameinit(bitmap, 47, 47, width/2 - 25, width - 50);
                }
                else {
                    picturecounter=0;
                    pview.frameinit(bitmap, width / 2 + 23, 50, width - 50, width - 50);
                }
            }
            else if (link == R.drawable.bmp4)
            {
                if (picturecounter == 0){
                    picturecounter++;
                    pview.frameinit(bitmap, 47, 47, width/2 - 25, width/2 - 25);
                }
                else if (picturecounter == 1){
                    picturecounter++;
                    pview.frameinit(bitmap, width/2 + 23, 47, width - 50, width/2 - 25);
                }
                else if (picturecounter == 2)
                {
                    picturecounter++;
                    pview.frameinit(bitmap, 47, width/2 + 23, width/2 - 25, width - 50);
                }
                else if(picturecounter == 3)
                {
                    picturecounter=0;
                    pview.frameinit(bitmap, width/2 + 23, width/2 + 23, width - 50, width - 50);
                }
            }
            else if (link == R.drawable.bmp5) {
                if (picturecounter == 0) {
                    picturecounter++;
                    pview.frameinit(bitmap, 47, 47, width / 2 - 25, width / 2 - 25);
                } else if (picturecounter == 1) {
                    picturecounter++;
                    pview.frameinit(bitmap, width / 2 + 23, 47, width - 50, width / 2 - 25);
                }
                else if (picturecounter == 2) {
                    picturecounter=0;
                    pview.frameinit(bitmap, 47 , width/2 + 23, width - 50, width - 50);
                }
            }
            else if (link == R.drawable.bmp6){
                if (picturecounter == 0){
                    picturecounter++;
                    pview.frameinit(bitmap, 47, 47, width - 50, width/2 - 25);
                }
                else if (picturecounter == 1)
                {
                    picturecounter++;
                    pview.frameinit(bitmap, 47, width/2 + 23, width/2 - 25, width - 50);
                }
                else if(picturecounter == 2)
                {
                    picturecounter=0;
                    pview.frameinit(bitmap, width/2 + 23, width/2 + 23, width - 50, width - 50);
                }
            }
            else if (link == R.drawable.bmp7){
                if (picturecounter == 0){
                    picturecounter++;
                    pview.frameinit(bitmap, 47, 47, width/2 - 25, width/2 - 25);
                }
                else if (picturecounter == 1) {
                    picturecounter++;
                    pview.frameinit(bitmap, width / 2 + 23, 47, width - 50, width - 50);
                }
                else if (picturecounter == 2)
                {
                    picturecounter=0;
                    pview.frameinit(bitmap, 47, width/2 + 23, width/2 - 25, width - 50);
                }
            }
            else if (link == R.drawable.bmp8){
                if (picturecounter == 0){
                    picturecounter++;
                    pview.frameinit(bitmap, 47, 47, width/2 - 25, width - 50);
                }
                else if (picturecounter == 1){
                    picturecounter++;
                    pview.frameinit(bitmap, width/2 + 23, 47, width - 50, width/2 - 25);
                }
                else if(picturecounter == 2)
                {
                    picturecounter=0;
                    pview.frameinit(bitmap, width/2 + 23, width/2 + 23, width - 50, width - 50);
                }
            }
        }

    }


    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
}

