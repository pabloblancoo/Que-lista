package grupomoviles.quelista.igu;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.annimon.stream.Stream;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import grupomoviles.quelista.R;
import grupomoviles.quelista.igu.recyclerViewAdapters.TicketAdapter;
import grupomoviles.quelista.logic.DownloadImageTask;
import grupomoviles.quelista.logic.DownloadTicketFileTask;
import grupomoviles.quelista.logic.Product;
import grupomoviles.quelista.logic.Ticket;
import grupomoviles.quelista.onlineDatabase.GestorBD;

public class TicketActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 6;
    TicketAdapter ticketAdapter;
    public static final String PRODUCTS = "PRODUCTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_ticket));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Descargando datos y procesando ticket");
        pd.show();

        List<Product> products = null;
        List<String> barcodes = new ArrayList<String>();
        String s = null;

        try {
            s = new DownloadTicketFileTask().execute(getIntent().getExtras().getString(ScanNFCActivity.URLTAG).toString()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (s != null) {
            String[] lineas = s.split("\n");
            for (int i = 1; i < lineas.length - 1; i++)
                barcodes.add(lineas[i].split(";")[0]);

            new DownloadImageTask(this).execute(barcodes.toArray(new String[barcodes.size()]));

            try {
                products = GestorBD.FindProducts(barcodes.toArray(new String[barcodes.size()]));
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (products != null) {
                for (int i = 1; i < lineas.length - 1; i++)
                    products.get(i-1).setStock(Integer.parseInt(lineas[i].split(";")[1]));
            }
        }

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recyclerView);
        recycler.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        recycler.setLayoutManager(new LinearLayoutManager(this));

        Ticket ticket = new Ticket();

        if (products != null) {
            Stream.of(products).forEach(p -> {
                ticket.getProducts().put(p.getCode(), p);
            });
        }
        ticketAdapter = new TicketAdapter(this, ticket);
        recycler.setAdapter(ticketAdapter);
        ticketAdapter.swipeList();
        ticketAdapter.notifyDataSetChanged();

        pd.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void accept(View view) {
        Intent intent = new Intent();
        intent.putExtra(PRODUCTS, (Serializable) ticketAdapter.getTicket().getProducts());
        setResult(RESULT_OK, intent);
        finish();
    }

}
