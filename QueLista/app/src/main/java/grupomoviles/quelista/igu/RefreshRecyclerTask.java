package grupomoviles.quelista.igu;

import android.os.AsyncTask;

import java.util.List;

import grupomoviles.quelista.logic.Product;

/**
 * Created by Nauce on 24/12/15.
 */
public class RefreshRecyclerTask extends AsyncTask<SimpleAdapter, Void, List<Product>> {

    SimpleAdapter adapter;

    @Override
    protected List<Product> doInBackground(SimpleAdapter... simpleAdapters) {

        adapter = simpleAdapters[0];

        return null;
    }

    @Override
    protected void onPostExecute(List<Product> products) {
        adapter.notifyDataSetChanged();
    }
}
