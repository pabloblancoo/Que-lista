package grupomoviles.quelista.localDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import grupomoviles.quelista.logic.Product;

/**
 * Created by Pablo on 22/12/2015.
 */
public class ProductDataSource {

    //NO TOCAR AUN, FALTA POR HACER
    private LocalDatabase helper;
    private SQLiteDatabase database;

    private final String[] allColumns = {
            LocalDatabase.PRODUCT_COLUMN_BARCODE,
            LocalDatabase.PRODUCT_COLUMN_DESCRIPTION,
            LocalDatabase.PRODUCT_COLUMN_BRAND,
            LocalDatabase.PRODUCT_COLUMN_NETVALUE,
            LocalDatabase.PRODUCT_COLUMN_UNITS,
            LocalDatabase.PRODUCT_COLUMN_CATEGORY,
            LocalDatabase.PRODUCT_COLUMN_SUBCATEGORY,
            LocalDatabase.PRODUCT_COLUMN_STOCK,
            LocalDatabase.PRODUCT_COLUMN_MINSTOCK,
            LocalDatabase.PRODUCT_COLUMN_UNITSTOADD,
            LocalDatabase.PRODUCT_COLUMN_LASTUPDATE,
            LocalDatabase.PRODUCT_COLUMN_CONSUMECYCLE,
            LocalDatabase.PRODUCT_COLUMN_CONSUMEUNITS,
            LocalDatabase.PRODUCT_COLUMN_SHOPPINGLISTUNITS,
            LocalDatabase.PRODUCT_COLUMN_CARTUNITS
    };

    /**
     * Constructor
     * @param context
     */
    public ProductDataSource(Context context) {
        helper = new LocalDatabase(context, null, null, 0);
    }

    /**
     * Abre la conexion a la base de datos local
     */
    public void openDatabase() {
        database = helper.getWritableDatabase();
    }

    /**
     * Cierra la conexion a la base de datos local
     */
    public void close() {
        helper.close();
    }

    /**
     * Añadir un producto a la base de datos local, si ya existe, lo actualiza
     *
     * @param product
     */
    public void insertProduct(Product product) {
        ContentValues values = getContentValues(product);

        // Insertamos la valoracion
        try {
            database.insertOrThrow(LocalDatabase.PRODUCT_TABLE_NAME, null, values);
        } catch (SQLiteConstraintException e) {
            update(product);
        }
    }

    /**
     * Buscamos un producto de la base de datos local
     * @param barcode Codigo del elemento a buscar
     */
    public int findProduct(String barcode) {
        String[] campos = new String[]{LocalDatabase.PRODUCT_COLUMN_BARCODE};
        Cursor c = database.query(LocalDatabase.PRODUCT_TABLE_NAME,
                campos,
                LocalDatabase.PRODUCT_COLUMN_BARCODE + "=" + barcode, null, null, null, null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            return 1;
        } else {
            return 0;
        }
    }


    /**
     * Elimina un producto de la base de datos local
     * @param barcode Codigo del elemento a borrar
     */
    public void deleteProduct(String barcode) {
        database.delete(LocalDatabase.PRODUCT_TABLE_NAME, LocalDatabase.PRODUCT_COLUMN_BARCODE + "=" + barcode, null);

    }

    /**
     * Actualiza un producto en la base de datos local
     * @param product
     */
    public void update(Product product){
        ContentValues values = getContentValues(product);

        database.update(LocalDatabase.PRODUCT_TABLE_NAME, values, LocalDatabase.PRODUCT_COLUMN_BARCODE + "=" + product.getCode(), null);
    }

