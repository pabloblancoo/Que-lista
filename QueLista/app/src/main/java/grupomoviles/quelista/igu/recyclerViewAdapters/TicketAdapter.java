package grupomoviles.quelista.igu.recyclerViewAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

import grupomoviles.quelista.R;
import grupomoviles.quelista.igu.ProductInfoActivity;
import grupomoviles.quelista.logic.Product;
import grupomoviles.quelista.logic.Ticket;

/**
 * Created by Pablo on 06/01/2016.
 */
public class TicketAdapter extends RecyclerSwipeAdapter<TicketAdapter.TicketViewHolder> {

    private Ticket ticket;
    private Context context;
    List<Product> items;
    SharedPreferences sharedPref;
    boolean miniaturasPref;

    public TicketAdapter(Context context, Ticket ticket) {
        List<Product> items2 = Stream.of(ticket.getProducts().values()).collect(Collectors.toList());
        this.context = context;
        this.items = Stream.of(items2).sortBy(i -> i.getDescription() + i.getNetValue()).collect(Collectors.toList());

        // Procesar valores actuales de las preferencias.
        //sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        //miniaturasPref = sharedPref.getBoolean("miniaturas", true);

        this.ticket = ticket;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void onResultProductInfoActivity(Product product) {
        items.remove(product);
        if(ticket.onResultProductInfoActivity(product))
            items.add(product);
        items = Stream.of(items).sortBy(i -> i.getDescription() + i.getNetValue()).collect(Collectors.toList());
        notifyDataSetChanged();
    }

    public void onResultNfcActivity(Product product){
        items = Stream.of(items).sortBy(i -> i.getDescription() + i.getNetValue()).collect(Collectors.toList());
        notifyDataSetChanged();
    }

    public void swipeList() {
        items = Stream.of(ticket.getProducts().values()).sortBy(p -> p.getDescription() + p.getNetValue()).collect(Collectors.toList());
        notifyDataSetChanged();
    }

    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_ticket, parent, false);

//        View blur = v.findViewById(R.id.blurLayout);
//        View hover = LayoutInflater.from(parent.getContext()).inflate(R.layout.hover_holder_pantry, null);
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
        ((SwipeLayout)v).setShowMode(SwipeLayout.ShowMode.PullOut);
        ((SwipeLayout)v).addDrag(SwipeLayout.DragEdge.Right, v.findViewById(R.id.layout_buttons_ticket));

        return new TicketViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(TicketViewHolder viewHolder, int position) {
        Product currentItem = items.get(position);

        //viewHolder.blurLayout.dismissHover();
        viewHolder.product = currentItem;

//        if(miniaturasPref) {
//            viewHolder.image.setVisibility(View.VISIBLE);
//            viewHolder.image.setImageBitmap(currentItem.getImage(context));
//        }
//        else{
//            viewHolder.image.setVisibility(View.GONE);
//        }

        viewHolder.image.setImageBitmap(currentItem.getImage(context));
        viewHolder.units.setText(String.valueOf(currentItem.getStock()));
        System.out.println(currentItem.getDescription());
        viewHolder.description.setText(currentItem.getDescription());
        viewHolder.brand.setText(currentItem.getBrand());
        viewHolder.netValue.setText(currentItem.getNetValue());

        if (position == getItemCount() - 1)
            viewHolder.itemView.setPadding(0, 0, 0, 120);
        else
            viewHolder.itemView.setPadding(0, 0, 0, 0);

        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipeLayout;
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        Product product;
        ImageView image;
        TextView description;
        TextView brand;
        TextView netValue;
        TextView units;
        TicketAdapter adapter;

        public TicketViewHolder(View v, TicketAdapter adapter) {
            super(v);

            image = (ImageView) v.findViewById(R.id.imgProductTicket);
            description = (TextView) v.findViewById(R.id.txDescriptionTicket);
            brand = (TextView) v.findViewById(R.id.txBrandTicket);
            netValue = (TextView) v.findViewById(R.id.txNetValueTicket);
            units = (TextView) v.findViewById(R.id.txUnitsTicket);

            v.findViewById(R.id.btnPlusStockTicket).setOnClickListener(this);
            v.findViewById(R.id.btnMinusStockTicket).setOnClickListener(this);
            v.findViewById(R.id.btnDeleteTicket).setOnClickListener(this);

            this.adapter = adapter;
        }

        @Override
        public void onClick(View v) {
            YoYo.with(Techniques.Pulse).duration(100).playOn(v);
            switch(v.getId()) {
                case R.id.btnPlusStockTicket:
                    units.setText(String.valueOf(product.increaseStock()));
                    break;
                case R.id.btnMinusStockTicket:
                    if (product.getStock() > 1)
                        units.setText(String.valueOf(product.decreaseStock()));
                    else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                        dialog.setMessage("¿Desea eliminar este producto del ticket?");
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
                case R.id.btnDeleteTicket:
                    removeProduct();
                    break;
            }
        }


        private void removeProduct() {
            ((SwipeLayout)itemView).close(false);
            adapter.items.remove(product);
            ticket.remove(product);
            adapter.notifyItemRemoved(getAdapterPosition());
        }

    }
}
