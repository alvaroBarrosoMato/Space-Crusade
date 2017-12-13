package alvaro.daniel.space_crusade;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * Created by Dany on 03/11/2017.
 */

public class GameActivity extends AppCompatActivity {
    VideoView videoFrame;
    Preferences myPrefs;
    MediaPlayer videoMP;
    TextView skipText;
    MyCanvas myCanvas;
    int actual_theme = 0;
    int pausePos = 0;
    LinearLayout exitDialog, gameOver;
    SensorManager sensorManager;
    Sensor gyroscope;
    public static Input input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPrefs = BaseActivity.myPrefs;
        changeTheme();
        setContentView(R.layout.activity_game);

        myCanvas = (MyCanvas)findViewById(R.id.gameCanvas);
        exitDialog = (LinearLayout)findViewById(R.id.exitDialog);
        gameOver = (LinearLayout)findViewById(R.id.gameover);
        BaseActivity.setFont(exitDialog);
        BaseActivity.setFont(gameOver);

        ((Button)findViewById(R.id.dialogButtonOk)).setOnClickListener((view)->{
            Animation pressed_anim = AnimationUtils.loadAnimation(this, R.anim.button_pressed_anim);
            view.startAnimation(pressed_anim);
            exitGame();
        });
        ((Button)findViewById(R.id.dialogButtonNo)).setOnClickListener((view)->{
            Animation pressed_anim = AnimationUtils.loadAnimation(this, R.anim.button_pressed_anim);
            view.startAnimation(pressed_anim);
            toggleExitDialog(false);
        });
        ((Button)findViewById(R.id.dialogButtonRetry)).setOnClickListener((view)->{
            Animation pressed_anim = AnimationUtils.loadAnimation(this, R.anim.button_pressed_anim);
            view.startAnimation(pressed_anim);
            myCanvas.restartGame();
            toggleExitDialog(false);
        });

        ((Button)findViewById(R.id.gameOverExit)).setOnClickListener((view)->{
            Animation pressed_anim = AnimationUtils.loadAnimation(this, R.anim.button_pressed_anim);
            view.startAnimation(pressed_anim);
            exitGame();
        });
        ((Button)findViewById(R.id.gameOverRetry)).setOnClickListener((view)->{
            Animation pressed_anim = AnimationUtils.loadAnimation(this, R.anim.button_pressed_anim);
            view.startAnimation(pressed_anim);
            myCanvas.restartGame();
            toggleGameOver(false, 0);
        });

