package grupomoviles.quelista;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidviewhover.BlurLayout;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

public class SimpleAdapter extends RecyclerSwipeAdapter<SimpleAdapter.SimpleViewHolder> {

    private static Context context;
    private List<Product> items;

    public void swipeList(List<Product> products) {
        items = products;
        notifyDataSetChanged();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Product product;
        private ImageView image;
        private TextView descripction;
        private TextView brand;
        private TextView netValue;
        private TextView units;
        private SimpleAdapter simpleAdapter;
        private BlurLayout blurLayout;
        private ImageButton btnCart;

        public SimpleViewHolder(View v, SimpleAdapter simpleAdapter, View hover) {
            super(v);

            image = (ImageView) v.findViewById(R.id.imageView);
            descripction = (TextView) v.findViewById(R.id.txDescription);
            brand = (TextView) v.findViewById(R.id.txBrand);
            netValue = (TextView) v.findViewById(R.id.txNetValue);
            units = (TextView) v.findViewById(R.id.txUnits);

            blurLayout = (BlurLayout) v.findViewById(R.id.blurLayout);
            v.findViewById(R.id.btnPlus).setOnClickListener(this);
            v.findViewById(R.id.btnMinus).setOnClickListener(this);
            v.findViewById(R.id.btnDelete).setOnClickListener(this);
            btnCart = (ImageButton) hover.findViewById(R.id.btnCart);

            this.simpleAdapter = simpleAdapter;
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
                            removeProduct();
                        }
                    });
                    dialog.show();
                }
            }
            else if (v.equals(v.findViewById(R.id.btnDelete))) {
                removeProduct();
            }
        }

        private void removeProduct() {
            product.setStock(Product.NOT_IN_PANTRY);
            simpleAdapter.items.remove(product);
            simpleAdapter.closeItem(getAdapterPosition());
            simpleAdapter.notifyItemRemoved(getAdapterPosition());
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

        View blur = v.findViewById(R.id.blurLayout);
        View hover = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hover_holder_pantry, null);
        ((BlurLayout)blur).setHoverView(hover);
        ((BlurLayout)blur).addChildAppearAnimator(hover, R.id.btnCart, Techniques.ZoomIn);
        ((BlurLayout)blur).addChildAppearAnimator(hover, R.id.textView, Techniques.SlideInDown);
        ((BlurLayout)blur).addChildDisappearAnimator(hover, R.id.btnCart, Techniques.ZoomOut);
        ((BlurLayout)blur).addChildDisappearAnimator(hover, R.id.textView, Techniques.SlideOutUp);
        ((SwipeLayout)v).setShowMode(SwipeLayout.ShowMode.PullOut);
        ((SwipeLayout)v).addDrag(SwipeLayout.DragEdge.Right, v.findViewById(R.id.layout_buttons));

        return new SimpleViewHolder(v, this, hover);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder viewHolder, int i) {
        Product currentItem = items.get(i);

        viewHolder.product = currentItem;
        viewHolder.descripction.setText(currentItem.getDescription());
        viewHolder.brand.setText(currentItem.getBrand());
        viewHolder.netValue.setText(currentItem.getNetValue());
        viewHolder.units.setText(String.valueOf(currentItem.getStock()));
        viewHolder.blurLayout.dismissHover();

        mItemManger.bindView(viewHolder.itemView, i);
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipeLayout;
    }

    @Override
    public void onViewRecycled(SimpleViewHolder holder) {
        holder.blurLayout.dismissHover();
        super.onViewRecycled(holder);
    }
}
