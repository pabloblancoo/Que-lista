package grupomoviles.quelista;

import android.os.AsyncTask;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import grupomoviles.quelista.onlineDatabase.GestorBD;

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
