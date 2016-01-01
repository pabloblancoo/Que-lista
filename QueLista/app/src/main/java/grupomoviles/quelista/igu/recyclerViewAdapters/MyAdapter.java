package grupomoviles.quelista.igu.recyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

import grupomoviles.quelista.logic.Product;

/**
 * Created by Nauce on 1/1/16.
 */
public abstract class MyAdapter extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {

    static Context context;
    List<Product> items;

    public MyAdapter(Context context) {
        this.context = context;
    }

    public void refresh() {

    }
}
