package grupomoviles.quelista.igu.recyclerViewAdapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        super(context, Stream.of(shoppingList.getProducts()).collect(Collectors.toList()));
        this.shoppingList = shoppingList;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public void swipeList() {
        items = Stream.of(shoppingList.getProducts()).collect(Collectors.toList());
    }

    @Override
    public void onResultProductInfoActivity(Product product) {
        items.remove(product);
        if(shoppingList.onResultProductInfoActivity(product))
            items.add(product);
        super.onResultProductInfoActivity(product);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_product, parent, false);

        View blur = v.findViewById(R.id.blurLayout);
        View hover = LayoutInflater.from(parent.getContext()).inflate(R.layout.hover_holder_shopping_list, null);
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

        return new ShoppingListViewHolder(v, this, hover);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        Product currentItem = items.get(position);

        ((ShoppingListViewHolder)viewHolder).units.setText(String.valueOf(currentItem.getShoppingListUnits()));
        ((ShoppingListViewHolder)viewHolder).unitsPantry.setText(String.valueOf(currentItem.getStock()));
        ((ShoppingListViewHolder)viewHolder).unitsCart.setText(String.valueOf(currentItem.getCartUnits()));

        if (currentItem.getCartUnits() == 0)
            ((SwipeLayout) viewHolder.itemView).findViewById(R.id.btnAddToCart).setVisibility(View.VISIBLE);
        else
            ((SwipeLayout) viewHolder.itemView).findViewById(R.id.btnAddToCart).setVisibility(View.GONE);

        super.onBindViewHolder(viewHolder, position);
    }

    public class ShoppingListViewHolder extends MyViewHolder {

        /**
         * Hover
         */
        private TextView unitsPantry;
        private TextView unitsCart;

        public ShoppingListViewHolder(View v, ShoppingListAdapter adapter, View hover) {
            super(v, adapter, hover);

            unitsPantry = (TextView) hover.findViewById(R.id.txPantry);
            unitsCart = (TextView) hover.findViewById(R.id.txCart);
        }

        @Override
        public void onClick(View v) {
            YoYo.with(Techniques.Pulse).duration(100).playOn(v);
            if (v.getId() == R.id.btnPlusStock)
                units.setText(String.valueOf(product.increaseShoppingListUnits()));
            else if (v.getId() == R.id.btnMinusStock) {
                if (product.getShoppingListUnits() > 1)
                    units.setText(String.valueOf(product.decreaseShoppingListUnits()));
                else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setTitle("¿Desea eliminar este producto de la lista de la compra?");
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
            else
                super.onClick(v);
        }

        private void removeProduct() {
            product.setShoppingListUnits(0);
            ((SwipeLayout)itemView).close(false);
            blurLayout.dismissHover();
            adapter.items.remove(product);
            shoppingList.remove(product);
            adapter.notifyItemRemoved(getAdapterPosition());
        }
    }
}
