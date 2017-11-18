package alvaro.daniel.space_crusade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Dani on 16/11/2017.
 */

public class BaseActivity extends AppCompatActivity implements MainFragment.OnMainFragmentListener{
    ImageView background;
    FrameLayout fragment_container;
    public static Preferences myPrefs = null;
    public static int actual_theme = 0;
    public final String ACTUAL_THEME = "actual_theme";
    private Fragment actualFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);

        fragment_container = findViewById(R.id.fragment_container);

        //si la aplicacion acaba de abrirse cargamos el fragment del menu principal
        //si se ha restaurado no es necesario
        if(savedInstanceState == null){
            firstFragment();
        }
    }

    private void firstFragment(){
        MainFragment firstFragment = new MainFragment();
        //comprobamos que la actividad tiene un contenedor para el fragment
        if(fragment_container != null){

            //Si la actividad se ha lanzado con datos e instrucciones adicionales desde un intent, pasamos sus extras al fragment
            firstFragment.setArguments(getIntent().getExtras());

            //añadimos el fragment al contenedor FrameLayout
            getSupportFragmentManager().beginTransaction().add(fragment_container.getId(), firstFragment).commit();
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
    }

    @Override
    public void changeMenu(Fragment newMenu, boolean canBack) {
        changeFragment(newMenu, canBack);
    }

    @Override
    public void exit() {
        finish();
    }
}
