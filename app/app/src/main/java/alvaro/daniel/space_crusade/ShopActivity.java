package alvaro.daniel.space_crusade;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by alvar on 03/11/2017.
 */

public class ShopActivity extends AppCompatActivity{
    private DBManager manager;

    Cursor cursor;
    ListView listView;
    SimpleCursorAdapter adapter;

    public static final int UI_ANIMATION_DELAY = MainActivity.UI_ANIMATION_DELAY;
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
        setContentView(R.layout.activity_shop);
        Log.i("contextInfo",""+(ShopActivity.this==null));
        manager = new DBManager(ShopActivity.this);
        //manager.insert("Torrente",2000, "Comedia");

        listView = (ListView)findViewById(R.id.ElementList);
        search();

    }
    public void search(){
        //Log.i("Buscando", search.getText().toString());
        /*if(search.getText().toString().isEmpty()){
            cursor = manager.consultar();
        }else{
            cursor = manager.select(search.getText().toString());
        }*/
        cursor = manager.getAll();
        String[] from = new String[]{manager.CN_NAME, manager.CN_TYPE, manager.CN_DESCRIPTION, manager.CN_IMAGE, manager.CN_PRICE};
        int[] to = new int[]{R.id.Element_Name, R.id.Element_Type, R.id.Element_Description, R.id.Element_Image, R.id.Element_Buy};
        adapter = new SimpleCursorAdapter(this, R.layout.activity_element, cursor, from, to, 0);
        listView.setAdapter(adapter);
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(MainActivity.fullscreen){
            hide2();
        }else{
            delayedHide(100);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        MainActivity.fullscreen = false;
    }

    private void hide() {
        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void hide2(){
        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        MainActivity.fullscreen = true;
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
