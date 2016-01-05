package grupomoviles.quelista.logic;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Nauce on 4/1/16.
 */
public class DownloadTicketFileTask extends AsyncTask<String, Void, BufferedReader> {

    @Override
    protected BufferedReader doInBackground(String... url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url[0]).openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            return new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
