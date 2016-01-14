package grupomoviles.quelista.logic;

import android.os.AsyncTask;

import com.annimon.stream.Stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import grupomoviles.quelista.onlineDatabase.GestorBD;

/**
 * Created by Nauce on 4/1/16.
 */
public class DownloadTicketFileTask extends AsyncTask<String, Void, List<Product>> {
    @Override
    protected List<Product> doInBackground(String... url) {
        List array = null;
        try {

            HttpURLConnection conn = (HttpURLConnection) new URL(url[0]).openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));
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

            return products;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
