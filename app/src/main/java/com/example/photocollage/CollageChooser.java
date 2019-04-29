/*
Choosing the frame for the collage, Shows all the frames
 */

package com.example.photocollage;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CollageChooser extends AppCompatActivity {

    TableLayout frameLayout;
    //ImageView firstFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage_chooser);
        frameLayout = findViewById(R.id.table_layout);
        for (int i = 0; i < frameLayout.getChildCount(); i++) {
            TableRow row = (TableRow) frameLayout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                ImageView iv = (ImageView) row.getChildAt(j);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Each of the cells has a tag so frame one has frame1, frame two has frame2
                        // and so on. I put tag so you know which jpg frame to grab when
                        // you click on the cell. They go left to right.
                        //Toast.makeText(getApplicationContext(), "This the Toast and Cell got touched " + v.getTag(), Toast.LENGTH_SHORT).show();
                        //Intent intent = new Intent(CollageChooser.this, ChangeSizeExampleActivity.class);
                        Intent intent = new Intent(CollageChooser.this, CollageCreator.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }

        /*for (View v : touchable) {
            if (v instanceof ImageView) {
                Log.i("THIS","IT GOT HERE ALSO");

                Toast.makeText(getApplicationContext(),"it worked", Toast.LENGTH_LONG).show();
                v.setOnClickListener(this);
            }
        }*/
    private View.OnClickListener tablerowOnClickListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            //Highlight selected row
            for (int i = 0; i < frameLayout.getChildCount(); i++) {
                View row = frameLayout.getChildAt(i);
                if (row == v) {
                    Toast.makeText(getApplicationContext(), "This is ImageView " + v.getTag(), Toast.LENGTH_LONG).show();
                } else {
                    //Change this to your normal background color.
                    Toast.makeText(getApplicationContext(), "This is ImageView " + v.getTag(), Toast.LENGTH_LONG).show();
                }
            }
        }
    };
}
