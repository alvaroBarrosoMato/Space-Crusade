package alvaro.daniel.space_crusade;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by alvar on 03/11/2017.
 */

public class ShopActivity extends AppCompatActivity {
    private DBManager manager;
    TextView name;
    TextView type;
    TextView description;
    TextView price;
    Cursor cursor;
    ListView listView;
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        manager = new DBManager(this);
        //manager.insert("Torrente",2000, "Comedia");
        name = (TextView)findViewById(R.id.Element_Name);
        type = (TextView)findViewById(R.id.Element_Type);
        description = (TextView)findViewById(R.id.Element_Description);
        price = (TextView)findViewById(R.id.Element_Buy);
        listView = (ListView)findViewById(R.id.lista);

        String[] from = new String[]{manager.CN_NAME, manager.CN_TYPE, manager.CN_DESCRIPTION, manager.CN_COST};
        int[] to = new int[]{R.id.Element_Name, R.id.Element_Type, R.id.Element_Description, R.id.Element_Buy, R.id.lista};
        cursor = manager.getAll();
        adapter = new SimpleCursorAdapter(this, R.layout.activity_element, cursor, from, to, 0);
        listView.setAdapter(adapter);
    }
}
