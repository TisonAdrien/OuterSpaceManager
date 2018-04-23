package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class BackgroundView extends SurfaceView implements Runnable {

    private Thread thread = null;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private ArrayList<Star> stars = new ArrayList<Star>();
    private Context context;
    private Boolean animated;

    public void animate(Context context, int screenX, int screenY){
        this.context = context;

        animated = true;

        surfaceHolder = getHolder();
        paint = new Paint();

        int starNums = 100;
        for (int i = 0; i < starNums; i++) {
            Star s  = new Star(screenX,screenY );
            stars.add(s);
        }
    }

    public BackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public BackgroundView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public BackgroundView(Context context){
        super(context);

    }

    @Override
    public void run() {
        while (animated){
            update();
            draw();
            control();
        }
    }

    private void update(){
        for (Star s : stars) {
            s.update(1);
        }
    }

    private void draw(){
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            paint.setColor(Color.WHITE);
            for (Star s : stars) {
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }

            //Unlocking the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause(){
        animated = false;
        thread.interrupt();
    }

    public void resume(){
        animated = true;
        thread = new Thread(this);
        thread.start();
    }
}
