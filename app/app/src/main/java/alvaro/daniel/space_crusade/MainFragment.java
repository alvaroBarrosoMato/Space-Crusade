package alvaro.daniel.space_crusade;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Dani on 16/11/2017.
 */

public class MainFragment extends Fragment implements View.OnClickListener{
    View layout;
    Button gameButton, creditsButton, shopButton, settingsButton;
    ImageButton exitButton;

    //interface para recibir los callbacks del fragment desde la actividad principal
    OnMainFragmentListener mCallback;

    public interface OnMainFragmentListener{
        public void changeMenu(Fragment newMenu, boolean canBack);
        public void exit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //cargamos el layout para este fragmento
        layout = inflater.inflate(R.layout.fragment_main, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        creditsButton = (Button)layout.findViewById(R.id.creditsButton);
        creditsButton.setOnClickListener(this);
        shopButton = (Button)layout.findViewById(R.id.shopButton);
        shopButton.setOnClickListener(this);
        gameButton = (Button)layout.findViewById(R.id.gameButton);
        gameButton.setOnClickListener(this);
        settingsButton = (Button)layout.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.gameButton:{
                ((BaseActivity)getActivity()).changeMenu(new GameFragment(), true);
                //mCallback.changeMenu(new GameFragment(), true);
                break;}
            case R.id.shopButton:{
                break;}
            case R.id.settingsButton:{
                break;}
            case R.id.creditsButton:{
                break;}
            case R.id.exitButton:{
                mCallback.exit();
                break;}
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //nos aseguramos de que la actividad que contiene el fragmento implemente nuestra interfaz
        //si no es asi lanzamos una excepcion
        try{
            mCallback = (OnMainFragmentListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()+" must implement OnMainFragmentListener interface");
        }
    }

    public void showMsg(){
        Toast.makeText(getContext(), "Hola me has llamado", Toast.LENGTH_SHORT).show();
    }
}
