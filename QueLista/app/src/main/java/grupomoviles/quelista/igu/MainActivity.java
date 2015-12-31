package grupomoviles.quelista.igu;

import android.nfc.NfcAdapter;

import android.opengl.Visibility;
import android.os.Bundle;

import android.support.design.internal.NavigationMenuItemView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
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
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    NfcAdapter nfcAdapter;

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    TabsFragment fragment;

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
        mNavigationView = (NavigationView) findViewById(R.id.navview);

        if(nfcAdapter == null) {
            mNavigationView.getMenu().findItem(R.id.nav_item_nfc).setEnabled(false);
            mNavigationView.getMenu().findItem(R.id.nav_item_nfc).setVisible(false);
            mNavigationView.getMenu().removeItem(R.id.nav_item_nfc);
        }


        /**
         * Inflamos el primer fragmento que vamos a mostrar.
         * En este caso decidimos que el primero que mostramos es el fragmento con las pestañas,
         * pero solo si no hay instancias anteriores.
         */
        fragment = new TabsFragment();
        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.fragment_container, fragment).commit();
        }

        /**
         * Establecemos los eventos de click en los elementos del Navigation Drawer
         */

        mNavigationView.setNavigationItemSelectedListener(menuItem -> {
            FragmentTransaction fragmentTransaction;
            mDrawerLayout.closeDrawers();

            fragmentTransaction = mFragmentManager.beginTransaction();
            switch (menuItem.getItemId()) {
                case R.id.nav_item_despensa:
                    fragment.setTab(0);
                    fragmentTransaction.replace(R.id.fragment_container,
                            fragment).commit();
                    break;
                case R.id.nav_item_lista:
                    fragment.setTab(1);
                    fragmentTransaction.replace(R.id.fragment_container,
                            fragment).commit();

                    break;
                case R.id.nav_item_carrito:
                    fragment.setTab(2);
                    fragmentTransaction.replace(R.id.fragment_container,
                            fragment).commit();
                    break;
                case R.id.nav_item_qr:
                    fragmentTransaction.replace(R.id.fragment_container,
                            new TabsFragment()).commit();
                    break;
                case R.id.nav_item_nfc:
                    fragmentTransaction.replace(R.id.fragment_container,
                            new TabsFragment()).commit();
                    break;
                case R.id.nav_item_opciones:
                    fragmentTransaction.replace(R.id.fragment_container,
                            new OpcionesFragment()).commit();
                    break;

                case R.id.nav_item_ayuda:

                    break;
            }

            return false;
        });

    }

    public void scan(View view) {
        IntentCaptureActivity ica = new IntentCaptureActivity();

        //Desactiva la opción de menú reverese camera
        ica.setReverseCamera(false);
        //Desactiva el sonido al escanear (por defecto está activado)
        //ica.setBeep(false);

        ica.initScan(this);
    }

}
