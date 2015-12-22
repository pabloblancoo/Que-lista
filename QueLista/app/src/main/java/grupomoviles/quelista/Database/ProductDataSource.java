package grupomoviles.quelista.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Pablo on 22/12/2015.
 */
public class ProductDataSource {

    private LocalDatabase helper;
    private SQLiteDatabase database;

    private final String[] allColumns = {
            LocalDatabase.COLUMN_BARCODE,
            LocalDatabase.COLUMN_BARCODE,
            LocalDatabase.COLUMN_DESCRIPTION,
            LocalDatabase.COLUMN_BRAND,
            LocalDatabase.COLUMN_NETVALUE,
            LocalDatabase.COLUMN_CATEGORY,
            LocalDatabase.COLUMN_SUBCATEGORY,
            LocalDatabase.COLUMN_STOCK,
            LocalDatabase.COLUMN_MINSTOCK,
            LocalDatabase.COLUMN_LASTUPDATE,
            LocalDatabase.COLUMN_CONSUMECYCLE,
            LocalDatabase.COLUMN_CONSUMEUNITS,
            LocalDatabase.COLUMN_SHOPPINGLISTUNITS,
            LocalDatabase.COLUMN_CARTUNITS
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
