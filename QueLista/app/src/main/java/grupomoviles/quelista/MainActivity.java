package grupomoviles.quelista;

import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.annimon.stream.Stream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import grupomoviles.quelista.Database.ProductDataSource;
import grupomoviles.quelista.captureCodes.IntentCaptureActivity;


public class MainActivity extends ActionBarActivity implements AppBarLayout.OnOffsetChangedListener{

    Fragment fragmentPantry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DownloadImageTask task;
        task = new DownloadImageTask();
        task.execute("5449000000996", "8410297112041", "8410297170058", "8410188012092",
                "5449000009067", "8410000826937", "8410014307682", "8410014312495", "5000127281752");

        List<Product> products = new ArrayList<>();

        products.add(new Product("5"));
        products.add(new Product("6"));
        products.add(new Product("7"));
        products.add(new Product("8"));
        //Creado para ver si arranca la BD
        ProductDataSource productDataSource = new ProductDataSource(getApplicationContext());
        productDataSource.openDatabase();
        Stream.of(products).forEach(p -> productDataSource.insertProduct(p));
        productDataSource.close();


        productDataSource.openDatabase();
        List<Product> productosEnLaBD = productDataSource.getAllProducts();
        productDataSource.close();

        Stream.of(productosEnLaBD).forEach(p -> System.out.println("Barcode: " + p.getCode() + " categoria: " + p.getCategory() + " fecha: "+ p.getLastUpdate()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_pantry) {
            fragmentPantry = new FragmentPantry();
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.relativeLayout, fragmentPantry)
                    .commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void prueba(View view) {
        Intent intent = new Intent(MainActivity.this,ActivityPruebas.class);
        startActivity(intent);
    }

    public void scan(View view) {
        IntentCaptureActivity ica = new IntentCaptureActivity();

        //Desactiva la opción de menú reverese camera
        ica.setReverseCamera(false);
        //Desactiva el sonido al escanear (por defecto está activado)
        //ica.setBeep(false);

        ica.initScan(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (fragmentPantry != null) {
            if (verticalOffset == 0) {
                findViewById(R.id.swipeRefresh).setEnabled(true);
            } else {
                findViewById(R.id.swipeRefresh).setEnabled(false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((AppBarLayout)findViewById(R.id.appBarLayout)).addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((AppBarLayout)findViewById(R.id.appBarLayout)).removeOnOffsetChangedListener(this);
    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... product) {
            FTPClient f = new FTPClient();

            try {
                Bitmap bp;
                ByteArrayOutputStream stream;
                byte[] byteArray;
                FileOutputStream outputStream;

                for (String s:product) {
                    f.connect("31.170.164.153", 21);
                    f.login("u750524270.solocarpeta", "moviles2015");
                    f.enterLocalActiveMode();
                    f.setFileType(FTP.BINARY_FILE_TYPE);

                    bp = BitmapFactory.decodeStream(f.retrieveFileStream("/" + s + ".png"));

                    if (bp != null) {
                        stream = new ByteArrayOutputStream();
                        bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byteArray = stream.toByteArray();

                        outputStream = getApplicationContext().openFileOutput(s + ".png", Context.MODE_PRIVATE);
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
}
