package grupomoviles.quelista.logic;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nauce on 4/1/16.
 */
public class DownloadTicketFileTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... url) {
        ;
        try {

            HttpURLConnection conn = (HttpURLConnection) new URL(url[0]).openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line;

            if (bf != null) {
                try {
                    while ((line = bf.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            int firstProduct = 1;
            List<Product> products = new ArrayList<Product>();

//            if (array != null) {
//                for (int i = firstProduct; i < array.size() - 1; i++) {
//                    try {
//                        String[] line = array.get(i).toString().split(";");
//                        Product p = GestorBD.FindProduct(line[0]);
//                        p.setStock(Integer.parseInt(line[1]));
//                        products.add(p);
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }

            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
