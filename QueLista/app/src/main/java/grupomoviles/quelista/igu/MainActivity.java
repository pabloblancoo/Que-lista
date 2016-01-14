package grupomoviles.quelista.igu;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import com.google.zxing.BarcodeFormat;

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

        pantryAdapter = new PantryAdapter(this, new Pantry(this));
        shoppingListAdapter = new ShoppingListAdapter(this, new ShoppingList(this));
        cartAdapter = new CartAdapter(this, new Cart(this));

        setContentView(R.layout.activity_main);
        setUpNavigationDrawer();

        fragment = new TabsFragment();
        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.fragment_container, fragment).commit();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("requestCode", "Codigo: " + requestCode);
        getPantryAdapter().refresh();
        getCartAdapter().refresh();
        getShoppingListAdapter().refresh();

        if (resultCode == RESULT_OK && ProductInfoActivity.REQUEST_CODE == requestCode) {
            Product product = (Product) data.getExtras().get(ProductInfoActivity.PRODUCT);
            pantryAdapter.onResultProductInfoActivity(product);
            shoppingListAdapter.onResultProductInfoActivity(product);
            cartAdapter.onResultProductInfoActivity(product);
        }
        else if (NewProductActivity.REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
            Product product = (Product) data.getExtras().get(NewProductActivity.PRODUCT);
            Log.i("PRODUCTO_NEW", "Codigo: " + product.getCode() + " -- Unidades: " + product.getStock());
            pantryAdapter.onResultNewProductActivity(product);
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage(getString(R.string.producto_no_esta_online)
                                    + "\n\n"+getString(R.string.desea_registrarlo));
                dialog.setPositiveButton(getString(R.string.Aceptar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this, NewProductActivity.class);
                        intent.putExtra(NewProductActivity.NEWPRODUCTCODE, content);
                        startActivityForResult(intent, NewProductActivity.REQUEST_CODE);
                    }
                });
                dialog.setNegativeButton(getString(R.string.Cancelar), null);
                dialog.show();
            }
            else {
                Intent intent = new Intent(MainActivity.this, ProductInfoActivity.class);
                intent.putExtra(ProductInfoActivity.PRODUCT, product);
                intent.putExtra(ProductInfoActivity.NEWPRODUCT, newProduct);
                startActivityForResult(intent, ProductInfoActivity.REQUEST_CODE);
            }
        }

        else if(ScanNFCActivity.REQUEST_CODE == requestCode && resultCode == RESULT_OK){
//            startActivityForResult(new Intent(MainActivity.this));
        }
//
//        else if(ScanNFCActivity.REQUEST_CODE == requestCode && resultCode == RESULT_OK){
//
//            Map<String,Product> map = (Map<String, Product>) data.getExtras().get(ScanNFCActivity.PRODUCTS);
//            Stream.of(map).forEach(m ->
//            {
//                pantryAdapter.onResultNfcActivity(m.getValue());
//                shoppingListAdapter.onResultNfcActivity(m.getValue());
//                cartAdapter.onResultNfcActivity(m.getValue());
//            });
//
//        }
    }

    private void setUpNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navview);

        if(NfcAdapter.getDefaultAdapter(this) == null) {
            mNavigationView.getMenu().findItem(R.id.nav_item_nfc).setVisible(false);
            mNavigationView.getMenu().removeItem(R.id.nav_item_nfc);
        }

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
                case R.id.nav_item_scan_product:
                    scan(null);
                    break;
                case R.id.nav_item_new_product:
                    Intent i = new Intent(this, NewProductActivity.class);
                    startActivityForResult(i, NewProductActivity.REQUEST_CODE);
                    break;
                case R.id.nav_item_qr:
                    IntentCaptureActivity ica = new IntentCaptureActivity();
                    ica.setBarcodeFormat(BarcodeFormat.QR_CODE);
                    ica.setReverseCamera(false);
                    ica.initScan(this);
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
