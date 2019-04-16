/*
Choosing the frame for the collage, Shows all the frames
 */

package com.example.photocollage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class CollageChooser extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage_chooser);
        onContentChanged();

        // imaveView1.setListener(listener)
        // imageView2.setListener(lisneter)
    }

    public void onContentChanged() {
        TableLayout frameLayout = findViewById(R.id.table_layout);
        int touchable = frameLayout.getChildCount();
        Log.i("THIS","IT GOT HERE");
        Log.i("HEIGHT",Integer.toString(touchable));

        /*for (View v : touchable) {
            if (v instanceof ImageView) {
                Log.i("THIS","IT GOT HERE ALSO");

                Toast.makeText(getApplicationContext(),"it worked", Toast.LENGTH_LONG).show();
                v.setOnClickListener(this);
            }
        }*/
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(),"This is ImageView " +v.getTag(), Toast.LENGTH_LONG).show();
    }

    /*private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // check tag
            Toast.makeText(getApplicationContext(),"This is ImageView " +v.getTag(), Toast.LENGTH_LONG).show();
            // do something depending on tag
        }
    };*/
}
