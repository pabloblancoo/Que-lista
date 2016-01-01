package grupomoviles.quelista.igu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.ExecutionException;

import grupomoviles.quelista.R;
import grupomoviles.quelista.igu.recyclerViewAdapters.MyAdapter;
import grupomoviles.quelista.igu.recyclerViewAdapters.PantryAdapter;
import grupomoviles.quelista.igu.recyclerViewAdapters.ShoppingListAdapter;
import grupomoviles.quelista.logic.Product;
import grupomoviles.quelista.logic.ShoppingList;
import grupomoviles.quelista.onlineDatabase.GestorBD;

/**
 * Created by Alperi on 30/12/2015
 */

public class ListaCompraFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_compra, container, false);

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

        Product product = null;

        try {
            product = GestorBD.FindProduct("5449000000996");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ShoppingList s = new ShoppingList();

        s.getProducts().add(product);

        recycler.setAdapter(new ShoppingListAdapter(getActivity(), s));

        return view;
    }


}
