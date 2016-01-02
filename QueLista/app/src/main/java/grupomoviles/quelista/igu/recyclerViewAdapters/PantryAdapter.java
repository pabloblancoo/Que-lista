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
import grupomoviles.quelista.logic.Cart;
import grupomoviles.quelista.logic.Pantry;
import grupomoviles.quelista.logic.Product;

public class PantryAdapter extends MyAdapter {

    private Pantry pantry;

    public PantryAdapter(Context context, Pantry pantry) {
        super(context, Stream.of(pantry.getProducts()).collect(Collectors.toList()));
        this.pantry = pantry;
    }

    public Pantry getPantry() {
        return pantry;
    }

    @Override
    public void onResultProductInfoActivity(Product product) {
        pantry.onResultProductInfoActivity(product);
        super.onResultProductInfoActivity(product);
    }

    public void swipeList() {
        items = Stream.of(pantry.getProducts()).sortBy(p -> p.getDescription().charAt(0)).collect(Collectors.toList());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_product, parent, false);

        View blur = v.findViewById(R.id.blurLayout);
        View hover = LayoutInflater.from(parent.getContext()).inflate(R.layout.hover_holder_pantry, null);
        ((BlurLayout)blur).setHoverView(hover);

        ((BlurLayout)blur).addChildAppearAnimator(hover, R.id.btnCart, Techniques.ZoomIn);
        ((BlurLayout)blur).addChildAppearAnimator(hover, R.id.btnShoppingList, Techniques.ZoomIn);
        ((BlurLayout)blur).addChildAppearAnimator(hover, R.id.btnMore, Techniques.ZoomIn);
        ((BlurLayout)blur).addChildAppearAnimator(hover, R.id.txCart, Techniques.SlideInDown);
        ((BlurLayout)blur).addChildAppearAnimator(hover, R.id.txShoppingList, Techniques.SlideInDown);

        ((BlurLayout)blur).addChildDisappearAnimator(hover, R.id.btnCart, Techniques.ZoomOut);
        ((BlurLayout)blur).addChildDisappearAnimator(hover, R.id.btnShoppingList, Techniques.ZoomOut);
        ((BlurLayout)blur).addChildDisappearAnimator(hover, R.id.btnMore, Techniques.ZoomOut);
        ((BlurLayout)blur).addChildDisappearAnimator(hover, R.id.txCart, Techniques.SlideOutUp);
        ((BlurLayout)blur).addChildDisappearAnimator(hover, R.id.txShoppingList, Techniques.SlideOutUp);

        ((SwipeLayout)v).setShowMode(SwipeLayout.ShowMode.PullOut);
        ((SwipeLayout)v).addDrag(SwipeLayout.DragEdge.Right, v.findViewById(R.id.layout_buttons));

        return new PantryViewHolder(v, this, hover);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        Product currentItem = items.get(position);

        ((PantryViewHolder)viewHolder).units.setText(String.valueOf(currentItem.getStock()));
        ((PantryViewHolder)viewHolder).unitsShoppingList.setText(String.valueOf(currentItem.getShoppingListUnits()));
        ((PantryViewHolder)viewHolder).unitsCart.setText(String.valueOf(currentItem.getCartUnits()));

        if (currentItem.getCartUnits() == 0)
            ((SwipeLayout) viewHolder.itemView).findViewById(R.id.btnAddToShoppingList).setVisibility(View.VISIBLE);
        else
            ((SwipeLayout)viewHolder.itemView).findViewById(R.id.btnAddToShoppingList).setVisibility(View.GONE);

        super.onBindViewHolder(viewHolder, position);
    }

    public class PantryViewHolder extends MyViewHolder
            implements View.OnClickListener {

        private TextView unitsShoppingList;
        private TextView unitsCart;

        public PantryViewHolder(View v, PantryAdapter adapter, View hover) {
            super(v, adapter, hover);

            unitsShoppingList = (TextView) hover.findViewById(R.id.txShoppingList);
            unitsCart = (TextView) hover.findViewById(R.id.txCart);
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
            else
                super.onClick(v);
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
