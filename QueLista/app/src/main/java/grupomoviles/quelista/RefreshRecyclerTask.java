package grupomoviles.quelista;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nauce on 24/12/15.
 */
public class RefreshRecyclerTask extends AsyncTask<SimpleAdapter, Void, List<Product>> {

    SimpleAdapter adapter;

    @Override
    protected List<Product> doInBackground(SimpleAdapter... simpleAdapters) {
        List<Product> products = new ArrayList<>();

        products.add(new Product("0", "Cereales Miel Pops", "Kellogg's", "Caja de 375 g", "Categoria", "Subcategoria"));
        products.add(new Product("1", "Cereales Miel Pops", "Kellogg's", "Caja de 375 g", "Categoria", "Subcategoria"));
        products.add(new Product("2", "Cereales Miel Pops", "Kellogg's", "Caja de 375 g", "Categoria", "Subcategoria"));
        products.add(new Product("3", "Cereales Miel Pops", "Kellogg's", "Caja de 375 g", "Categoria", "Subcategoria"));
        products.add(new Product("4", "Cereales Miel Pops", "Kellogg's", "Caja de 375 g", "Categoria", "Subcategoria"));
        adapter = simpleAdapters[0];

        return products;
    }

    @Override
    protected void onPostExecute(List<Product> products) {
        adapter.swipeList(products);
    }
}
