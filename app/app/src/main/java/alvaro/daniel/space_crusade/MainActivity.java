package alvaro.daniel.space_crusade;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button gameButton, creditsButton, shopButton, settingsButton;
    ImageButton exitButton;
    public static Preferences myPrefs = null;
    public static int actualTheme = 0;
    ImageView background;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    public static final int UI_ANIMATION_DELAY = 300;
    public static boolean fullscreen;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            hide2();
        }
    };

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!fullscreen) {
            delayedHide(100);
        }
        //cargamos las preferencias guardadas en el movil para configurar los datos
        if(myPrefs == null) {
            loadPrefs();
        }
        changeTheme();
        setContentView(R.layout.activity_main);
        /*Intent i = new Intent(MainActivity.this, BaseActivity.class);
        startActivity(i);*/

        TextView record = (TextView)findViewById(R.id.textRecord);
        record.setText(Integer.toString(myPrefs.getRecord()));
        TextView money = (TextView)findViewById(R.id.textMoney);
        money.setText(Integer.toString(myPrefs.getMoney()));

        creditsButton = (Button)findViewById(R.id.creditsButton);
        creditsButton.setOnClickListener(this);
        shopButton = (Button)findViewById(R.id.shopButton);
        shopButton.setOnClickListener(this);
        gameButton = (Button)findViewById(R.id.gameButton);
        gameButton.setOnClickListener(this);
        settingsButton = (Button)findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(this);
        exitButton = (ImageButton)findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);

        background = (ImageView)findViewById(R.id.background);
        loadBackround();
    }

    private void loadBackround() {
        if(actualTheme == 0){
            background.setBackgroundResource(R.drawable.anim_background_dark);
        }else{
            background.setBackgroundResource(R.drawable.anim_background_dark);
        }

        AnimationDrawable pro = (AnimationDrawable)background.getBackground();
        pro.start();
    }

    private void changeTheme() {
        int theme = myPrefs.getTheme();
        switch(theme){
            case 0:{
                if(actualTheme != 0) {
                    setTheme(R.style.Dark);
                    actualTheme = 0;
                }
                break;
            }
            case 1:{
                if(actualTheme != 1) {
                    setTheme(R.style.Light);
                    actualTheme = 1;
                }
                break;
            }
        }
    }

    private void loadPrefs() {
        myPrefs = new Preferences(getPreferences(MODE_PRIVATE));
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(fullscreen){
            hide2();
        }else{
            delayedHide(100);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        fullscreen = false;
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void hide2(){
        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

        decorView.setSystemUiVisibility(uiOptions);
        fullscreen = true;
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.gameButton:{
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                startActivity(i);
                break;}
            case R.id.shopButton:{
                Intent i = new Intent(MainActivity.this, ShopActivity.class);
                startActivity(i);
                break;}
            case R.id.settingsButton:{
                //Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                //startActivity(i);
                break;}
            case R.id.creditsButton:{
                //Intent i = new Intent(MainActivity.this, CreditsActivity.class);
                //startActivity(i);
                break;}
            case R.id.exitButton:{
                finish();
                break;}
        }
    }

    public void debugTheme(View view){
        if(myPrefs.getTheme() == 0){
            myPrefs.refreshTheme(1);
        }else{
            myPrefs.refreshTheme(0);
        }
        recreate();
    }

    public void debugMoney(View view){
        myPrefs.incrementMoney(100);


        TextView money = (TextView)findViewById(R.id.textMoney);
        money.setText(Integer.toString(myPrefs.getMoney()));
    }

    public void debugRecord(View view){
        myPrefs.incrementRecord(100);
        TextView record = (TextView)findViewById(R.id.textRecord);
        record.setText(Integer.toString(myPrefs.getRecord()));
    }
}
