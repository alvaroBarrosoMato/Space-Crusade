package alvaro.daniel.space_crusade;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Dany on 27/11/2017.
 */

public class MyCanvas extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private Context ctx;
    private GameThread myThread;
    Bitmap mbitmap;

    public MyCanvas(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        ctx = context;
        holder = getHolder();
        holder.addCallback(this);
        mbitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.space_ship);
        holder.setFixedSize(holder.getSurfaceFrame().width(), holder.getSurfaceFrame().height());
    }

    public void startThread(){
        myThread.setRunning(true);
        myThread.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder){
        myThread = new GameThread(holder, ctx, this, 10);
        /*canvas = holder.lockCanvas(null);
        draw(canvas);
        holder.unlockCanvasAndPost(canvas);*/
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        myThread.interrupt();
        boolean retry = true;
        while(retry){
            try{
                myThread.join();
                retry = false;
            }
            catch(Exception e){
                Log.v("Exception Occured", e.getMessage());
            }
        }
    }

    public GameThread getGameThread(){
        return myThread;
    }

    public boolean pauseScene(){
        return myThread.myScene.pause();
    }

    public boolean playScene(){
       return myThread.myScene.play();
    }

    public Scene.SCENE_STATE getSceneState(){
        return myThread.myScene.getState();
    }

    public void stopGame(){
        myThread.interrupt();
        try {
            myThread.join();
        } catch (InterruptedException e) {
            Log.e("Thread Join Error", "The Thread Was interrupted before join.");
            e.printStackTrace();
        }
    }

    public void restartGame(){
        myThread.restartGame();
    }

    public void doDraw(Canvas canvas, Scene myScene){
        canvas.drawColor(ResourcesCompat.getColor(getResources(),R.color.blueSky,null));
        Rect rec = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3);
        p.setColor(Color.RED);
        canvas.drawRect(rec, p);
        canvas.drawBitmap(mbitmap, 60, 200, null);
    }
}
