package grupomoviles.quelista.igu;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.BufferOverflowException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import grupomoviles.quelista.R;
import grupomoviles.quelista.igu.recyclerViewAdapters.TicketAdapter;
import grupomoviles.quelista.logic.DownloadTicketFileTask;
import grupomoviles.quelista.logic.Product;
import grupomoviles.quelista.logic.Ticket;
import grupomoviles.quelista.onlineDatabase.GestorBD;

public class TicketActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 6;
    TicketAdapter ticketAdapter = new TicketAdapter(this, new Ticket());
    public static final String PRODUCTS = "products";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        BufferedReader bf = null;

        try {
            bf = new DownloadTicketFileTask().execute(getIntent().getStringExtra(ScanNFCActivity.URLTAG)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            new WorkAsyncTask().execute(bf).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recyclerView);
        recycler.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        recycler.setLayoutManager(new LinearLayoutManager(this));

        recycler.setAdapter(ticketAdapter);
        ticketAdapter.swipeList();
    }

    public void accept(View view) {
        Intent intent = new Intent();
        intent.putExtra(PRODUCTS, (Serializable) ticketAdapter.getTicket().getProducts());
        setResult(RESULT_OK, intent);
        finish();
    }

    private class WorkAsyncTask extends AsyncTask<BufferedReader, Void, List<Product>> {

        @Override
        protected List<Product> doInBackground(BufferedReader... bf) {
            List<String> array = null;

            if (bf[0] != null) {
                array = new ArrayList<String>();
                String line;
                try {
                    while ((line = bf[0].readLine()) != null) {
                        array.add(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            int firstProduct = 1;
            List<Product> products = new ArrayList<Product>();

            if (array != null) {
                for (int i = firstProduct; i < array.size() - 1; i++) {
                    try {
                        String[] line = array.get(i).toString().split(";");
                        Product p = GestorBD.FindProduct(line[0]);
                        p.setStock(Integer.parseInt(line[1]));
                        products.add(p);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(products != null) {
                Stream.of(products).forEach(p -> ticketAdapter.getTicket().getProducts().put(p.getCode(), p));
            }
            
            return Stream.of(ticketAdapter.getTicket().getProducts().values()).collect(Collectors.toList());
        }
    }

}
