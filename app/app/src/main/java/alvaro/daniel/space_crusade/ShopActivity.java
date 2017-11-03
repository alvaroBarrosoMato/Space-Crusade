package alvaro.daniel.space_crusade;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by alvar on 03/11/2017.
 */

public class ShopActivity {
    private DbManager manager;
    EditText name;
    EditText year;
    EditText genre;
    EditText search;
    Cursor cursor;
    ListView listView;
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        manager = new DbManager(this);
        //manager.insert("Torrente",2000, "Comedia");
        name = (EditText)findViewById(R.id.TextName);
        year = (EditText)findViewById(R.id.TextYear);
        genre = (EditText)findViewById(R.id.TextGenre);
        search = (EditText)findViewById(R.id.TextSearch);
        listView = (ListView)findViewById(R.id.lista);
    }
}
