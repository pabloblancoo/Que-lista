package grupomoviles.quelista.igu.recyclerViewAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.androidviewhover.BlurLayout;
import com.daimajia.swipe.SwipeLayout;

import java.util.List;

import grupomoviles.quelista.R;
import grupomoviles.quelista.igu.MainActivity;
import grupomoviles.quelista.igu.ScanNFCActivity;
import grupomoviles.quelista.logic.Pantry;
import grupomoviles.quelista.logic.Product;

public class PantryAdapter extends MyAdapter {

    private Pantry pantry;

    public PantryAdapter(Context context, Pantry pantry) {
        super(context, Stream.of(pantry.getProducts().values()).collect(Collectors.toList()));
        this.pantry = pantry;
        
        List<Product> temp = cargarBDLocal();
        if(temp!=null && !temp.isEmpty()) {
            Stream.of(temp).forEach(p -> this.onResultProductInfoActivity(p));
        }

        Stream.of(this.items).forEach(x -> Log.i("PANTRY", "PRODUCTO " + x.getCode() + " -- Unidades: " + x.getStock()));
        //Stream.of(items).forEach(p -> pantry.getProducts().put(p.getCode(), p));
        //Stream.of(items).forEach(p -> Log.i("PANTRY", "PRODUCTO " + p.getCode()));
    }

    public Pantry getPantry() {
        return pantry;
    }


    @Override
    public void onResultProductInfoActivity(Product product) {
        items.remove(product);
        if(pantry.onResultProductInfoActivity(product))
            items.add(product);
        super.onResultProductInfoActivity(product);
    }

    //@Override
    public void onResultNewProductActivity(Product product) {
        boolean encontrado = false;
       /* Stream.of(items).forEach(i -> {
            if (i.getCode().equals(product.getCode()))
                i.setStock(i.getStock() + product.getStock());
        });*/

        /*for (Product i :items) {
            if (i.getCode().equals(product.getCode())) {
                i.setStock(i.getStock() + product.getStock());
                encontrado = true;
            }
        }

        if(!encontrado) {
            items.add(product);
        }*/

        items.add(product);
        super.onResultProductInfoActivity(product);
    }


    public void onResultNfcActivity(Product product) {
        pantry.onResultNfcActivity(product);
    }

    public void swipeList() {
        items = Stream.of(pantry.getProducts().values()).sortBy(p -> p.getDescription() + p.getNetValue()).collect(Collectors.toList());
    }

    public void filtrar(String cadena) {
        items = Stream.of(pantry.getProducts().values())
                .filter(p -> p.getDescription().trim().replace("-", "").concat(" ")
                            .concat(p.getBrand().trim()).concat(" ")
                            .concat(p.getNetValue().trim()).toLowerCase().contains(cadena.trim().toLowerCase()))
                .sortBy(p -> p.getDescription() + p.getNetValue())
                .collect(Collectors.toList());
    }

    @Override
    public void refresh() {
        pantry.refresh();
        swipeList();
        notifyDataSetChanged();
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

        if (currentItem.getShoppingListUnits() == Product.NOT_IN_SHOPPING_LIST)
            ((SwipeLayout) viewHolder.itemView).findViewById(R.id.btnAddToShoppingList).setVisibility(View.VISIBLE);
        else
            ((SwipeLayout)viewHolder.itemView).findViewById(R.id.btnAddToShoppingList).setVisibility(View.GONE);

        super.onBindViewHolder(viewHolder, position);
    }

    public class PantryViewHolder extends MyViewHolder
            implements View.OnClickListener {

        //Hover
        private TextView unitsShoppingList;
        private TextView unitsCart;

        public PantryViewHolder(View v, PantryAdapter adapter, View hover) {
            super(v, adapter, hover);

            unitsShoppingList = (TextView) hover.findViewById(R.id.txShoppingList);
            unitsCart = (TextView) hover.findViewById(R.id.txCart);

            v.findViewById(R.id.btnAddToShoppingList).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            YoYo.with(Techniques.Pulse).duration(100).playOn(v);
            switch(v.getId()) {
                case R.id.btnPlusStock:
                    units.setText(String.valueOf(product.increaseStock()));
                    guardarDatosBDLocal(product);
                    break;
                case R.id.btnMinusStock:
                    if (product.getStock() > 0) {
                        units.setText(String.valueOf(product.decreaseStock()));
                        guardarDatosBDLocal(product);
                    }
                    else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                        dialog.setMessage("¿Desea eliminar este producto de la despensa?");
                        dialog.setNegativeButton("Cancelar", null);
                        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            removeProduct();
                        }
                    });
                    dialog.show();
                    }
                    break;
                case R.id.btnDelete:
                    removeProduct();
                    break;
                case R.id.btnAddToShoppingList:
                    addToShoppingList();
                    break;
                default:
                    super.onClick(v);
            }
        }

        private void addToShoppingList() {
            itemView.findViewById(R.id.btnAddToShoppingList).setVisibility(View.GONE);
            ((SwipeLayout)itemView).close(false);
            notifyItemChanged(getAdapterPosition());
            ((MainActivity) context).getShoppingListAdapter().addToShoppingList(pantry.find(product.getCode()));
            Snackbar.make(itemView, product.getDescription() + " ha sido añadido a la lista de la compra", Snackbar.LENGTH_LONG).show();
        }

        private void removeProduct() {
            ((SwipeLayout)itemView).close(false);
            blurLayout.dismissHover();
            product.setStock(-1);
            guardarDatosBDLocal(product);
            adapter.items.remove(product);
            pantry.remove(product);
            adapter.notifyItemRemoved(getAdapterPosition());
        }
    }
}
