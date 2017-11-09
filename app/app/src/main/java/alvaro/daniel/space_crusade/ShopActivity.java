package alvaro.daniel.space_crusade;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        manager = new DBManager(this);
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
}
