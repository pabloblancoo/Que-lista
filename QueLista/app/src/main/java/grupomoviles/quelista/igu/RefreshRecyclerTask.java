package grupomoviles.quelista.igu;

import android.os.AsyncTask;

import java.util.List;

import grupomoviles.quelista.logic.Product;

/**
 * Created by Nauce on 24/12/15.
 */
public class RefreshRecyclerTask extends AsyncTask<PantryAdapter, Void, List<Product>> {

    PantryAdapter adapter;

    @Override
    protected List<Product> doInBackground(PantryAdapter... pantryAdapters) {

        adapter = pantryAdapters[0];

        return null;
    }

    @Override
    protected void onPostExecute(List<Product> products) {
        adapter.notifyDataSetChanged();
    }
}
