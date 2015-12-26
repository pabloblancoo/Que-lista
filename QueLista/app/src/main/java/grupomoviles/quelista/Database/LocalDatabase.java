package grupomoviles.quelista.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import grupomoviles.quelista.MainActivity;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by Pablo on 21/12/2015.
 */
public class LocalDatabase extends SQLiteOpenHelper {

    /*
    Nombre de laS TABLAS
     */
    public static final String PRODUCT_TABLE_NAME = "t_products";
    public  static final String CATEGORY_TABLE_NAME = "t_category";
    public  static final String SUBCATEGORY_TABLE_NAME = "t_subcategory";
    public  static final String BRAND_TABLE_NAME = "t_brand";

    /*
        Columnas de la TABLA PRODUCT
     */
    public static final String PRODUCT_COLUMN_BARCODE = "barcode";
    public static final String PRODUCT_COLUMN_DESCRIPTION = "description";
    public static final String PRODUCT_COLUMN_BRAND = "brand";
    public static final String PRODUCT_COLUMN_NETVALUE = "netvalue";
    public static final String PRODUCT_COLUMN_UNITS = "units";
    public static final String PRODUCT_COLUMN_CATEGORY = "IDcategory";
    public static final String PRODUCT_COLUMN_SUBCATEGORY = "IDsubcategory";
    public static final String PRODUCT_COLUMN_STOCK = "stock";
    public static final String PRODUCT_COLUMN_MINSTOCK = "minstock";
    public static final String PRODUCT_COLUMN_UNITSTOADD = "unitstoadd";
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
        Columnas de la tabla CATEGORY
    */
    public static final String BRAND_COLUMN_ID = "id";
    public static final String BRAND_COLUMN_NAME = "name";
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
    private static final int DATABASE_VERSION = 8;


    /*
        Crear la tabla brand
     */
    private static String BRAND_CREATE_TABLE =
            "create table if not exists " + BRAND_TABLE_NAME +
                    "(" +
                    BRAND_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    BRAND_COLUMN_NAME + " TEXT NOT NULL " +
                    ")";
    /*

     */
    /*
        Crear la tabla category
     */
    private static String CATEGORY_CREATE_TABLE =
            "create table if not exists " + CATEGORY_TABLE_NAME +
            "(" +
            CATEGORY_COLUMN_ID + " INTEGER PRIMARY KEY, " +
            CATEGORY_COLUMN_NAME + " TEXT NOT NULL " +
            ")";
    /*
    Crear la tabla subcategory
     */

    private static String SUBCATEGORY_CREATE_TABLE =
            "create table if not exists " + SUBCATEGORY_TABLE_NAME +
            "(" +
            SUBCATEGORY_COLUMN_ID + " INTEGER PRIMARY KEY, " +
            SUBCATEGORY_COLUMN_ID_CATEGORY + " INTEGER NOT NULL, " +
            SUBCATEGORY_COLUMN_NAME + " TEXT NOT NULL, " +
            " FOREIGN KEY (" + SUBCATEGORY_COLUMN_ID_CATEGORY + ") REFERENCES " + CATEGORY_TABLE_NAME + "(" + CATEGORY_COLUMN_ID + ") " +
            ")";
    /*
    Crear la tabla product
     */

    private static String PRODUCT_CREATE_TABLE =
            "create table if not exists " + PRODUCT_TABLE_NAME +
            " (" +
                    PRODUCT_COLUMN_BARCODE + " TEXT PRIMARY KEY," +
                    PRODUCT_COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                    PRODUCT_COLUMN_BRAND + " INTEGER NOT NULL," +
                    PRODUCT_COLUMN_NETVALUE + " TEXT NOT NULL," +
                    PRODUCT_COLUMN_UNITS + " INTEGER NOT NULL," +
                    PRODUCT_COLUMN_CATEGORY + " INTEGER NOT NULL," +
                    PRODUCT_COLUMN_SUBCATEGORY + " INTEGER NOT NULL," +
                    PRODUCT_COLUMN_STOCK + " INTEGER NOT NULL," +
                    PRODUCT_COLUMN_MINSTOCK + " INTEGER NOT NULL," +
                    PRODUCT_COLUMN_UNITSTOADD + " INTEGER NOT NULL," +
                    PRODUCT_COLUMN_LASTUPDATE + " DATE," +
                    PRODUCT_COLUMN_CONSUMECYCLE + " INTEGER NOT NULL," +
                    PRODUCT_COLUMN_CONSUMEUNITS + " INTEGER NOT NULL," +
                    PRODUCT_COLUMN_SHOPPINGLISTUNITS + " INTEGER NOT NULL," +
                    PRODUCT_COLUMN_CARTUNITS + " INTEGER NOT NULL, " +
                    "FOREIGN KEY (" + PRODUCT_COLUMN_BRAND + ") REFERENCES " + BRAND_TABLE_NAME + "(" + BRAND_COLUMN_ID + "), " +
                    "FOREIGN KEY (" + PRODUCT_COLUMN_CATEGORY + ") REFERENCES " + SUBCATEGORY_TABLE_NAME + "(" + SUBCATEGORY_COLUMN_ID_CATEGORY + "), " +
                    "FOREIGN KEY (" + PRODUCT_COLUMN_SUBCATEGORY + ") REFERENCES " + SUBCATEGORY_TABLE_NAME + "(" + SUBCATEGORY_COLUMN_ID + ") " +
                    ")";

    private static final String DATABASE_DROP = "drop table if exists " + PRODUCT_TABLE_NAME;
    /*
        Sript para crear la BD
         */
    private static final String DATABASE_CREATE = BRAND_CREATE_TABLE + "\n" + CATEGORY_CREATE_TABLE + "\n" + SUBCATEGORY_CREATE_TABLE + "\n" + PRODUCT_CREATE_TABLE;

    public LocalDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    /**
     * Metodo que crea la BD
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //sqLiteDatabase.execSQL(DATABASE_CREATE);
        sqLiteDatabase.execSQL(BRAND_CREATE_TABLE);
        sqLiteDatabase.execSQL(CATEGORY_CREATE_TABLE);
        sqLiteDatabase.execSQL(SUBCATEGORY_CREATE_TABLE);
        sqLiteDatabase.execSQL(PRODUCT_CREATE_TABLE);


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
