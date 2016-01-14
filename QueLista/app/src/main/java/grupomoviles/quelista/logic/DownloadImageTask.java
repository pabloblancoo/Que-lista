package grupomoviles.quelista.logic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import grupomoviles.quelista.igu.MainActivity;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private Activity activity;

    public DownloadImageTask(MainActivity activity) {
        this.activity = activity;
    }

    public Bitmap doInBackground(String... product) {
        FTPClient f = new FTPClient();

        try {
            Bitmap bp;
            ByteArrayOutputStream stream;
            byte[] byteArray;
            FileOutputStream outputStream;

            for (String s : product) {
                f.connect("31.170.164.153", 21);
                f.login("u750524270.solocarpeta", "moviles2015");
                f.enterLocalActiveMode();
                f.setFileType(FTP.BINARY_FILE_TYPE);

                bp = BitmapFactory.decodeStream(f.retrieveFileStream("/" + s + ".png"));

                if (bp != null) {
                    stream = new ByteArrayOutputStream();
                    bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();

                    outputStream = activity.getApplicationContext().openFileOutput(s + ".png", Context.MODE_PRIVATE);
                    outputStream.write(byteArray);
                    outputStream.close();
                }
                f.disconnect();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}