package grupomoviles.quelista.igu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import grupomoviles.quelista.R;
import grupomoviles.quelista.igu.recyclerViewAdapters.MyAdapter;
import grupomoviles.quelista.logic.Product;
import grupomoviles.quelista.onlineDatabase.GestorBD;
import grupomoviles.quelista.onlineDatabase.GetProducts;

public class FragmentTicket extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);


        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.recyclerView);
        recycler.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        Bundle bundle = getArguments();
        ArrayList lineas = (ArrayList) bundle.get(ScanNFCActivity.BUFFERED);
        int firstProduct = 1;
        List<Product> products = new ArrayList<Product>();
        for (int i = firstProduct; i< lineas.size()-1; i++){
            try {
                String[] line = lineas.get(i).toString().split(";");
                Product p = GestorBD.FindProduct( line[0]);
                p.setStock(Integer.parseInt(line[1]));
                products.add(p);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Stream.of(products).forEach(p -> ((ScanNFCActivity) getActivity()).getTicketAdapter().getTicket().getProducts().put(p.getCode(), p));

        recycler.setAdapter(((ScanNFCActivity) getActivity()).getTicketAdapter());
        ((ScanNFCActivity) getActivity()).getTicketAdapter().swipeList();


        return view;
    }
}
