package grupomoviles.quelista.igu.recyclerViewAdapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
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

import grupomoviles.quelista.R;
import grupomoviles.quelista.igu.MainActivity;
import grupomoviles.quelista.logic.Product;
import grupomoviles.quelista.logic.Ticket;

/**
 * Created by Pablo on 06/01/2016.
 */
public class TicketAdapter extends MyAdapter {

    private Ticket ticket;

    public TicketAdapter(Context context, Ticket ticket) {
        super(context, Stream.of(ticket.getProducts().values()).collect(Collectors.toList()));
        this.ticket = ticket;
    }

    public Ticket getTicket() {
        return ticket;
    }

    @Override
    public void onResultProductInfoActivity(Product product) {
        items.remove(product);
        if(ticket.onResultProductInfoActivity(product))
            items.add(product);
        super.onResultProductInfoActivity(product);
    }

    public void swipeList() {
        items = Stream.of(ticket.getProducts().values()).sortBy(p -> p.getDescription() + p.getNetValue()).collect(Collectors.toList());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_product, parent, false);

        View blur = v.findViewById(R.id.blurLayout);
        View hover = LayoutInflater.from(parent.getContext()).inflate(R.layout.hover_holder_pantry, null);
//        ((BlurLayout)blur).setHoverView(hover);

//        ((BlurLayout)blur).addChildAppearAnimator(hover, R.id.btnCart, Techniques.ZoomIn);
//        ((BlurLayout)blur).addChildAppearAnimator(hover, R.id.btnShoppingList, Techniques.ZoomIn);
//        ((BlurLayout)blur).addChildAppearAnimator(hover, R.id.btnMore, Techniques.ZoomIn);
//        ((BlurLayout)blur).addChildAppearAnimator(hover, R.id.txCart, Techniques.SlideInDown);
//        ((BlurLayout)blur).addChildAppearAnimator(hover, R.id.txShoppingList, Techniques.SlideInDown);
//
//        ((BlurLayout)blur).addChildDisappearAnimator(hover, R.id.btnCart, Techniques.ZoomOut);
//        ((BlurLayout)blur).addChildDisappearAnimator(hover, R.id.btnShoppingList, Techniques.ZoomOut);
//        ((BlurLayout)blur).addChildDisappearAnimator(hover, R.id.btnMore, Techniques.ZoomOut);
//        ((BlurLayout)blur).addChildDisappearAnimator(hover, R.id.txCart, Techniques.SlideOutUp);
//        ((BlurLayout)blur).addChildDisappearAnimator(hover, R.id.txShoppingList, Techniques.SlideOutUp);
//
//        ((SwipeLayout)v).setShowMode(SwipeLayout.ShowMode.PullOut);
//        ((SwipeLayout)v).addDrag(SwipeLayout.DragEdge.Right, v.findViewById(R.id.layout_buttons));

        return new PantryViewHolder(v, this, hover);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        Product currentItem = items.get(position);

        ((PantryViewHolder)viewHolder).units.setText(String.valueOf(currentItem.getStock()));
        ((PantryViewHolder)viewHolder).unitsShoppingList.setText(String.valueOf(currentItem.getShoppingListUnits()));
        ((PantryViewHolder)viewHolder).unitsCart.setText(String.valueOf(currentItem.getCartUnits()));
        super.onBindViewHolder(viewHolder, position);

    }

    public class PantryViewHolder extends MyViewHolder
            implements View.OnClickListener {

        //Hover
        private TextView unitsShoppingList;
        private TextView unitsCart;

        public PantryViewHolder(View v, TicketAdapter adapter, View hover) {
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
                    break;
                case R.id.btnMinusStock:
                    if (product.getStock() > 0)
                        units.setText(String.valueOf(product.decreaseStock()));
                    else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                        dialog.setMessage("¿Desea eliminar este producto de los que has comprado?");
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
                default:
                    super.onClick(v);
            }
        }


        private void removeProduct() {
            ((SwipeLayout)itemView).close(false);
            blurLayout.dismissHover();
            adapter.items.remove(product);
            ticket.remove(product);
            adapter.notifyItemRemoved(getAdapterPosition());
        }
    }
}
