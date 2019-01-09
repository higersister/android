package cn.cmy.custom;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "$$$$$$$$$$$$$$$$";
    private ImageView mImg;
    Matrix matrix = new Matrix();


    /*private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private OnScaleGestureDetector onScaleGestureDetector;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        init();
    }

    private void init() {
        if (null != getSupportActionBar()) {
            getSupportActionBar().hide();
        }
      /*  onScaleGestureDetector = new OnScaleGestureDetector();
        scaleGestureDetector = new ScaleGestureDetector(getBaseContext(), onScaleGestureDetector);
        gestureDetector = new GestureDetector(getBaseContext(), onScaleGestureDetector);
       */ /*mImg = findViewById(R.id.img);
        matrix.postScale(0.5f, 0.4f);
        mImg.setImageMatrix(matrix);
        mImg.setOnTouchListener(new OnTouchListener());*/



    }


    private class OnTouchListener implements View.OnTouchListener {

        private static final int MODE_DRAG = 1;
        private static final int MODE_ZOOM = 2;
        private int mode = 0;
        private PointF pointF = new PointF();
        private PointF midPoint = new PointF();
        private Matrix oldMatrix = new Matrix();
        private Matrix newMatrix = matrix;
        private float oldDistance;
        private float newDistance;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction() & event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: {
                    mode = MODE_DRAG;
                    pointF.set(event.getX(), event.getY());
                    oldMatrix.set(mImg.getImageMatrix());
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    if (mode == MODE_DRAG) {
                        float dx = event.getX() - pointF.x;
                        float dy = event.getY() - pointF.y;
                        newMatrix.set(oldMatrix);
                        newMatrix.postTranslate(dx, dy);
                    } else if (mode == MODE_ZOOM) {
                        float dx = event.getX(0) - event.getX(1);
                        float dy = event.getY(0) - event.getY(1);
                        newMatrix.set(oldMatrix);
                        newDistance = (float) Math.sqrt(dx * dx + dy * dy);
                        newMatrix.postScale(newDistance / oldDistance, newDistance / oldDistance, midPoint.x, midPoint.y);
                    }
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP: {
                    mode = 0;
                    break;
                }
                case MotionEvent.ACTION_POINTER_DOWN: {
                    mode = MODE_ZOOM;
                    oldMatrix.set(mImg.getImageMatrix());
                    float dx = event.getX(0) - event.getX(1);
                    float dy = event.getY(0) - event.getY(1);
                    oldDistance = (float) Math.sqrt(dx * dx + dy * dy);
                    midPoint.set((event.getX(0) + event.getX(1)) / 2, (event.getY(0) + event.getY(1)) / 2);
                    break;
                }


            }
            mImg.setImageMatrix(newMatrix);
            return true;
        }
    }


    /*    private class OnScaleGestureDetector implements ScaleGestureDetector.OnScaleGestureListener, GestureDetector.OnGestureListener {

        private Matrix oldMatrix = matrix;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            oldMatrix.postScale(detector.getCurrentSpan() / detector.getPreviousSpan(),
                    detector.getCurrentSpan() / detector.getPreviousSpan(), detector.getFocusX(), detector.getFocusY());
            mImg.setImageMatrix(oldMatrix);

            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {

            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            oldMatrix.preTranslate(-distanceX, -distanceY);
            mImg.setImageMatrix(oldMatrix);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        return scaleGestureDetector.onTouchEvent(event);
    }*/
}
