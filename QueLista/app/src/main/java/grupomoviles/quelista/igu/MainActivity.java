package grupomoviles.quelista.igu;

import android.content.Intent;
import android.nfc.NfcAdapter;

import android.os.Bundle;

import android.view.View;

import java.io.IOException;


import grupomoviles.quelista.R;
import grupomoviles.quelista.captureCodes.IntentCaptureActivity;
import grupomoviles.quelista.logic.DownloadImageTask;
import grupomoviles.quelista.logic.TicketReader;


import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    NfcAdapter nfcAdapter;

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadImageTask task;
        task = new DownloadImageTask(this);
        task.execute("5449000000996", "8410297112041", "8410297170058", "8410188012092",
                "5449000009067", "8410000826937", "8410014307682", "8410014312495", "5000127281752");

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        TicketReader ticketReader = new TicketReader();
        try {
            ticketReader.leer("");
        } catch (IOException e) {
            System.out.print("No existe");
        }


        /******************************* SECCION GUI *******************************/

        /**
         * Establecemos el DrawerLayout y el NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navView);

        /**
         * Inflamos el primer fragmento que vamos a mostrar.
         * En este caso decidimos que el primero que mostramos es el fragmento con las pestañas,
         * pero solo si no hay instancias anteriores.
         */
        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
        }

        /**
         * Establecemos los eventos de click en los elementos del Navigation Drawer
         */

        mNavigationView.setNavigationItemSelectedListener(menuItem -> {
            mDrawerLayout.closeDrawers();
            FragmentTransaction fragmentTransaction;
            if (menuItem.getItemId() == R.id.nav_item_settings) {
                fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerView, new OpcionesFragment()).commit();
            } else if (menuItem.getItemId() == R.id.nav_item_inicio) {
                fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
            }

            return false;
        });

        /**
         * Configuramos el ActionBarDrawerToggle
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    public void scan(View view) {
        IntentCaptureActivity ica = new IntentCaptureActivity();

        //Desactiva la opción de menú reverese camera
        ica.setReverseCamera(false);
        //Desactiva el sonido al escanear (por defecto está activado)
        //ica.setBeep(false);

        ica.initScan(this);
    }

    public void lanzar(View view) {
        startActivity(new Intent(MainActivity.this, ProductInfoActivity.class));
    }

}
