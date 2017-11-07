package alvaro.daniel.space_crusade;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by alvaro on 03/11/2017.
 */

public class ShopActivity extends AppCompatActivity {
    private DBManager manager;
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
    }
}
