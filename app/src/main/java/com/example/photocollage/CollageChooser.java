/*
Choosing the frame for the collage, Shows all the frames
 */

package com.example.photocollage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class CollageChooser extends AppCompatActivity {

    TableLayout frameLayout;
    TextView tview;
    //ImageView firstFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage_chooser);
        frameLayout = findViewById(R.id.table_layout);
        tview = findViewById(R.id.collage_text);
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
                        //makeText(getApplicationContext(), "This the Toast and Cell got touched " + v.getTag(), Toast.LENGTH_SHORT).show();
                        //Intent intent = new Intent(CollageChooser.this, CenterRotate.class);
                        //String fileName = "bmp1";
                        int link;
                        if (v.getId() == R.id.frame1)
                            link = R.drawable.bmp1;
                        else if (v.getId() == R.id.frame2)
                            link = R.drawable.bmp2;
                        else if (v.getId() == R.id.frame3)
                            link = R.drawable.bmp3;
                        else if (v.getId() == R.id.frame4)
                            link = R.drawable.bmp4;
                        else if (v.getId() == R.id.frame5)
                            link = R.drawable.bmp5;
                        else if (v.getId() == R.id.frame6)
                            link = R.drawable.bmp6;
                        else if (v.getId() == R.id.frame7)
                            link = R.drawable.bmp7;
                        else
                            link = R.drawable.bmp8;

                        Intent intent = new Intent(CollageChooser.this, CollageCreator.class);
                        intent.putExtra("image", link);
                        startActivity(intent);
                    }
                });
            }
        }
    }


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
