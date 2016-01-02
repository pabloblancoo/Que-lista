package grupomoviles.quelista.igu;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;

import java.util.List;
import java.util.concurrent.ExecutionException;

import grupomoviles.quelista.R;
import grupomoviles.quelista.igu.recyclerViewAdapters.MyAdapter;
import grupomoviles.quelista.igu.recyclerViewAdapters.PantryAdapter;
import grupomoviles.quelista.logic.Pantry;
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

        List<Product> products = null;

        try {
            products = GestorBD.FindProducts("5449000000996", "8410297112041", "8410297170058",
                    "8410188012092", "5449000009067", "8410000826937", "8410014307682", "8410014312495", "5000127281752");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Stream.of(products).forEach(p -> ((MainActivity) getActivity()).pantryAdapter.getPantry().getProducts().add(p));

        recycler.setAdapter(((MainActivity) getActivity()).pantryAdapter);
        ((MainActivity) getActivity()).pantryAdapter.swipeList();

        return view;
    }
}
