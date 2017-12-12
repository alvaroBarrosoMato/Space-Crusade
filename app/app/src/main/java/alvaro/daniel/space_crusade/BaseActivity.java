package alvaro.daniel.space_crusade;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Dani on 16/11/2017.
 */

public class BaseActivity extends AppCompatActivity /*implements MainFragment.OnMainFragmentListener*/{
    ImageView background;
    FrameLayout fragment_container;
    TextView money, record;
    public static Preferences myPrefs = null;
    static int actual_theme = 0;
    //public final String ACTUAL_THEME = "actual_theme";
    private Fragment actualFragment;
    //private boolean mainMenuFocus;
    //FragmentManager fragManager;
    static MediaPlayer mainTheme;
    public static boolean recreating;
    static Typeface spaceFont;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //cargamos las preferencias guardadas en el movil para configurar los datos
        if(myPrefs == null)
            loadPrefs();
        changeTheme();
        setContentView(R.layout.activity_background);
        spaceFont = Typeface.createFromAsset(getAssets(), "fonts/radiospacebold.ttf");

        fragment_container = findViewById(R.id.fragment_container);
        background = (ImageView)findViewById(R.id.background);
        record = (TextView)findViewById(R.id.textRecord);
        changeRecord(myPrefs.getRecord());
        money = (TextView)findViewById(R.id.textMoney);
        changeMoney(myPrefs.getMoney());

        //fragManager = getSupportFragmentManager();

        if(!recreating) {
            mainTheme = MediaPlayer.create(BaseActivity.this, R.raw.space_music_orion);
            mainTheme.setLooping(true);
            setVolume(myPrefs.musicVol);
            mainTheme.start();
        }

        //si la aplicacion acaba de abrirse cargamos el fragment del menu principal
        //si se ha restaurado no es necesario
        //mainMenuFocus = true;
        if(savedInstanceState == null && actualFragment == null){
            firstFragment();
        }
        setFont(findViewById(R.id.header));
        startBackgroundAnim();
        recreating = false;
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(!recreating)
            mainTheme.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mainTheme.start();
        if(background.getBackground() == null){
            startBackgroundAnim();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!recreating) {
            mainTheme.release();
            mainTheme = null;
        }
    }

    @Override
    public void onBackPressed(){
        // do something here and don't write super.onBackPressed()
        // cuando el recuento de fragmentos disponibles en el stack es 0 no volvemos atras ya que estamos en el menu principal.
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
        }else{
            ExitAppFragmentDialog exitDialog = new ExitAppFragmentDialog();
            exitDialog.show(getSupportFragmentManager(), "ExitAppDialogFragment");
        }
        /*
        if(mainMenuFocus){
            ExitAppFragmentDialog exitDialog = new ExitAppFragmentDialog();
            exitDialog.show(getSupportFragmentManager(), "ExitAppDialogFragment");
        }else{
            super.onBackPressed();
            backToMainMenu();
        }*/
    }

    private void firstFragment(){
        MainFragment firstFragment = new MainFragment();
        //comprobamos que la actividad tiene un contenedor para el fragment
        if(fragment_container != null){

            //Si la actividad se ha lanzado con datos e instrucciones adicionales desde un intent, pasamos sus extras al fragment
            firstFragment.setArguments(getIntent().getExtras());

            //añadimos el fragment al contenedor FrameLayout
            getSupportFragmentManager().beginTransaction().add(fragment_container.getId(), firstFragment, "main").commitNow();
            actualFragment = firstFragment;
        }
    }

    public void stop(){
        background.setBackground(null);
    }

    public void changeFragment(Fragment newFragment){
        //creamos el fragment y le pasamos argumentos
        Bundle args = new Bundle();
        //args.putInt(this.ACTUAL_THEME, actual_theme);
        newFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //reemplazamos el fragment del contenedor por el nuevo
        transaction.replace(R.id.fragment_container, newFragment);

        //con esto dejamos que el usuario pueda retroceder a los fragments anteriores
        transaction.addToBackStack(null);
        transaction.commit();
        //transaction.runOnCommit(() -> Toast.makeText(this, "BackStackCount "+fragManager.getBackStackEntryCount(), Toast.LENGTH_SHORT).show());
        actualFragment = newFragment;
        //mainMenuFocus = false;
    }

    private static ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }

    public static void setFont(View root){
        //devuelve una lista de todos los hijos del view (sin contar los view groups)
        ArrayList<View> allViews = getAllChildren(root);
        for(View v : allViews){
            if(v instanceof TextView){
                TextView tv = (TextView) v;
                tv.setTypeface(spaceFont);
            }else if(v instanceof Button){
                Button bv = (Button) v;
                bv.setTypeface(spaceFont);
            }
        }
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

    public static void setVolume(int value){
        double num = 50 - value > 0? Math.log(50 - value) : 0;
        float volume = (float) (1 - (num/ Math.log(50)));
        mainTheme.setVolume(volume, volume);
    }

    private void loadPrefs() {
        myPrefs = new Preferences(getPreferences(MODE_PRIVATE));
    }

    /*@Override
    public void onWindowFocusChanged(boolean hasFocus){
        if(hasFocus) {
            changeTheme();
            //startBackgroundAnim();
        }
    }*/

    public void startBackgroundAnim(){
        if(background.getBackground() == null){
            AnimationDrawable back = createAnimation();
            background.setBackground(back);
            back.start();
        }
    }

    private AnimationDrawable createAnimation(){
        AnimationDrawable animation = new AnimationDrawable();
        animation.setOneShot(false);
        Resources res = this.getResources();

        String theme = actual_theme == 0? "dark" : "light";
        int duration = 80;
        for(int i = 0; i < 75; i++){
            String name = "background_" + theme + i;
                    //String.format("%03d", from);
            int globeId = res.getIdentifier(name, "drawable",
                    this.getPackageName());

            animation.addFrame(res.getDrawable(globeId), duration);
        }
        return animation;
    }

    /*private void backToMainMenu(){
        actualFragment = fragManager.findFragmentById(R.id.fragment_container);
        mainMenuFocus = true;
    }*/

    /*public void changeMenu(Fragment newMenu) {
        changeFragment(newMenu);
    }*/

    public void changeMoney(int value){
        money.setText(Integer.toString(value));
    }
    public void changeRecord(int value){
        record.setText(Integer.toString(value));
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
        changeMoney(myPrefs.getMoney());
    }

    public void debugRecord(View view){
        myPrefs.incrementRecord(100);
        changeRecord(myPrefs.getRecord());
    }
}
