package grupomoviles.quelista.onlineDatabase;

import android.os.AsyncTask;

import com.annimon.stream.Stream;
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

        BasicDBList list =  new GetMongoLab(qb.findPostURL(qb.findProducts(barcodes))).peticion();

        if (list == null)
            return null;

        List<Product> products = new ArrayList<>();

        Stream.of(list).forEach(o -> {
            products.add(new Product(((DBObject) o).get("codigo").toString(),
                    ((DBObject) o).get("descripcion").toString(),
                    ((DBObject) o).get("marca").toString(),
                    ((DBObject) o).get("cantidadneta").toString(),
                    ((DBObject) o).get("categoria").toString(),
                    ((DBObject) o).get("subcategoria") != null ? ((DBObject) o).get("subcategoria").toString():"",
                    ((DBObject) o).get("unidades") != null ? Integer.parseInt(((DBObject) o).get("unidades").toString()):1
                    ));
        });

        return products;
    }


}
