package grupomoviles.quelista.igu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


import grupomoviles.quelista.R;
import grupomoviles.quelista.captureCodes.CaptureActivity;
import grupomoviles.quelista.captureCodes.IntentCaptureActivity;
import grupomoviles.quelista.igu.recyclerViewAdapters.CartAdapter;
import grupomoviles.quelista.igu.recyclerViewAdapters.PantryAdapter;
import grupomoviles.quelista.igu.recyclerViewAdapters.ShoppingListAdapter;
import grupomoviles.quelista.localDatabase.ProductDataSource;
import grupomoviles.quelista.logic.Cart;
import grupomoviles.quelista.logic.DownloadImageTask;
import grupomoviles.quelista.logic.Pantry;
import grupomoviles.quelista.logic.Product;
import grupomoviles.quelista.logic.ShoppingList;
import grupomoviles.quelista.onlineDatabase.GestorBD;


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
        PreferenceManager.setDefaultValues(this, R.xml.preferencias, false);

        // Procesar valores actuales de las preferencias.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean miniaturasPref = sharedPref.getBoolean("miniaturas", true);
        cantidadItems = Integer.parseInt(sharedPref.getString("numArticulos", "8"));

        pantryAdapter = new PantryAdapter(this, new Pantry(this));
        shoppingListAdapter = new ShoppingListAdapter(this, new ShoppingList(this));
        cartAdapter = new CartAdapter(this, new Cart(this));

        List<Product> temp = cargarBDLocal();
        if(temp!=null && !temp.isEmpty()) {
            Stream.of(temp).forEach(p -> {
                pantryAdapter.onResultProductInfoActivity(p);
                shoppingListAdapter.onResultProductInfoActivity(p);
                cartAdapter.onResultProductInfoActivity(p);
            });
        }

        setContentView(R.layout.activity_main);
        setUpNavigationDrawer();

        fragment = new TabsFragment();
        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.fragment_container, fragment).commit();
        }

        pantryAdapter.refresh();
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
        else if (resultCode == RESULT_OK && IntentCaptureActivity.CODE_CAPTURE_ACTIVITY == requestCode &&
                data.getExtras().getString(CaptureActivity.SCAN_FORMAT).toString().equals(BarcodeFormat.EAN_13.toString())) {
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
                if (isOnline()) {
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
            }

            if (product == null) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                if (isOnline())
                    dialog.setMessage(getString(R.string.producto_no_esta_online)
                            + "\n\n" + getString(R.string.desea_registrarlo));
                else
                    dialog.setMessage(getString(R.string.sin_conexion)
                            + "\n\n" + getString(R.string.quieres_manualmente));
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

        else if(resultCode == RESULT_OK && ScanNFCActivity.REQUEST_CODE == requestCode) {
            Intent intent = new Intent(MainActivity.this, TicketActivity.class);
            intent.putExtra(ScanNFCActivity.URLTAG ,data.getExtras().getString(ScanNFCActivity.URLTAG));
            startActivityForResult(intent, TicketActivity.REQUEST_CODE);
        }

        else if (resultCode == RESULT_OK &&
                IntentCaptureActivity.CODE_CAPTURE_ACTIVITY == requestCode
                && data.getExtras().getString(CaptureActivity.SCAN_FORMAT).toString().equals(BarcodeFormat.QR_CODE.toString())) {
            Intent intent = new Intent(MainActivity.this, TicketActivity.class);
            intent.putExtra(ScanNFCActivity.URLTAG, data.getExtras().getString(CaptureActivity.SCAN_CONTENT));
            startActivityForResult(intent, TicketActivity.REQUEST_CODE);
        }

        else if(resultCode == RESULT_OK && TicketActivity.REQUEST_CODE == requestCode){

            Map<String,Product> map = (Map<String, Product>) data.getExtras().get(TicketActivity.PRODUCTS);
            Stream.of(map).forEach(m ->
            {
                pantryAdapter.onResultNfcActivity(m.getValue());
                shoppingListAdapter.onResultNfcActivity(m.getValue());
                cartAdapter.onResultNfcActivity(m.getValue());
            });

            pantryAdapter.refresh();
            shoppingListAdapter.refresh();
            cartAdapter.refresh();
        }
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
                    if (isOnline()) {
                        IntentCaptureActivity ica = new IntentCaptureActivity();
                        ica.setBarcodeFormat(BarcodeFormat.QR_CODE);
                        ica.setReverseCamera(false);
                        ica.initScan(this);
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                        dialog.setMessage("No tiene conexión a internet");
                        dialog.show();
                    }
                    break;
                case R.id.nav_item_nfc:
                    if (isOnline()) {
                        Intent intent = new Intent(this, ScanNFCActivity.class);
                        startActivityForResult(intent, ScanNFCActivity.REQUEST_CODE);
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                        dialog.setMessage("No tiene conexión a internet");
                        dialog.show();
                    }
                    break;
                case R.id.nav_item_opciones:
                    //fragmentTransaction.replace(R.id.fragment_container, new OpcionesFragment()).commit();
                    startActivity(new Intent(this, SettingsActivity.class));
                    break;

                case R.id.nav_item_about:
                    startActivity(new Intent(this, AboutActivity.class));
                    Log.i("MENU","about");
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

    public void acceptShop(View view) {
        getCartAdapter().getCart().acceptShopCart();
        getCartAdapter().refresh();
    }

    public List<Product> cargarBDLocal() {
        List<Product> p = null;
        ProductDataSource database = new ProductDataSource(this);
        database.openDatabase();
        p = database.getAllProducts();
        database.close();

        //Stream.of(p).forEach(x -> Log.i("PANTRY", "PRODUCTO " + x.getCode()));

        return  p;
    }

    public boolean isOnline() {
        NetworkInfo i = ((ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;

        return true;
    }
}
