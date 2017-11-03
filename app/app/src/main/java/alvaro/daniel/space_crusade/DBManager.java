package alvaro.daniel.space_crusade;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;

public class DBManager {
    public static final String TABLE_NAME = "shop";
    public static final String CN__ID = "_id";
    public static final String CN_NAME = "name";
    public static final String CN_TYPE = "type";
    public static final String CN_DESCRIPTION = "description";
    public static final String CN_IMAGE = "image";


    private DBHelper helper;
    private SQLiteDatabase db;

    public DbManager (Context context)  {
        try {
            helper = new DBHelper(context);
            db = helper.getMyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Cursor select (String type) {
        String[] columnas = new String[]{CN__ID,CN_NAME, CN_TYPE, CN_DESCRIPTION, CN_IMAGE};
        return db.query(TABLE_NAME, columnas, CN_TYPE +" LIKE ?", new String[]{"%" + type + "%"}, null, null, null);
    }

    public Cursor getAll () {
        String[] columnas = new String[]{CN__ID,CN_NAME, CN_TYPE, CN_DESCRIPTION, CN_IMAGE};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);
    }

    public Cursor getOrdered (String field) {
        String[] columnas = new String[]{CN__ID,CN_NAME, CN_TYPE, CN_DESCRIPTION, CN_IMAGE};
        return db.query(TABLE_NAME, columnas, null, null, null, null, field);
    }





}