    @NonNull
    private ContentValues getContentValues(Product product) {
        ContentValues values = new ContentValues();
        values.put(LocalDatabase.PRODUCT_COLUMN_BARCODE, product.getCode());
        values.put(LocalDatabase.PRODUCT_COLUMN_DESCRIPTION, product.getDescription());
        values.put(LocalDatabase.PRODUCT_COLUMN_BRAND, product.getBrand());
        values.put(LocalDatabase.PRODUCT_COLUMN_NETVALUE, product.getNetValue());
        values.put(LocalDatabase.PRODUCT_COLUMN_UNITS, product.getUnits());
        values.put(LocalDatabase.PRODUCT_COLUMN_CATEGORY, product.getCategory());
        values.put(LocalDatabase.PRODUCT_COLUMN_SUBCATEGORY, product.getSubcategory());
        values.put(LocalDatabase.PRODUCT_COLUMN_STOCK, product.getStock());
        values.put(LocalDatabase.PRODUCT_COLUMN_MINSTOCK, product.getMinStock());
        values.put(LocalDatabase.PRODUCT_COLUMN_UNITSTOADD, product.getUnitsToAdd());

        if(product.getLastUpdate() == null)
            values.put(LocalDatabase.PRODUCT_COLUMN_LASTUPDATE, (byte[]) null);
        else
            values.put(LocalDatabase.PRODUCT_COLUMN_LASTUPDATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(product.getLastUpdate()));

        values.put(LocalDatabase.PRODUCT_COLUMN_CONSUMECYCLE, product.getConsumeCycle());
        values.put(LocalDatabase.PRODUCT_COLUMN_CONSUMEUNITS, product.getConsumeUnits());
        values.put(LocalDatabase.PRODUCT_COLUMN_SHOPPINGLISTUNITS, product.getShoppingListUnits());
        values.put(LocalDatabase.PRODUCT_COLUMN_CARTUNITS, product.getCartUnits());
        return values;
    }

    /**
     * Obtiene todas las valoraciones andadidas por los usuarios.
     *
     * @return Lista de objetos de tipo Product
     */
    public List<Product> getAllProducts() {
        // Lista que almacenara el resultado
        List<Product> valorationList = new ArrayList<Product>();
        Cursor cursor = database.query(LocalDatabase.PRODUCT_TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Product product = null;
            try {
                if(cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_LASTUPDATE)) == null || cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_LASTUPDATE)) == "")
                    product = getProductWithoutDate(cursor);
                else
                    product = getProductWithDate(cursor);
            } catch (ParseException e) {
                System.out.println("Error en la fecha");
                Log.e("LOCALDATABASE", "Error en la fecha");
            }




            valorationList.add(product);
            cursor.moveToNext();
        }

        cursor.close();
        // Una vez obtenidos todos los datos y cerrado el cursor, devolvemos la
        // lista.
        return valorationList;
    }

    private Product getProductWithoutDate(Cursor cursor) {
        return new Product(
                cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_BARCODE)),
                cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_BRAND)),
                cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_NETVALUE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_UNITS)),
                cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_CATEGORY)),
                cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_SUBCATEGORY)),
                cursor.getInt(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_STOCK)),
                cursor.getInt(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_MINSTOCK)),
                cursor.getInt(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_UNITSTOADD)),
                null, //Valor de la fecha
                cursor.getInt(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_CONSUMECYCLE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_CONSUMEUNITS)),
                cursor.getInt(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_SHOPPINGLISTUNITS)),
                cursor.getInt(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_CARTUNITS)));

    }

    @NonNull
    private Product getProductWithDate(Cursor cursor) throws ParseException {
        return new Product(
                cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_BARCODE)),
                cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_BRAND)),
                cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_NETVALUE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_UNITS)),
                cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_CATEGORY)),
                cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_SUBCATEGORY)),
                cursor.getInt(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_STOCK)),
                cursor.getInt(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_MINSTOCK)),
                cursor.getInt(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_UNITSTOADD)),
                new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_LASTUPDATE))),
                cursor.getInt(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_CONSUMECYCLE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_CONSUMEUNITS)),
                cursor.getInt(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_SHOPPINGLISTUNITS)),
                cursor.getInt(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_CARTUNITS)));
    }

}
