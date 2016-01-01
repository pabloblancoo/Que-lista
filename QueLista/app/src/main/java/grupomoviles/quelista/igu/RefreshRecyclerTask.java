package grupomoviles.quelista.igu;

import android.os.AsyncTask;

import java.util.List;

import grupomoviles.quelista.igu.recyclerViewAdapters.MyAdapter;
import grupomoviles.quelista.igu.recyclerViewAdapters.PantryAdapter;
import grupomoviles.quelista.logic.Product;

/**
 * Created by Nauce on 24/12/15.
 */
public class RefreshRecyclerTask extends AsyncTask<MyAdapter, Void, List<Product>> {

    MyAdapter adapter;

    @Override
    protected List<Product> doInBackground(MyAdapter... adapters) {

        adapter = adapters[0];

        return null;
    }

    @Override
    protected void onPostExecute(List<Product> products) {
        adapter.notifyDataSetChanged();
    }
}
