/*
The opening screen
 */

package com.example.photocollage;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openCollageChooser();
            }
        }, 2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },2000);
    }
    

    public void openCollageChooser() {
        Intent intent = new Intent(this, CollageChooser.class);
        startActivity(intent);
    }
}
