package grupomoviles.quelista.onlineDatabase;

import android.os.AsyncTask;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;

import grupomoviles.quelista.logic.Product;

/**
 * Created by Nauce on 18/11/15.
 */
public class GetProducts extends AsyncTask<String, Void, List<Product>> {
    private static final String DOCUMENT = "productos";

    @Override
    protected List<Product> doInBackground(String... barcodes) {
        QueryBuilder qb = new QueryBuilder(DOCUMENT);

        BasicDBList list =  new GetMongoLab(qb.findPostURL(qb.findAllProducts())).peticion();

        if (list == null)
            return null;

        List<Product> products = new ArrayList<>();

        int unidades = 1;
        String subcategoria = "";

        for (String barcode:barcodes) {
            for (int i = 0; i < list.size(); i++) {
                if (((DBObject) list.get(i)).get("codigo").toString().equals(barcode)) {
                    if (((DBObject) list.get(i)).get("unidades") != null)
                        unidades = Integer.parseInt(((DBObject) list.get(i)).get("unidades").toString());

                    if (((DBObject) list.get(i)).get("subcategoria") != null)
                        subcategoria = ((DBObject) list.get(i)).get("subcategoria").toString();

                    products.add(new Product(((DBObject) list.get(i)).get("codigo").toString(),
                            ((DBObject) list.get(i)).get("descripcion").toString(),
                            ((DBObject) list.get(i)).get("marca").toString(),
                            ((DBObject) list.get(i)).get("cantidadneta").toString(),
                            ((DBObject) list.get(i)).get("categoria").toString(), subcategoria, unidades));
                    list.remove(i);
                    break;
                }
            }
        }

        return products;
    }
}
