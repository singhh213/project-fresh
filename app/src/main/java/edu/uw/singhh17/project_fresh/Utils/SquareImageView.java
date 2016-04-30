package edu.uw.singhh17.project_fresh.Utils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by harpreetsingh on 4/30/16.
 */
public class SquareImageView extends ImageView {


        public SquareImageView(Context context) {
            super(context);
        }

        public SquareImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            // check device's current orientation
            int orientation = getResources().getConfiguration().orientation;

            if (orientation == Configuration.ORIENTATION_LANDSCAPE) { // app is in landscape mode
                // get ImageView's width
                int width = getMeasuredWidth();
                // set width and height to width
                setMeasuredDimension(width, width);
            } else { // app is in portrait mode
                // get ImageView's height
                int height = getMeasuredHeight();
                // set width and height to height
                setMeasuredDimension(height, height);
            }
        }

}
