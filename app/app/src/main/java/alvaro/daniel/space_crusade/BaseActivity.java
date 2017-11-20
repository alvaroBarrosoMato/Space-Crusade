package alvaro.daniel.space_crusade;

import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Dani on 16/11/2017.
 */

public class BaseActivity extends AppCompatActivity /*implements MainFragment.OnMainFragmentListener*/{
    ImageView background;
    FrameLayout fragment_container;
    public static Preferences myPrefs = null;
    public static int actual_theme = 0;
    public final String ACTUAL_THEME = "actual_theme";
    private Fragment actualFragment;
    private boolean mainMenuFocus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //cargamos las preferencias guardadas en el movil para configurar los datos
        if(myPrefs == null)
            loadPrefs();
        changeTheme();
        setContentView(R.layout.activity_background);

        fragment_container = findViewById(R.id.fragment_container);
        background = (ImageView)findViewById(R.id.background);
        TextView record = (TextView)findViewById(R.id.textRecord);
        record.setText(Integer.toString(myPrefs.getRecord()));
        TextView money = (TextView)findViewById(R.id.textMoney);
        money.setText(Integer.toString(myPrefs.getMoney()));

        //si la aplicacion acaba de abrirse cargamos el fragment del menu principal
        //si se ha restaurado no es necesario
        mainMenuFocus = true;
        if(savedInstanceState == null && actualFragment == null){
            firstFragment();
        }
        startBackgroundAnim();
    }

    private void firstFragment(){
        MainFragment firstFragment = new MainFragment();
        //comprobamos que la actividad tiene un contenedor para el fragment
        if(fragment_container != null){

            //Si la actividad se ha lanzado con datos e instrucciones adicionales desde un intent, pasamos sus extras al fragment
            firstFragment.setArguments(getIntent().getExtras());

            //añadimos el fragment al contenedor FrameLayout
            getSupportFragmentManager().beginTransaction().add(fragment_container.getId(), firstFragment).commit();
            actualFragment = firstFragment;
        }
    }

    private void changeFragment(Fragment newFragment, boolean canBack){
        //creamos el fragment y le pasamos argumentos
        Bundle args = new Bundle();
        args.putInt(this.ACTUAL_THEME, actual_theme);
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //reemplazamos el fragment del contenedor por el nuevo
        transaction.replace(R.id.fragment_container, newFragment);

        if(canBack){
            //con esto dejamos que el usuario pueda retroceder a los fragments anteriores
            transaction.addToBackStack(null);
        }
        transaction.commit();
        actualFragment = newFragment;
        mainMenuFocus = false;
    }

    private void changeTheme() {
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

    private void startBackgroundAnim(){
        AnimationDrawable back = createAnimation();
        background.setBackground(back);
        back.start();
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

    private void backToMainMenu(){
        actualFragment = (MainFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        mainMenuFocus = true;
    }

    public void changeMenu(Fragment newMenu, boolean canBack) {
        changeFragment(newMenu, canBack);
    }

    @Override
    public void onBackPressed(){
        // do something here and don't write super.onBackPressed()
        if(mainMenuFocus){
            ExitAppFragmentDialog exitDialog = new ExitAppFragmentDialog();
            exitDialog.show(getSupportFragmentManager(), "ExitAppDialogFragment");
        }else{
            super.onBackPressed();
            backToMainMenu();
        }
    }

    /*@Override
    public void exit() {
        finish();
    }*/

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
