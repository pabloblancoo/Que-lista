package grupomoviles.quelista.igu.recyclerViewAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import grupomoviles.quelista.localDatabase.ProductDataSource;
import grupomoviles.quelista.logic.Product;

/**
 * Created by Nauce on 1/1/16.
 */
public abstract class MyAdapter extends RecyclerSwipeAdapter<MyAdapter.MyViewHolder> {

    static Context context;
    List<Product> items;
    SharedPreferences sharedPref;
    boolean miniaturasPref;

    public MyAdapter(Context context, List<Product> items) {
        this.context = context;
        this.items = Stream.of(items).sortBy(i -> i.getDescription() + i.getNetValue()).collect(Collectors.toList());

        // Procesar valores actuales de las preferencias.
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        miniaturasPref = sharedPref.getBoolean("miniaturas", true);
    }

    public void onResultProductInfoActivity(Product product) {
        items = Stream.of(items).sortBy(i -> i.getDescription() + i.getNetValue()).collect(Collectors.toList());
        notifyDataSetChanged();
    }

    public void onResultNfcActivity(Product product){
        items = Stream.of(items).sortBy(i -> i.getDescription() + i.getNetValue()).collect(Collectors.toList());
        notifyDataSetChanged();
    }

    public abstract void refresh();

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

        if(miniaturasPref) {
            viewHolder.image.setVisibility(View.VISIBLE);
            viewHolder.image.setImageBitmap(currentItem.getImage(context));
        }
        else{
            viewHolder.image.setVisibility(View.GONE);
        }

        viewHolder.description.setText(currentItem.getDescription());
        viewHolder.brand.setText(currentItem.getBrand());
        viewHolder.netValue.setText(currentItem.getNetValue());

        if (position == getItemCount() - 1)
            viewHolder.itemView.setPadding(0, 0, 0, 12);
        else
            viewHolder.itemView.setPadding(0, 0, 0, 0);

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

    public void setConMiniaturas(boolean miniaturasPref) {
        if (miniaturasPref != this.miniaturasPref) {
            this.miniaturasPref = miniaturasPref;
            // Notificar que las miniaturas fueron afectadas
            notifyDataSetChanged();
        }

    }

    public List<Product> cargarBDLocal() {
        List<Product> p = null;
        ProductDataSource database = new ProductDataSource(context);
        database.openDatabase();
        p = database.getAllProducts();
        database.close();

        //Stream.of(p).forEach(x -> Log.i("PANTRY", "PRODUCTO " + x.getCode()));

        return  p;
    }

    public void guardarDatosBDLocal(Product product) {
        ProductDataSource database = new ProductDataSource(context);
        database.openDatabase();
        if(product.getStock()==-1 && product.getShoppingListUnits()==0 && product.getCartUnits()==0) {
            database.deleteProduct(product.getCode());
        } else {
            database.insertProduct(product);
        }
        database.close();
    }
}
