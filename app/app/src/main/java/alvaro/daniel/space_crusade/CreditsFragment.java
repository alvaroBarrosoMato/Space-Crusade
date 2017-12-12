package alvaro.daniel.space_crusade;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Dany on 20/11/2017.
 */

public class CreditsFragment extends Fragment {
    View layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //cargamos el layout para este fragmento
        layout = inflater.inflate(R.layout.fragment_credits, container, false);
        ((BaseActivity)getActivity()).setFont(layout);
        ((TextView)layout.findViewById(R.id.copyright)).setTypeface(Typeface.DEFAULT);
        return layout;
    }
}
