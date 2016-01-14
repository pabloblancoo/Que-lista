package grupomoviles.quelista.igu;

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

import com.annimon.stream.Stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    TicketAdapter ticketAdapter = new TicketAdapter(this, new Ticket());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        BufferedReader bf = null;
        List<String> array = null;

        try {
            bf = new DownloadTicketFileTask().execute(getIntent().getStringExtra(ScanNFCActivity.URLTAG)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (bf != null) {
            array = new ArrayList<String>();
            String line;
            try {
                while ((line = bf.readLine()) != null) {
                    array.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recyclerView);
        recycler.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        recycler.setLayoutManager(new LinearLayoutManager(this));

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
        recycler.setAdapter(ticketAdapter);
        ticketAdapter.swipeList();
    }

    public void accept(View view) {

    }

}
