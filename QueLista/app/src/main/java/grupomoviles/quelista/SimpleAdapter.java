package grupomoviles.quelista;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder>
        implements ItemClickListener {

    private final Context context;
    private List<Product> items;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Product product;
        private ImageView image;
        private TextView descripction;
        private TextView brand;
        private TextView netValue;
        private TextView units;
        private SimpleAdapter simpleAdapter;

        public SimpleViewHolder(View v, SimpleAdapter simpleAdapter) {
            super(v);

            image = (ImageView) v.findViewById(R.id.imageView);
            descripction = (TextView) v.findViewById(R.id.txDescription);
            brand = (TextView) v.findViewById(R.id.txBrand);
            netValue = (TextView) v.findViewById(R.id.txNetValue);
            units = (TextView) v.findViewById(R.id.txUnits);

            v.findViewById(R.id.btnPlus).setOnClickListener(this);
            v.findViewById(R.id.btnMinus).setOnClickListener(this);

            this.simpleAdapter= simpleAdapter;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.equals(v.findViewById(R.id.btnPlus)))
                units.setText(String.valueOf(product.increaseUnits()));
            else if (v.equals(v.findViewById(R.id.btnMinus))) {
                if (product.getStock() > 0)
                    units.setText(String.valueOf(product.decreaseUnits()));
                else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setTitle("¿Desea eliminar este producto de la despensa?");
                    dialog.setNegativeButton("Cancelar", null);
                    dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            product.decreaseUnits();
                            simpleAdapter.items.remove(product);
                            simpleAdapter.notifyDataSetChanged();
                        }
                    });
                    dialog.show();
                }
            }
            else
                simpleAdapter.onItemClick(v, getAdapterPosition());
        }

        public ItemClickListener getListener() {
            return simpleAdapter;
        }
    }

    public SimpleAdapter(Context context, List<Product> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.holder_pantry, viewGroup, false);
        return new SimpleViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder viewHolder, int i) {
        Product currentItem = items.get(i);

        viewHolder.product = currentItem;
        viewHolder.descripction.setText(currentItem.getDescription());
        viewHolder.brand.setText(currentItem.getBrand());
        viewHolder.netValue.setText(currentItem.getNetValue());
        viewHolder.units.setText(String.valueOf(currentItem.getStock()));
    }

    /**
     * Sobrescritura del método de la interfaz {@link ItemClickListener}
     *
     * @param view     item actual
     * @param position posición del item actual
     */
    @Override
    public void onItemClick(View view, int position) {

    }


}

interface ItemClickListener {
    void onItemClick(View view, int position);
}
