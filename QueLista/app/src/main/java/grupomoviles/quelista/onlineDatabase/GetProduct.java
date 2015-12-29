package grupomoviles.quelista.onlineDatabase;

import android.os.AsyncTask;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

import grupomoviles.quelista.logic.Product;

/**
 * Created by Nauce on 18/11/15.
 */
public class GetProduct extends AsyncTask<String, Void, Product> {
    private static final String DOCUMENT = "productos";

    @Override
    protected Product doInBackground(String... codigo) {
        QueryBuilder qb = new QueryBuilder(DOCUMENT);

        BasicDBList list =  new GetMongoLab(qb.findPostURL(qb.findProduct(codigo[0]))).peticion();

        if (list == null)
            return null;

        Product p = null;
        int unidades = 1;
        String subcategoria = "";

        if (list.size() > 0) {
            DBObject o = (DBObject) list.get(0);
            if (o.get("unidades") != null)
                unidades = Integer.parseInt(o.get("unidades").toString());

            if (o.get("subcategoria") != null)
                subcategoria =  o.get("subcategoria").toString();

            p = new Product(o.get("codigo").toString(),
                    o.get("descripcion").toString(),
                    o.get("marca").toString(),
                    o.get("cantidadneta").toString(),
                    o.get("categoria").toString(), subcategoria, unidades);
        }

        return p;
    }
}