        videoFrame = (VideoView) findViewById(R.id.videoFrame);
        videoFrame.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"//" + R.raw.ignition));
        videoFrame.setOnCompletionListener((mp)->startGame());
        videoFrame.setOnPreparedListener((mp)->{
            videoMP = mp;
            if(!myPrefs.isFirstGame()) {
                videoFrame.setOnTouchListener((view, motionEvent) -> {
                    startGame();
                    return false;
                });
            }
            setVolume(myPrefs.musicVol);
            if(pausePos != 0)
                videoFrame.seekTo(pausePos);
            videoFrame.start();
        });

        skipText = (TextView)findViewById(R.id.skipText);
        skipText.setTextColor(ResourcesCompat.getColor(getResources(),R.color.font1,null));
        if(!myPrefs.isFirstGame()){
            skipText.postDelayed(()->showText(),3000);
        }

        //Gestion del input
        input = new Input();

        //sensor del gyroscopio
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        if(gyroscope == null){
            Toast.makeText(getBaseContext(), "Gyroscope sensosr not available.", Toast.LENGTH_SHORT).show();
            finish();
        }

        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values);

                //Remap coordinate system
                float[] remappedRotationMatrix = new float[16];
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        remappedRotationMatrix);

                // Convert to orientations
                float[] orientations = new float[3];
                SensorManager.getOrientation(remappedRotationMatrix, orientations);

                //conversion radians to degrees
                for(int i = 0; i < 3; i++) {
                    orientations[i] = (float)(Math.toDegrees(orientations[i]));
                }
                input.orientations = orientations;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        }, gyroscope, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_UP){
            input.touchUp = true;
            input.touchPos = new Vector2(event.getX(), event.getY());
        }
        return super.onTouchEvent(event);
    }

    public void changeTheme() {
        int theme = myPrefs.getTheme();
        switch(theme){
            case 0:{
                if(actual_theme != 0) {
                    setTheme(R.style.Dark);
                    actual_theme = 0;
                }
                break;
            }
            case 1:{
                if(actual_theme != 1) {
                    setTheme(R.style.Light);
                    actual_theme = 1;
                }
                break;
            }
        }
    }

    private void showText(){
        if(!myPrefs.isFirstGame() && skipText.getVisibility() == View.VISIBLE){
            skipText.setAlpha(1f);
            Animation skip_anim = AnimationUtils.loadAnimation(this, R.anim.skip_video_anim);
            skipText.startAnimation(skip_anim);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(videoMP != null){
            pausePos = videoMP.getCurrentPosition();
        }
        if(myCanvas.getGameThread() != null && myCanvas.getGameThread().getState() == Thread.State.RUNNABLE){
            myCanvas.pauseScene();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(myCanvas.getGameThread() != null && exitDialog.getVisibility() != View.VISIBLE && myCanvas.getGameThread().getState() == Thread.State.RUNNABLE){
            myCanvas.playScene();
        }
    }

    @Override
    protected void onDestroy() {
        destroyVideoMP();
        super.onDestroy();
    }

    private void destroyVideoMP(){
        if(videoMP != null){
            try{
            if(videoMP.isPlaying()) {
                videoMP.stop();
            }
            videoFrame.setVisibility(View.GONE);
                videoMP.reset();
                videoMP.release();
            }catch(IllegalStateException e){
                Log.e("Error in MediaPlayer", "Error callingg reset and release methods: "+e.getMessage());
            }
            videoMP = null;
            //videoFrame.stopPlayback();
            //videoFrame.destroyDrawingCache();
        }
    }

    private void startGame(){
        //videoFrame.setVisibility(View.GONE);
        destroyVideoMP();
        skipText.clearAnimation();
        skipText.setVisibility(View.GONE);
        myCanvas.startThread();
    }

    @Override
    public void onBackPressed(){
        if(myCanvas.getGameThread().getState() == Thread.State.RUNNABLE){
            if(exitDialog.getVisibility() != View.VISIBLE){
                myCanvas.pauseScene();
                toggleExitDialog(true);
            }else{
                toggleExitDialog(false);
            }
                /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("ExitGameDialogFragment");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                ExitGameFragmentDialog exitDialog = new ExitGameFragmentDialog();
                exitDialog.show(ft, "ExitGameDialogFragment");
               exitDialog.onCancel(new DialogInterface() {
                    @Override
                    public void cancel() {
                        Toast.makeText(getApplicationContext(), "Back pressed in fragment", Toast.LENGTH_SHORT).show();
                        Toast.makeText(GameActivity.this, "Back pressed in fragment", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void dismiss() {
                        Toast.makeText(getApplicationContext(), "Back pressed in fragment", Toast.LENGTH_SHORT).show();
                        Toast.makeText(GameActivity.this, "Back pressed in fragment", Toast.LENGTH_SHORT).show();
                    }
                });*/

        }else{
            super.onBackPressed();
        }
    }

    public void exitGame(){
        myCanvas.stopGame();
        finish();
    }

    private void toggleExitDialog(Boolean state){
        if(state){
            exitDialog.setVisibility(View.VISIBLE);
            Animation dialog_appear = AnimationUtils.loadAnimation(this, R.anim.dialog_appear);
            exitDialog.startAnimation(dialog_appear);
        }else{
            Animation dialog_disappear = AnimationUtils.loadAnimation(this, R.anim.dialog_disappear);
            exitDialog.startAnimation(dialog_disappear);
            dialog_disappear.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    exitDialog.setVisibility(View.GONE);
                    myCanvas.playScene();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    public void toggleGameOver(Boolean state, int distance){
        if(state){
            myCanvas.pauseScene();
            ((TextView)findViewById(R.id.distance)).setText(Integer.toString(distance));
            //check for new record
            if(distance > myPrefs.getRecord()){
                ((TextView)findViewById(R.id.newRecord)).setVisibility(View.VISIBLE);
                myPrefs.refreshRecord(distance);
            }
            gameOver.setVisibility(View.VISIBLE);
            Animation dialog_appear = AnimationUtils.loadAnimation(this, R.anim.dialog_appear);
            gameOver.startAnimation(dialog_appear);
        }else{
            ((TextView)findViewById(R.id.newRecord)).setVisibility(View.INVISIBLE);
            Animation dialog_disappear = AnimationUtils.loadAnimation(this, R.anim.dialog_disappear);
            gameOver.startAnimation(dialog_disappear);
            dialog_disappear.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    gameOver.setVisibility(View.GONE);
                    myCanvas.playScene();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    public void continueGame(){
        myCanvas.playScene();
    }

    private void setVolume(int value){
        double num = 50 - value > 0? Math.log(50 - value) : 0;
        float volume = (float) (1 - (num/ Math.log(50)));
        videoMP.setVolume(volume, volume);
    }
}

