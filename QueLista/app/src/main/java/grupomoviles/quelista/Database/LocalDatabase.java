package grupomoviles.quelista.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pablo on 21/12/2015.
 */
public class LocalDatabase extends SQLiteOpenHelper {

    /*
    Nombre de la BD
     */
    private static final String TABLE_NAME = "products";
    /*
    Columnas de la base de datos
     */
    public static final String COLUMN_BARCODE = "barcode";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_BRAND = "brand";
    public static final String COLUMN_NETVALUE = "netvalue";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_SUBCATEGORY = "subcategory";
    public static final String COLUMN_STOCK = "stock";
    public static final String COLUMN_MINSTOCK = "minstock";
    public static final String COLUMN_LASTUPDATE = "lastupdate";
    public static final String COLUMN_CONSUMECYCLE = "consumecycle";
    public static final String COLUMN_CONSUMEUNITS = "consumeunits";
    public static final String COLUMN_SHOPPINGLISTUNITS = "shoppinglistunits";
    public static final String COLUMN_CARTUNITS = "cartunits";

    /*
    Nombre y version de la BD
     */
    private static final String DATABASE_NAME = "storeDatabase.db";
    private static final int DATABASE_VERSION = 1;

    /*
    Sript para crear la BD
     */
    private static final String DATABASE_CREATE =
            "create table " + TABLE_NAME +
                    "(" +
                    COLUMN_BARCODE + "TEXT NOT NULL PRIMARY KEY," +
                    COLUMN_DESCRIPTION + "TEXT NOT NULL," +
                    COLUMN_BRAND + "TEXT NOT NULL," +
                    COLUMN_NETVALUE + "TEXT NOT NULL," +
                    COLUMN_CATEGORY + "TEXT NOT NULL," +
                    COLUMN_SUBCATEGORY + "TEXT NOT NULL," +
                    COLUMN_STOCK + "INTEGER NOT NULL," +
                    COLUMN_MINSTOCK + "INTEGER NOT NULL," +
                    COLUMN_LASTUPDATE + "DATE NOT NULL," +
                    COLUMN_CONSUMECYCLE + "INTEGER NOT NULL," +
                    COLUMN_CONSUMEUNITS + "INTEGER NOT NULL," +
                    COLUMN_SHOPPINGLISTUNITS + "INTEGER NOT NULL," +
                    COLUMN_CARTUNITS + "INTEGER NOT NULL )";

    private static final String DATABASE_DROP = "drop table if existe" + TABLE_NAME;

    public LocalDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DATABASE_DROP);
        this.onCreate(sqLiteDatabase);
    }
}
