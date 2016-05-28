package grupomoviles.quelista.logic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private Activity activity;

    public DownloadImageTask(Activity activity) {
        this.activity = activity;
    }

    public Bitmap doInBackground(String... product) {
        try {
            Bitmap bp;
            ByteArrayOutputStream stream;
            byte[] byteArray;
            FileOutputStream outputStream;

            for (String s : product) {
                bp = DownloadImage("http://quelista.comli.com/app/images/" + s + ".png");

                if (bp != null) {
                    stream = new ByteArrayOutputStream();
                    bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();

                    outputStream = activity.getApplicationContext().openFileOutput(s + ".png", Context.MODE_PRIVATE);
                    outputStream.write(byteArray);
                    outputStream.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return in;
    }

    private Bitmap DownloadImage(String URL) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}