package grupomoviles.quelista.igu.recyclerViewAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.daimajia.androidviewhover.BlurLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

import grupomoviles.quelista.R;
import grupomoviles.quelista.igu.ProductInfoActivity;
import grupomoviles.quelista.logic.Product;

/**
 * Created by Nauce on 1/1/16.
 */
public abstract class MyAdapter extends RecyclerSwipeAdapter<MyAdapter.MyViewHolder> {

    static Context context;
    List<Product> items;

    public MyAdapter(Context context, List<Product> items) {
        this.context = context;
        this.items = Stream.of(items).sortBy(i -> i.getDescription().charAt(0)).collect(Collectors.toList());
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    public void onResultProductInfoActivity(Product product) {
        items = Stream.of(items).sortBy(i -> i.getDescription().charAt(0)).collect(Collectors.toList());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipeLayout;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        Product currentItem = items.get(position);

        viewHolder.blurLayout.dismissHover();
        viewHolder.product = currentItem;
        viewHolder.image.setImageBitmap(currentItem.getImage(context));
        viewHolder.description.setText(currentItem.getDescription());
        viewHolder.brand.setText(currentItem.getBrand());
        viewHolder.netValue.setText(currentItem.getNetValue());

        mItemManger.bindView(viewHolder.itemView, position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Product product;
        ImageView image;
        TextView description;
        TextView brand;
        TextView netValue;
        TextView units;
        MyAdapter adapter;
        BlurLayout blurLayout;

        public MyViewHolder(View v, MyAdapter adapter, View hover) {
            super(v);

            image = (ImageView) v.findViewById(R.id.imgProduct);
            description = (TextView) v.findViewById(R.id.txDescription);
            brand = (TextView) v.findViewById(R.id.txBrand);
            netValue = (TextView) v.findViewById(R.id.txNetValue);
            units = (TextView) v.findViewById(R.id.txUnits);

            blurLayout = (BlurLayout) v.findViewById(R.id.blurLayout);
            v.findViewById(R.id.btnPlusStock).setOnClickListener(this);
            v.findViewById(R.id.btnMinusStock).setOnClickListener(this);
            v.findViewById(R.id.btnDelete).setOnClickListener(this);

            hover.findViewById(R.id.btnMore).setOnClickListener(this);

            this.adapter = adapter;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btnMore) {
                Intent i = new Intent(context, ProductInfoActivity.class);
                i.putExtra(ProductInfoActivity.PRODUCT, product);
                ((Activity)context).startActivityForResult(i, ProductInfoActivity.REQUEST_CODE);
            }
        }
    }
}
