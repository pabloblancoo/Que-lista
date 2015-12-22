package grupomoviles.quelista.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Pablo on 22/12/2015.
 */
public class ProductDataSource {

    //NO TOCAR AUN, FALTA POR HACER
    private LocalDatabase helper;
    private SQLiteDatabase database;

    private final String[] allColumns = {
            LocalDatabase.PRODUCT_COLUMN_BARCODE,
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


}
