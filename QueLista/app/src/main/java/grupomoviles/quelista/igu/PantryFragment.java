package grupomoviles.quelista.igu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;

import java.util.List;
import java.util.concurrent.ExecutionException;

import grupomoviles.quelista.R;
import grupomoviles.quelista.igu.recyclerViewAdapters.MyAdapter;
import grupomoviles.quelista.logic.DownloadImageTask;
import grupomoviles.quelista.logic.Product;
import grupomoviles.quelista.onlineDatabase.GestorBD;

/**
 * Created by Nauce on 26/12/15.
 */
public class PantryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pantry, container, false);

        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.recyclerView);
        recycler.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        refreshLayout.setColorSchemeResources(R.color.color_rojo_app);

        // Iniciar la tarea asíncrona al revelar el indicador
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new RefreshRecyclerTask().execute((MyAdapter) recycler.getAdapter());
                        refreshLayout.setRefreshing(false);
                    }
                }
        );

        if(((MainActivity) getActivity()).getPantryAdapter().getPantry().getProducts().size() == 0
                && ((MainActivity) getActivity()).getShoppingListAdapter().getShoppingList().getProducts().size() == 0
                && ((MainActivity) getActivity()).getCartAdapter().getCart().getProducts().size() == 0) {
            Log.i("VACIA", "LO ESTA");
            List<Product> products = null;

            if(products !=  null) {
                Stream.of(products).forEach(p -> {
                    ((MainActivity) getActivity()).getPantryAdapter().onResultProductInfoActivity(p);
                    ((MainActivity) getActivity()).getPantryAdapter().guardarDatosBDLocal(p);
                    ((MainActivity) getActivity()).getPantryAdapter().notifyDataSetChanged();
                });
            }
        }

        recycler.setAdapter(((MainActivity) getActivity()).getPantryAdapter());
        ((MainActivity) getActivity()).getPantryAdapter().swipeList();

        return view;
    }
}
