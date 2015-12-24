package grupomoviles.quelista;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import grupomoviles.quelista.captureCodes.IntentCaptureActivity;

public class MainActivity extends ActionBarActivity implements AppBarLayout.OnOffsetChangedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recyclerView);
        recycler.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        recycler.setLayoutManager(new LinearLayoutManager(this));

        List<Product> products = new ArrayList<>();

        products.add(new Product("0", "Cereales Miel Pops", "Kellogg's", "Caja de 375 g", "Categoria", "Subcategoria"));
        products.add(new Product("1", "Cereales Miel Pops", "Kellogg's", "Caja de 375 g", "Categoria", "Subcategoria"));
        products.add(new Product("2", "Cereales Miel Pops", "Kellogg's", "Caja de 375 g", "Categoria", "Subcategoria"));
        products.add(new Product("3", "Cereales Miel Pops", "Kellogg's", "Caja de 375 g", "Categoria", "Subcategoria"));

        recycler.setAdapter(new SimpleAdapter(this, products));

        //Creado para ver si arranca la BD
       // LocalDatabase db = new LocalDatabase(this,"",null,1);

        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        refreshLayout.setColorSchemeResources(R.color.color_rojo_app);

        // Iniciar la tarea asíncrona al revelar el indicador
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new RefreshRecyclerTask().execute((SimpleAdapter)recycler.getAdapter());
                        refreshLayout.setRefreshing(false);
                    }
                }
        );
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
        if (id == R.id.action_settings) {
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
        //Desactiva el sonido al escanear (por defecto está activado
        //ica.setBeep(false);

        ica.initScan(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            findViewById(R.id.swipeRefresh).setEnabled(true);
        } else {
            findViewById(R.id.swipeRefresh).setEnabled(false);
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
}
