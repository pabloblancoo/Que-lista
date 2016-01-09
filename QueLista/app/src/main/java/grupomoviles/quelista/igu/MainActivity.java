package grupomoviles.quelista.igu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


import grupomoviles.quelista.R;
import grupomoviles.quelista.captureCodes.CaptureActivity;
import grupomoviles.quelista.captureCodes.IntentCaptureActivity;
import grupomoviles.quelista.igu.recyclerViewAdapters.CartAdapter;
import grupomoviles.quelista.igu.recyclerViewAdapters.PantryAdapter;
import grupomoviles.quelista.igu.recyclerViewAdapters.ShoppingListAdapter;
import grupomoviles.quelista.logic.Cart;
import grupomoviles.quelista.logic.DownloadImageTask;
import grupomoviles.quelista.logic.Pantry;
import grupomoviles.quelista.logic.Product;
import grupomoviles.quelista.logic.ShoppingList;
import grupomoviles.quelista.onlineDatabase.GestorBD;
import grupomoviles.quelista.onlineDatabase.GetProducts;


import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import com.annimon.stream.Stream;

public class MainActivity extends AppCompatActivity {

    private PantryAdapter pantryAdapter;
    private ShoppingListAdapter shoppingListAdapter;
    private CartAdapter cartAdapter;

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    TabsFragment fragment;

    private int cantidadItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cargar valores por defecto
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_data_sync, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_notification, false);

        // Procesar valores actuales de las preferencias.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean miniaturasPref = sharedPref.getBoolean("miniaturas", true);
        cantidadItems = Integer.parseInt(sharedPref.getString("numArticulos", "8"));

        pantryAdapter = new PantryAdapter(this, new Pantry());
        shoppingListAdapter = new ShoppingListAdapter(this, new ShoppingList());
        cartAdapter = new CartAdapter(this, new Cart());

        DownloadImageTask task;
        task = new DownloadImageTask(this);
        task.execute("5449000000996", "8410297112041", "8410297170058", "8410188012092",
                "5449000009067", "8410000826937", "8410014307682", "8410014312495", "5000127281752");


        setUpNavigationDrawer();

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ProductInfoActivity.REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
            Product product = (Product) data.getExtras().get(ProductInfoActivity.PRODUCT);
            pantryAdapter.onResultProductInfoActivity(product);
            shoppingListAdapter.onResultProductInfoActivity(product);
            cartAdapter.onResultProductInfoActivity(product);
        }
        else if (NewProductActivity.REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
            Product product = (Product) data.getExtras().get(NewProductActivity.PRODUCT);
            pantryAdapter.onResultProductInfoActivity(product);
            shoppingListAdapter.onResultProductInfoActivity(product);
            cartAdapter.onResultProductInfoActivity(product);
        }
        else if (IntentCaptureActivity.CODE_CAPTURE_ACTIVITY == requestCode && resultCode == RESULT_OK) {
            String content = data.getExtras().getString(CaptureActivity.SCAN_CONTENT);
            Product product;
            boolean newProduct = false;
            product = pantryAdapter.getPantry().find(content);
            if (product == null)
                product = shoppingListAdapter.getShoppingList().find(content);
            if (product == null)
                product = cartAdapter.getCart().find(content);
            if (product == null) {
                newProduct = true;
                try {
                    product = GestorBD.FindProduct(content);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (product != null)
                    new DownloadImageTask(this).execute(product.getCode());
            }

            if (product == null) {
                Intent intent = new Intent(MainActivity.this, NewProductActivity.class);
                intent.putExtra(NewProductActivity.NEWPRODUCTCODE, content);
                startActivityForResult(intent, NewProductActivity.REQUEST_CODE);
                /*AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("El producto no se encuentra registrado y tampoco está en la base de datos online...");
                dialog.show();*/
            }
            else {
                Intent intent = new Intent(MainActivity.this, ProductInfoActivity.class);
                intent.putExtra(ProductInfoActivity.PRODUCT, product);
                intent.putExtra(ProductInfoActivity.NEWPRODUCT, newProduct);
                startActivityForResult(intent, ProductInfoActivity.REQUEST_CODE);
            }
        }

        else if(ScanNFCActivity.REQUEST_CODE == requestCode && resultCode == RESULT_OK){

            Map<String,Product> map = (Map<String, Product>) data.getExtras().get(ScanNFCActivity.PRODUCTS);
            Stream.of(map).forEach(m ->
            {
                pantryAdapter.onResultNfcActivity(m.getValue());
                shoppingListAdapter.onResultNfcActivity(m.getValue());
                cartAdapter.onResultNfcActivity(m.getValue());
            });

        }
    }

    private void setUpNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navview);

        if(NfcAdapter.getDefaultAdapter(this) == null) {
            mNavigationView.getMenu().findItem(R.id.nav_item_nfc).setVisible(false);
            mNavigationView.getMenu().removeItem(R.id.nav_item_nfc);
        }

        /**
         * Establecemos los eventos de click en los elementos del Navigation Drawer
         */
        mNavigationView.setNavigationItemSelectedListener(menuItem -> {
            FragmentTransaction fragmentTransaction;

            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (menuItem.getItemId()) {
                case R.id.nav_item_despensa:
                    fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
                    fragment.setTab(0);
                    break;
                case R.id.nav_item_lista:
                    fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
                    fragment.setTab(1);
                    break;
                case R.id.nav_item_carrito:
                    fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
                    fragment.setTab(2);
                    break;
                case R.id.nav_item_new_product:
                    Intent i = new Intent(this, NewProductActivity.class);
                    startActivityForResult(i, NewProductActivity.REQUEST_CODE);
                    break;
                case R.id.nav_item_qr:

                    break;
                case R.id.nav_item_nfc:
                    Intent intent = new Intent(this,ScanNFCActivity.class);
                    startActivityForResult(intent, ScanNFCActivity.REQUEST_CODE);
                    break;
                case R.id.nav_item_opciones:
                    //fragmentTransaction.replace(R.id.fragment_container, new OpcionesFragment()).commit();
                    startActivity(new Intent(this, SettingsActivity.class));
                    break;

                case R.id.nav_item_ayuda:

                    break;
            }

            mDrawerLayout.closeDrawers();

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

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        // Actualizar visibilidad de miniaturas
        boolean miniaturasPref = sharedPref.getBoolean("miniaturas", true);
        pantryAdapter.setConMiniaturas(miniaturasPref);
    }

    public PantryAdapter getPantryAdapter() {
        return pantryAdapter;
    }

    public ShoppingListAdapter getShoppingListAdapter() {
        return shoppingListAdapter;
    }

    public CartAdapter getCartAdapter() {
        return cartAdapter;
    }

}
