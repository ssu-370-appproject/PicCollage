package com.example.photocollage;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ortiz.touchview.TouchImageView;

/**
 * An example Activity for how to handle a TouchImageView that might be resized.
 *
 * If you want your image to look like it's being cropped or sliding when you resize it, instead of
 * changing its zoom level, you probably want ScaleType.CENTER. Here's an example of how to use it:
 *
 *   image.setScaleType(CENTER);
 *   image.setMinZoom(TouchImageView.AUTOMATIC_MIN_ZOOM);
 *   image.setMaxZoomRatio(3.0f);
 *   float widthRatio = (float) image.getMeasuredWidth() / image.getDrawable().getIntrinsicWidth();
 *   float heightRatio = (float) image.getMeasuredHeight() / image.getDrawable().getIntrinsicHeight();
 *   image.setZoom(Math.max(widthRatio, heightRatio));  // For an initial view that looks like CENTER_CROP
 *   image.setZoom(Math.min(widthRatio, heightRatio));  // For an initial view that looks like FIT_CENTER
 *
 * That code is run when the button displays "CENTER (with X zoom)".
 *
 * You can use other ScaleTypes, but for all of them, the size of the image depends somehow on the
 * size of the TouchImageView, just like it does in ImageView. You can thus expect your image to
 * change magnification as its View changes sizes.
 */
public class ChangeSizeExampleActivity extends AppCompatActivity {
    private TouchImageView image;
    private FrameLayout imageContainer;
    private Button switchScaleTypeButton;

    BottomNavigationView botnav;
    ValueAnimator xSizeAnimator = new ValueAnimator();
    ValueAnimator ySizeAnimator = new ValueAnimator();
    private int xSizeAdjustment = 0;
    private int ySizeAdjustment = 0;

    //
    // Two of the ScaleTypes are stand-ins for CENTER with different initial zoom levels. This is
    // special-cased in processScaleType.
    //
    private static final ImageView.ScaleType[] scaleTypes = {
            ImageView.ScaleType.CENTER,
            ImageView.ScaleType.CENTER_CROP,
            ImageView.ScaleType.FIT_START, // stand-in for CENTER with initial zoom that looks like FIT_CENTER
            ImageView.ScaleType.FIT_END,  // stand-in for CENTER with initial zoom that looks like CENTER_CROP
            ImageView.ScaleType.CENTER_INSIDE,
            ImageView.ScaleType.FIT_XY,
            ImageView.ScaleType.FIT_CENTER};
    private int scaleTypeIndex = 0;

    private static int[] images = { R.drawable.nature_1, R.drawable.nature_2, R.drawable.nature_6, R.drawable.nature_7, R.drawable.nature_8 };
    public static final int IMAGE_GALLERY_REQUEST = 20;
    private int imageIndex = 0;

