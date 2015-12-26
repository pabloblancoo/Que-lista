package grupomoviles.quelista.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import grupomoviles.quelista.Product;

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
            LocalDatabase.PRODUCT_COLUMN_CATEGORY,
            LocalDatabase.PRODUCT_COLUMN_SUBCATEGORY,
            LocalDatabase.PRODUCT_COLUMN_STOCK,
            LocalDatabase.PRODUCT_COLUMN_MINSTOCK,
            LocalDatabase.PRODUCT_COLUMN_LASTUPDATE,
            LocalDatabase.PRODUCT_COLUMN_CONSUMECYCLE,
            LocalDatabase.PRODUCT_COLUMN_CONSUMEUNITS,
            LocalDatabase.PRODUCT_COLUMN_SHOPPINGLISTUNITS,
            LocalDatabase.PRODUCT_COLUMN_CARTUNITS
    };

    public ProductDataSource(Context context) {
        helper = new LocalDatabase(context, null, null, 0);
    }

    public void openDatabase() {
        database = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    /**
     * Añadir un producto a la base de datos
     *
     * @param product
     * @return
     */
    public void insertProduct(Product product) {

        final ContentValues values = new ContentValues();
        values.put(LocalDatabase.PRODUCT_COLUMN_BARCODE, product.getCode());

        values.put(LocalDatabase.PRODUCT_COLUMN_DESCRIPTION, product.getDescription());
        values.put(LocalDatabase.PRODUCT_COLUMN_BRAND, product.getBrand());
        values.put(LocalDatabase.PRODUCT_COLUMN_NETVALUE, product.getNetValue());
        values.put(LocalDatabase.PRODUCT_COLUMN_CATEGORY, product.getCategory());
        values.put(LocalDatabase.PRODUCT_COLUMN_SUBCATEGORY, product.getSubcategory());
        values.put(LocalDatabase.PRODUCT_COLUMN_STOCK, product.getStock());
        values.put(LocalDatabase.PRODUCT_COLUMN_MINSTOCK, product.getMinStock());
        //values.put(LocalDatabase.PRODUCT_COLUMN_LASTUPDATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(product.getLastUpdate()));
        values.put(LocalDatabase.PRODUCT_COLUMN_LASTUPDATE, new String(""));

        values.put(LocalDatabase.PRODUCT_COLUMN_CONSUMECYCLE, product.getConsumeCycle());
        values.put(LocalDatabase.PRODUCT_COLUMN_CONSUMEUNITS, product.getConsumeUnits());
        values.put(LocalDatabase.PRODUCT_COLUMN_SHOPPINGLISTUNITS, product.getShoppingListUnits());
        values.put(LocalDatabase.PRODUCT_COLUMN_CARTUNITS, product.getCartUnits());

        // Insertamos la valoracion
        try {
            database.insertOrThrow(LocalDatabase.PRODUCT_TABLE_NAME, null, values);
        } catch (SQLiteConstraintException e) {
            System.out.println("Ya esta insertado el elemento con barcode: " + product.getCode());
        }
    }


    /**
     * Elimina un producto de la BD local
     * @param barcode Codigo del elemento a borrar
     */
    public void deleteProduct(String barcode) {
        database.delete(LocalDatabase.PRODUCT_TABLE_NAME, LocalDatabase.PRODUCT_COLUMN_BARCODE + "=" + barcode, null);

    }

    public void update(Product product){
        final ContentValues values = new ContentValues();
        values.put(LocalDatabase.PRODUCT_COLUMN_BARCODE, product.getCode());

        values.put(LocalDatabase.PRODUCT_COLUMN_DESCRIPTION, product.getDescription());
        values.put(LocalDatabase.PRODUCT_COLUMN_BRAND, product.getBrand());
        values.put(LocalDatabase.PRODUCT_COLUMN_NETVALUE, product.getNetValue());
        values.put(LocalDatabase.PRODUCT_COLUMN_CATEGORY, product.getCategory());
        values.put(LocalDatabase.PRODUCT_COLUMN_SUBCATEGORY, product.getSubcategory());
        values.put(LocalDatabase.PRODUCT_COLUMN_STOCK, product.getStock());
        values.put(LocalDatabase.PRODUCT_COLUMN_MINSTOCK, product.getMinStock());
        //values.put(LocalDatabase.PRODUCT_COLUMN_LASTUPDATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(product.getLastUpdate()));
        values.put(LocalDatabase.PRODUCT_COLUMN_LASTUPDATE, new String(""));

        values.put(LocalDatabase.PRODUCT_COLUMN_CONSUMECYCLE, product.getConsumeCycle());
        values.put(LocalDatabase.PRODUCT_COLUMN_CONSUMEUNITS, product.getConsumeUnits());
        values.put(LocalDatabase.PRODUCT_COLUMN_SHOPPINGLISTUNITS, product.getShoppingListUnits());
        values.put(LocalDatabase.PRODUCT_COLUMN_CARTUNITS, product.getCartUnits());

        database.update(LocalDatabase.PRODUCT_TABLE_NAME,values,LocalDatabase.PRODUCT_COLUMN_BARCODE + "=" + product.getCode(),null);
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
            //String code, String description, String brand, String netValue, String category, String subcategory
            final Product product = new Product(cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.PRODUCT_COLUMN_BARCODE)), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));

            valorationList.add(product);
            cursor.moveToNext();
        }

        cursor.close();
        // Una vez obtenidos todos los datos y cerrado el cursor, devolvemos la
        // lista.
        return valorationList;
    }

}
