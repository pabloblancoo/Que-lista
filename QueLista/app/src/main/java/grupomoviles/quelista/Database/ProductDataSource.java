package grupomoviles.quelista.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

    public long insertProduct(final Product product) {

        final ContentValues values = new ContentValues();
        values.put(LocalDatabase.PRODUCT_COLUMN_BARCODE, product.getCode());

        values.put(LocalDatabase.PRODUCT_COLUMN_DESCRIPTION, product.getDescription());
        values.put(LocalDatabase.PRODUCT_COLUMN_BRAND, product.getBrand());
        values.put(LocalDatabase.PRODUCT_COLUMN_NETVALUE, product.getNetValue());
        values.put(LocalDatabase.PRODUCT_COLUMN_CATEGORY, product.getCategory());
        values.put(LocalDatabase.PRODUCT_COLUMN_SUBCATEGORY, product.getSubcategory());
        values.put(LocalDatabase.PRODUCT_COLUMN_STOCK, product.getStock());
        values.put(LocalDatabase.PRODUCT_COLUMN_MINSTOCK, product.getMinStock());
        values.put(LocalDatabase.PRODUCT_COLUMN_LASTUPDATE, product.getLastUpdate());
        values.put(LocalDatabase.PRODUCT_COLUMN_CONSUMECYCLE, product.getConsumeCycle());
        values.put(LocalDatabase.PRODUCT_COLUMN_CONSUMEUNITS, product.getConsumeUnits());
        values.put(LocalDatabase.PRODUCT_COLUMN_SHOPPINGLISTUNITS, product.getShoppingListUnits());
        values.put(LocalDatabase.PRODUCT_COLUMN_CARTUNITS, product.getCartUnits());

        // Insertamos la valoracion
        try {
            database.insert(LocalDatabase.PRODUCT_TABLE_NAME, null, values);
        } catch (Exception e) {
            return -1;
        }
        return 1;
    }


}
