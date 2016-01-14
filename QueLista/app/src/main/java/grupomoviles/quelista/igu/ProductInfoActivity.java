package grupomoviles.quelista.igu;

import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Stream;

import java.util.Date;
import java.util.List;

import grupomoviles.quelista.R;
import grupomoviles.quelista.localDatabase.ProductDataSource;
import grupomoviles.quelista.logic.Product;

public class ProductInfoActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    public static final String PRODUCT = "PRODUCT";
    public static final String NEWPRODUCT = "NEWPRODUCT";
    public static final int REQUEST_CODE = 1;
    private boolean newProduct = false;

    private ImageView productImage;

    private TextView description;
    private TextView brand;
    private TextView netValue;
    private TextView category;

    private TextView unitsPantry;
    private TextView unitsLista;
    private TextView unitsCarrito;
    private TextView unitsDescontar;
    private TextView unitsDays;
    private TextView unitsWhenHave;
    private TextView unitsAddWhenHave;

    private SwitchCompat switchCompatTakeUnits;
    private SwitchCompat switchCompatAddToShoppingList;
    private Button buttonPlusDescontar;
    private Button buttonMinusDescontar;
    private Button buttonPlusDays;
    private Button buttonMinusDays;

    private Button buttonPlusWhenHave;
    private Button buttonMinusWhenHave;
    private Button buttonPlusAddWhenHave;
    private Button buttonMinusAddWhenHave;

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_info));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        product = (Product) getIntent().getExtras().get(PRODUCT);
        if (getIntent().getExtras().get(NEWPRODUCT) != null)
            newProduct = (boolean) getIntent().getExtras().get(NEWPRODUCT);

        if (newProduct)
            getSupportActionBar().setTitle(getString(R.string.producto_nuevo));
        else
            getSupportActionBar().setTitle(product.getCode());

        productImage = (ImageView) findViewById(R.id.imgProduct);

        description = (TextView) findViewById(R.id.txDescription);
        brand = (TextView) findViewById(R.id.txBrand);
        netValue = (TextView) findViewById(R.id.txNetValue);
        category = (TextView) findViewById(R.id.txCategory);

        unitsPantry = (TextView) findViewById(R.id.txUnitsPantry);
        unitsLista = (TextView) findViewById(R.id.txUnitsShoppingList);
        unitsCarrito = (TextView) findViewById(R.id.txUnitsCart);

        switchCompatTakeUnits = (SwitchCompat) findViewById(R.id.switchTakeUnits);

        unitsDescontar = (TextView) findViewById(R.id.txUnitsTakes);
        buttonPlusDescontar = (Button) findViewById(R.id.btnPlusUnitsTakes);
        buttonMinusDescontar = (Button) findViewById(R.id.btnMinusUnitsTakes);

        unitsDays = (TextView) findViewById(R.id.txDaysTakes);
        buttonPlusDays = (Button) findViewById(R.id.btnPlusDaysTakes);
        buttonMinusDays = (Button) findViewById(R.id.btnMinusDaysTakes);

        switchCompatAddToShoppingList = (SwitchCompat) findViewById(R.id.switchAddToShoppingList);

        unitsWhenHave = (TextView) findViewById(R.id.txUnitsWhenHave);
        buttonPlusWhenHave = (Button) findViewById(R.id.btnPlusWhenHave);
        buttonMinusWhenHave = (Button) findViewById(R.id.btnMinusWhenHave);

        unitsAddWhenHave = (TextView) findViewById(R.id.txUnitsAddWhenHave);
        buttonPlusAddWhenHave = (Button) findViewById(R.id.btnPlusAddWhenHave);
        buttonMinusAddWhenHave = (Button) findViewById(R.id.btnMinusAddWhenHave);

        //Mostrar u ocultar botones
        if (newProduct) {
            findViewById(R.id.fabAccept).setVisibility(View.VISIBLE);
            findViewById(R.id.fabCancel).setVisibility(View.VISIBLE);
            findViewById(R.id.linearLayoutWithPadding).setPadding(0, 0, 0, 120);
        }
        //Mostrar o ocultar los textView al principio de la aplicacion
        if (product.getStock() == -1) {
            unitsPantry.setVisibility(View.INVISIBLE);
        }

        //Eventos
        switchCompatTakeUnits.setOnCheckedChangeListener(this);
        switchCompatAddToShoppingList.setOnCheckedChangeListener(this);

        showAllProductProperties();
    }

    private void showAllProductProperties() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                while (bitmap == null) {
                    bitmap = product.getImage(getApplicationContext());
                }
                productImage.setImageBitmap(bitmap);
            }
        }).start();

        productImage.setImageBitmap(product.getImage(getApplicationContext()));

        description.setText(product.getDescription());
        brand.setText(product.getBrand());
        netValue.setText(product.getNetValue());
        category.setText(product.getCategory());


        unitsPantry.setText(String.valueOf(product.getStock()));
        unitsLista.setText(String.valueOf(product.getShoppingListUnits()));
        unitsCarrito.setText(String.valueOf(product.getCartUnits()));

        Date date = product.getLastUpdate();
        if (date == null) {
            findViewById(R.id.layoutTakeUnitsSwitch).setVisibility(View.GONE);
            switchCompatTakeUnits.setChecked(false);
        } else {
//            switchCompatTakeUnits.setChecked(true);
            unitsDescontar.setText(String.valueOf(product.getConsumeUnits()));
            unitsDays.setText(String.valueOf(product.getConsumeCycle()));
        }

        if (product.getMinStock() == -1) {
            findViewById(R.id.layoutAddToShoppingListSwitch).setVisibility(View.GONE);
            switchCompatAddToShoppingList.setChecked(false);
        } else {
//            switchCompatAddToShoppingList.setChecked(true);
            unitsWhenHave.setText(String.valueOf(product.getMinStock()));
            unitsAddWhenHave.setText(String.valueOf(product.getUnitsToAdd()));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                if (!newProduct) {
                    Intent i = new Intent();
                    i.putExtra(PRODUCT, product);
                    setResult(RESULT_OK, i);
                }
                onBackPressed();
                return true;

            case R.id.action_delete:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage(getString(R.string.desea_eliminar_producto_completo));
                dialog.setPositiveButton(getString(R.string.Aceptar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteProduct();
                        Intent o = new Intent();
                        o.putExtra(PRODUCT, product);
                        setResult(RESULT_OK, o);
                        onBackPressed();
                    }
                });
                dialog.setNegativeButton(R.string.Cancelar, null);
                dialog.show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!newProduct)
            getMenuInflater().inflate(R.menu.menu_product_info, menu);

        return true;
    }

    public void aumentarPantry(View view) {
        if (unitsPantry.getVisibility() == View.INVISIBLE) {
            unitsPantry.setVisibility(View.VISIBLE);
        }
        product.increaseStock();
        makeChanges(unitsPantry, true);
    }

    public void disminuirPantry(View view) {
        product.decreaseStock();
        makeChanges(unitsPantry, false);
    }

    public void aumentarShoppingList(View view) {
        if (unitsLista.getVisibility() == View.INVISIBLE) {
            unitsLista.setVisibility(View.VISIBLE);
        }
        product.increaseShoppingListUnits();
        makeChanges(unitsLista, true);
    }

    public void disminuirShoppingList(View view) {
        product.decreaseShoppingListUnits();
        makeChanges(unitsLista, false);
    }

    public void aumentarCartList(View view) {
        if (unitsCarrito.getVisibility() == View.INVISIBLE) {
            unitsCarrito.setVisibility(View.VISIBLE);
        }
        product.increaseCartUnits();
        makeChanges(unitsCarrito, true);
    }

    public void disminuirCartList(View view) {
        product.decreaseCartUnits();
        makeChanges(unitsCarrito, false);

    }

    public void aumentarUnitsDescontar(View view) {
        product.increaseConsumeUnits();

        makeChanges(unitsDescontar, true);

//        Este es el metodo que hay que llamar para que cambie unidades, por unidad
//        changeTextUnits(unitsDescontar);


    }

    private void changeTextUnits(TextView textView) {
        int units = Integer.parseInt(textView.getText().toString());
        if(units > 1){
            textView.setText(R.string.unitsTakesMore);
        }
        else{
            textView.setText(R.string.unitsTakes);
        }
    }

    public void disminuirUnitsDescontar(View view) {
        product.decreaseConsumeUnits();
        makeChanges(unitsDescontar, false);
    }

    public void aumentarDays(View view) {
        product.increaseConsumeCycle();
        makeChanges(unitsDays, true);
    }


    public void disminuirDays(View view) {
        product.decreaseConsumeCycle();
        makeChanges(unitsDays, false);
    }

    public void aumentarWhenHave(View view) {
        product.increaseMinStock();
        makeChanges(unitsWhenHave, true);
    }

    public void disminuirWhenHave(View view) {
        product.decreaseMinStock();
        makeChanges(unitsWhenHave, false);
    }

    public void aumentarAddWhenHave(View view) {
        product.increaseUnitsToAdd();
        makeChanges(unitsAddWhenHave, true);
    }

    public void disminuirAddWhenHave(View view) {
        product.decreaseUnitsToAdd();
        makeChanges(unitsAddWhenHave, false);
    }

    private void makeChanges(TextView textView, boolean sum) {
        String texto = textView.getText().toString();
        int units = Integer.parseInt(texto);
        if (sum)
            units++;
        else {
            if (units > 0)
                units--;
        }
        textView.setText(String.valueOf(units));
        if (textView == unitsPantry)
            checkStockUnits();
        if (!newProduct)
            guardarDatos();
        showDatabaseData();

    }

    private void checkStockUnits() {

        if (switchCompatAddToShoppingList.isChecked()) {
            if (product.getStock() <= product.getMinStock() && product.getShoppingListUnits() < product.getUnitsToAdd()) {
                if (unitsLista.getVisibility() == View.INVISIBLE) {
                    unitsLista.setVisibility(View.VISIBLE);
                }
                product.setShoppingListUnits(product.getShoppingListUnits() + product.getUnitsToAdd());
                unitsPantry.setText(String.valueOf(product.getStock()));
                unitsLista.setText(String.valueOf(product.getShoppingListUnits()));

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle(getString(R.string.se_han_añadido)+" " + product.getUnitsToAdd() + " "+ getString(R.string.productos_a_la_lista_compra));
                dialog.setPositiveButton(getString((R.string.Aceptar)), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dialog.show();

            }
        }

    }

    private void showDatabaseData() {
        ProductDataSource database = new ProductDataSource(this);
        database.openDatabase();
        List<Product> productsList = database.getAllProducts();
        database.close();

        Stream.of(productsList).forEach(p -> System.out.println(
                "Stock: " + p.getStock() +
                        "; ShoppingList: " + p.getShoppingListUnits() +
                        "; CartUnits: " + p.getCartUnits() +
                        ";LastUpdate: " + p.getLastUpdate() +
                        "; Minstock: " + p.getMinStock() +
                        "; Añadir: " + p.getUnitsToAdd()));
    }

    private void guardarDatos() {
        ProductDataSource database = new ProductDataSource(this);
        database.openDatabase();
        database.insertProduct(product);
        database.close();
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == switchCompatTakeUnits.getId())
            if (b) {
                findViewById(R.id.layoutTakeUnitsSwitch).setVisibility(View.VISIBLE);
                product.setLastUpdate(new Date());
                product.setConsumeUnits(Integer.parseInt(unitsDescontar.getText().toString()));
                product.setConsumeCycle(Integer.parseInt(unitsDays.getText().toString()));
            } else {
                findViewById(R.id.layoutTakeUnitsSwitch).setVisibility(View.GONE);

                product.setLastUpdate(null);
            }
        else if (compoundButton.getId() == switchCompatAddToShoppingList.getId()) {
            if (b) {
                findViewById(R.id.layoutAddToShoppingListSwitch).setVisibility(View.VISIBLE);
                product.setMinStock(Integer.parseInt(unitsWhenHave.getText().toString()));
                product.setUnitsToAdd(Integer.parseInt(unitsAddWhenHave.getText().toString()));
            } else {
                findViewById(R.id.layoutAddToShoppingListSwitch).setVisibility(View.GONE);
                product.setMinStock(-1);
            }
        }
        if (!newProduct)
            guardarDatos();
        showDatabaseData();
    }

    public void accept(View view) {
        guardarDatos();
        Intent i = new Intent();
        i.putExtra(PRODUCT, product);
        setResult(RESULT_OK, i);
        finish();
    }

    public void cancel(View view) {
        finish();
    }

    private void deleteProduct() {
        product.setStock(-1);
        product.setShoppingListUnits(0);
        product.setCartUnits(0);

        ProductDataSource database = new ProductDataSource(this);
        database.openDatabase();
        database.deleteProduct(product.getCode());
        database.close();

        //guardarDatos();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (!newProduct) {
                Intent i = new Intent();
                i.putExtra(PRODUCT, product);
                setResult(RESULT_OK, i);
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
