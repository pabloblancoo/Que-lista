package grupomoviles.quelista.igu.recyclerViewAdapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.androidviewhover.BlurLayout;
import com.daimajia.swipe.SwipeLayout;

import java.util.List;

import grupomoviles.quelista.R;
import grupomoviles.quelista.igu.ProductInfoActivity;
import grupomoviles.quelista.logic.Product;
import grupomoviles.quelista.logic.ShoppingList;

public class ShoppingListAdapter extends MyAdapter {

    private ShoppingList shoppingList;

    public ShoppingListAdapter(Context context, ShoppingList shoppingList) {
        super(context);
        this.items = Stream.of(shoppingList.getProducts()).collect(Collectors.toList());
        this.shoppingList = shoppingList;
    }

    public void swipeList(List<Product> products) {
        items = products;
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
    public ShoppingListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.holder_product, viewGroup, false);

        View blur = v.findViewById(R.id.blurLayout);
        View hover = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hover_holder_shopping_list, null);
        ((BlurLayout)blur).setHoverView(hover);
        ((BlurLayout)blur).addChildAppearAnimator(hover, R.id.btnCart, Techniques.ZoomIn);
        ((BlurLayout)blur).addChildAppearAnimator(hover, R.id.btnPantry, Techniques.ZoomIn);
        ((BlurLayout)blur).addChildAppearAnimator(hover, R.id.btnMore, Techniques.ZoomIn);
        ((BlurLayout)blur).addChildAppearAnimator(hover, R.id.txCart, Techniques.SlideInDown);
        ((BlurLayout)blur).addChildAppearAnimator(hover, R.id.txPantry, Techniques.SlideInDown);
        ((BlurLayout)blur).addChildDisappearAnimator(hover, R.id.btnCart, Techniques.ZoomOut);
        ((BlurLayout)blur).addChildDisappearAnimator(hover, R.id.btnPantry, Techniques.ZoomOut);
        ((BlurLayout)blur).addChildDisappearAnimator(hover, R.id.btnMore, Techniques.ZoomOut);
        ((BlurLayout)blur).addChildDisappearAnimator(hover, R.id.txCart, Techniques.SlideOutUp);
        ((BlurLayout)blur).addChildDisappearAnimator(hover, R.id.txPantry, Techniques.SlideOutUp);
        ((SwipeLayout)v).setShowMode(SwipeLayout.ShowMode.PullOut);
        ((SwipeLayout)v).addDrag(SwipeLayout.DragEdge.Right, v.findViewById(R.id.layout_buttons));

        return new ShoppingListViewHolder(v, this, hover);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Product currentItem = items.get(position);

        ((ShoppingListViewHolder)viewHolder).blurLayout.dismissHover();
        ((ShoppingListViewHolder)viewHolder).product = currentItem;
        ((ShoppingListViewHolder)viewHolder).image.setImageBitmap(currentItem.getImage(context));
        ((ShoppingListViewHolder)viewHolder).description.setText(currentItem.getDescription());
        ((ShoppingListViewHolder)viewHolder).brand.setText(currentItem.getBrand());
        ((ShoppingListViewHolder)viewHolder).netValue.setText(currentItem.getNetValue());
        ((ShoppingListViewHolder)viewHolder).units.setText(String.valueOf(currentItem.getStock()));

        ((ShoppingListViewHolder)viewHolder).unitsPantry.setText(String.valueOf(currentItem.getStock()));
        ((ShoppingListViewHolder)viewHolder).unitsCart.setText(String.valueOf(currentItem.getCartUnits()));

        mItemManger.bindView(viewHolder.itemView, position);
    }

    public static class ShoppingListViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Product product;
        private ImageView image;
        private TextView description;
        private TextView brand;
        private TextView netValue;
        private TextView units;
        private ShoppingListAdapter adapter;
        private BlurLayout blurLayout;

        /**
         * Hover
         */
        private TextView unitsPantry;
        private TextView unitsCart;

        public ShoppingListViewHolder(View v, ShoppingListAdapter adapter, View hover) {
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
            unitsPantry = (TextView) hover.findViewById(R.id.txPantry);
            unitsCart = (TextView) hover.findViewById(R.id.txCart);

            this.adapter = adapter;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            YoYo.with(Techniques.Pulse).duration(100).playOn(v);
            if (v.getId() == R.id.btnPlusStock)
                units.setText(String.valueOf(product.increaseStock()));
            else if (v.getId() == R.id.btnMinusStock) {
                if (product.getStock() > 0)
                    units.setText(String.valueOf(product.decreaseStock()));
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
            else if (v.getId() == R.id.btnDelete) {
                removeProduct();
            }
            else if (v.getId() == R.id.btnMore) {
                Intent i = new Intent(context, ProductInfoActivity.class);
                i.putExtra(ProductInfoActivity.PRODUCT, product);
                context.startActivity(i);
            }
        }

        private void removeProduct() {
            product.setStock(Product.NOT_IN_PANTRY);
            ((SwipeLayout)itemView).close(false);
            blurLayout.dismissHover();
            adapter.items.remove(product);
            adapter.notifyItemRemoved(getAdapterPosition());
        }
    }
}