    private SizeBehaviorAdjuster resizeAdjuster;
    private SizeBehaviorAdjuster rotateAdjuster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_size2);
        image = findViewById(R.id.img);
        image.setBackgroundColor(Color.LTGRAY);
        image.setMinZoom(TouchImageView.AUTOMATIC_MIN_ZOOM);
        image.setMaxZoomRatio(6.0f);
        imageContainer = findViewById(R.id.image_container);
        botnav = findViewById(R.id.bottom_navigation_manip);

        //findViewById(R.id.left).setOnClickListener(new SizeAdjuster(-1, 0));
        //findViewById(R.id.right).setOnClickListener(new SizeAdjuster(1, 0));
        //findViewById(R.id.up).setOnClickListener(new SizeAdjuster(0, -1));
        //findViewById(R.id.down).setOnClickListener(new SizeAdjuster(0, 1));

        resizeAdjuster = new SizeBehaviorAdjuster(false, "resize: ");
        rotateAdjuster = new SizeBehaviorAdjuster(true, "rotate: ");
        findViewById(R.id.resize).setOnClickListener(resizeAdjuster);
        findViewById(R.id.rotate).setOnClickListener(rotateAdjuster);

        botnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_center:
                        scaleTypeIndex = (scaleTypeIndex + 1) % scaleTypes.length;
                        processScaleType(scaleTypes[scaleTypeIndex], true);
                        break;
                    case R.id.action_rotate:

                        break;
                    case R.id.action_rotate_left:

                        break;
                }
                return true;
            }
        });
        if (savedInstanceState != null) {
            scaleTypeIndex = savedInstanceState.getInt("scaleTypeIndex");
            resizeAdjuster.setIndex((Button) findViewById(R.id.resize), savedInstanceState.getInt("resizeAdjusterIndex"));
            rotateAdjuster.setIndex((Button) findViewById(R.id.rotate), savedInstanceState.getInt("rotateAdjusterIndex"));
            imageIndex = savedInstanceState.getInt("imageIndex");
            image.setImageResource(images[imageIndex]);
        }

        image.post(new Runnable() {
            @Override
            public void run() {
                processScaleType(scaleTypes[scaleTypeIndex], false);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("scaleTypeIndex", scaleTypeIndex);
        outState.putInt("resizeAdjusterIndex", resizeAdjuster.index);
        outState.putInt("rotateAdjusterIndex", rotateAdjuster.index);
        outState.putInt("imageIndex", imageIndex);
    }

    @SuppressLint("SetTextI18n")
    private void processScaleType(ImageView.ScaleType scaleType, boolean resetZoom) {
        if (scaleType == ImageView.ScaleType.FIT_END) {
            //switchScaleTypeButton.setText(ImageView.ScaleType.CENTER.name() + " (with " + ImageView.ScaleType.CENTER_CROP.name() + " zoom)");
            //image.setScaleType(ImageView.ScaleType.CENTER);
            float widthRatio = (float) image.getMeasuredWidth() / image.getDrawable().getIntrinsicWidth();
            float heightRatio = (float) image.getMeasuredHeight() / image.getDrawable().getIntrinsicHeight();
            if (resetZoom) {
                image.setZoom(Math.max(widthRatio, heightRatio));
            }
        } else if (scaleType == ImageView.ScaleType.FIT_START) {
            //switchScaleTypeButton.setText(ImageView.ScaleType.CENTER.name() + " (with " + ImageView.ScaleType.FIT_CENTER.name() + " zoom)");
            //image.setScaleType(ImageView.ScaleType.CENTER);
            float widthRatio = (float) image.getMeasuredWidth() / image.getDrawable().getIntrinsicWidth();
            float heightRatio = (float) image.getMeasuredHeight() / image.getDrawable().getIntrinsicHeight();
            if (resetZoom) {
                image.setZoom(Math.min(widthRatio, heightRatio));
            }
        } else {
            //switchScaleTypeButton.setText(scaleType.name());
            image.setScaleType(scaleType);
            if (resetZoom) {
                image.resetZoom();
            }
        }
    }

    private void adjustImageSize() {
        double width = imageContainer.getMeasuredWidth() * Math.pow(1.1, xSizeAdjustment);
        double height = imageContainer.getMeasuredHeight() * Math.pow(1.1, ySizeAdjustment);
        xSizeAnimator.cancel();
        ySizeAnimator.cancel();
        xSizeAnimator = ValueAnimator.ofInt(image.getWidth(), (int) width);
        ySizeAnimator = ValueAnimator.ofInt(image.getHeight(), (int) height);
        xSizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams layoutParams = image.getLayoutParams();
                layoutParams.width = (Integer) animation.getAnimatedValue();
                image.setLayoutParams(layoutParams);
            }
        });
        ySizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams layoutParams = image.getLayoutParams();
                layoutParams.height = (Integer) animation.getAnimatedValue();
                image.setLayoutParams(layoutParams);
            }
        });
        xSizeAnimator.setDuration(200);
        ySizeAnimator.setDuration(200);
        xSizeAnimator.start();
        ySizeAnimator.start();
    }

    private class SizeAdjuster implements View.OnClickListener {
        int dx, dy;

        SizeAdjuster(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        @Override
        public void onClick(View v) {
            int newXScale = Math.min(0, xSizeAdjustment + dx);
            int newYScale = Math.min(0, ySizeAdjustment + dy);
            if (newXScale == xSizeAdjustment && newYScale == ySizeAdjustment) {
                return;
            }
            xSizeAdjustment = newXScale;
            ySizeAdjustment = newYScale;
            adjustImageSize();
        }
    }

    private class SizeBehaviorAdjuster implements View.OnClickListener {
        private final TouchImageView.FixedPixel[] values = TouchImageView.FixedPixel.values();
        private int index = 0;
        private boolean forOrientationChanges;
        private String buttonPrefix;

        SizeBehaviorAdjuster(boolean forOrientationChanges, String buttonPrefix) {
            this.forOrientationChanges = forOrientationChanges;
            this.buttonPrefix = buttonPrefix;
        }

        @Override
        public void onClick(View v) {
            setIndex((Button) v, (index + 1) % values.length);
        }

        public int getIndex() {
            return index;
        }

        @SuppressLint("SetTextI18n")
        void setIndex(Button b, int index) {
            this.index = index;
            if (forOrientationChanges) {
                image.setOrientationChangeFixedPixel(values[index]);
            } else {
                image.setViewSizeChangeFixedPixel(values[index]);
            }
            b.setText(buttonPrefix + values[index].name());
        }
    }
}
