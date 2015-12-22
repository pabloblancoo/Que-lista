package grupomoviles.quelista.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pablo on 21/12/2015.
 */
public class LocalDatabase extends SQLiteOpenHelper {

    /*
    Nombre de laS TABLAS
     */
    private static final String PRODUCT_TABLE_NAME = "products";
    private static final String CATEGORY_TABLE_NAME = "category";
    private static final String SUBCATEGORY_TABLE_NAME = "subcategory";

    /*
        Columnas de la TABLA PRODUCT
     */
    public static final String PRODUCT_COLUMN_BARCODE = "barcode";
    public static final String PRODUCT_COLUMN_DESCRIPTION = "description";
    public static final String PRODUCT_COLUMN_BRAND = "brand";
    public static final String PRODUCT_COLUMN_NETVALUE = "netvalue";
    public static final String PRODUCT_COLUMN_CATEGORY = "category";
    public static final String PRODUCT_COLUMN_SUBCATEGORY = "subcategory";
    public static final String PRODUCT_COLUMN_STOCK = "stock";
    public static final String PRODUCT_COLUMN_MINSTOCK = "minstock";
    public static final String PRODUCT_COLUMN_LASTUPDATE = "lastupdate";
    public static final String PRODUCT_COLUMN_CONSUMECYCLE = "consumecycle";
    public static final String PRODUCT_COLUMN_CONSUMEUNITS = "consumeunits";
    public static final String PRODUCT_COLUMN_SHOPPINGLISTUNITS = "shoppinglistunits";
    public static final String PRODUCT_COLUMN_CARTUNITS = "cartunits";


    /*
        Columnas de la tabla CATEGORY
     */
    public static final String CATEGORY_COLUMN_ID = "id";
    public static final String CATEGORY_COLUMN_NAME = "name";

    /*
        Columnas de la tabla SUBCATEGORY
     */
    public static final String SUBCATEGORY_COLUMN_ID_CATEGORY = "id_category";
    public static final String SUBCATEGORY_COLUMN_ID = "id";
    public static final String SUBCATEGORY_COLUMN_NAME = "name";

    /*
        Nombre y version de la BD
     */
    private static final String DATABASE_NAME = "storeDatabase.db";
    private static final int DATABASE_VERSION = 1;


    /*
        Crear la tabla category
     */
    private static String CATEGORY_CREATE_TABLE =
            "create table " + CATEGORY_TABLE_NAME +
            "(" +
            SUBCATEGORY_COLUMN_ID + "INTEGER NOT NULL PRIMARY KEY," +
            SUBCATEGORY_COLUMN_NAME + "TEXT NOT NULL," +
            ")";
    /*
    Crear la tabla subcategory
     */

    private static String SUBCATEGORY_CREATE_TABLE =
            "create table " + SUBCATEGORY_TABLE_NAME +
            "(" +
            SUBCATEGORY_COLUMN_ID + "INTEGER NOT NULL PRIMARY KEY," +
            SUBCATEGORY_COLUMN_ID_CATEGORY + "INTEGER NOT NULL," +
            SUBCATEGORY_COLUMN_NAME + "TEXT NOT NULL," +
            "FOREIGN KEY " + SUBCATEGORY_COLUMN_ID_CATEGORY + "REFERENCES (" + CATEGORY_TABLE_NAME + "." + CATEGORY_COLUMN_ID + ")" +
            ")";
    /*
    Crear la tabla product
     */

    private static String PRODUCT_CREATE_TABLE =
            "create table " + PRODUCT_TABLE_NAME +
            "(" +
                    PRODUCT_COLUMN_BARCODE + "TEXT NOT NULL PRIMARY KEY," +
                    PRODUCT_COLUMN_DESCRIPTION + "TEXT NOT NULL," +
                    PRODUCT_COLUMN_BRAND + "TEXT NOT NULL," +
                    PRODUCT_COLUMN_NETVALUE + "TEXT NOT NULL," +
                    PRODUCT_COLUMN_CATEGORY + "INT NOT NULL," +
                    PRODUCT_COLUMN_SUBCATEGORY + "INT NOT NULL," +
                    PRODUCT_COLUMN_STOCK + "INTEGER NOT NULL," +
                    PRODUCT_COLUMN_MINSTOCK + "INTEGER NOT NULL," +
                    PRODUCT_COLUMN_LASTUPDATE + "DATE NOT NULL," +
                    PRODUCT_COLUMN_CONSUMECYCLE + "INTEGER NOT NULL," +
                    PRODUCT_COLUMN_CONSUMEUNITS + "INTEGER NOT NULL," +
                    PRODUCT_COLUMN_SHOPPINGLISTUNITS + "INTEGER NOT NULL," +
                    PRODUCT_COLUMN_CARTUNITS + "INTEGER NOT NULL " +
            "FOREIGN KEY " + PRODUCT_COLUMN_CATEGORY + "REFERENCES (" + SUBCATEGORY_TABLE_NAME + "." + SUBCATEGORY_COLUMN_ID_CATEGORY + ")" +
            "FOREIGN KEY " + PRODUCT_COLUMN_SUBCATEGORY + "REFERENCES (" + SUBCATEGORY_TABLE_NAME + "." + SUBCATEGORY_COLUMN_ID + ")" +
            ")";

    private static final String DATABASE_DROP = "drop table if existe" + PRODUCT_TABLE_NAME;
    /*
        Sript para crear la BD
         */
    private static final String DATABASE_CREATE = CATEGORY_CREATE_TABLE + "\n" + SUBCATEGORY_CREATE_TABLE + "\n" + PRODUCT_CREATE_TABLE;

    public LocalDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    /**
     * Metodo que crea la BD
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    /**
     * Metodo que actualiza la BD
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DATABASE_DROP);
        this.onCreate(sqLiteDatabase);
    }
}
