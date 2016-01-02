package grupomoviles.quelista.igu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
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
    public static final int REQUEST_CODE = 1;

    ImageView productImage;

    TextView description;
    TextView brand;
    TextView netValue;
    TextView category;

    TextView unitsPantry;
    TextView unitsLista;
    TextView unitsCarrito;
    TextView unitsDescontar;
    TextView unitsDays;
    TextView unitsWhenHave;
    TextView unitsAddWhenHave;
    SwitchCompat switchCompatTakeUnits;
    SwitchCompat switchCompatAddToShoppingList;
    Button buttonPlusDescontar;
    Button buttonMinusDescontar;
    Button buttonPlusDays;
    Button buttonMinusDays;

    Button buttonPlusWhenHave;
    Button buttonMinusWhenHave;
    Button buttonPlusAddWhenHave;
    Button buttonMinusAddWhenHave;

    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_info));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        product = (Product) getIntent().getExtras().get(PRODUCT);

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

        //Eventos
        switchCompatTakeUnits.setOnCheckedChangeListener(this);
        switchCompatAddToShoppingList.setOnCheckedChangeListener(this);

        showAllProductProperties();
    }

    private void showAllProductProperties() {

        productImage.setImageBitmap(product.getImage(getApplicationContext()));

        description.setText(product.getDescription());
        brand.setText(product.getBrand());
        netValue.setText(product.getNetValue());
        category.setText(product.getCategory());


        unitsPantry.setText(product.getStock() + "");
        unitsLista.setText(product.getShoppingListUnits() + "");
        unitsCarrito.setText(product.getCartUnits() + "");

        Date date = product.getLastUpdate();
        if (date == null) {
            findViewById(R.id.layoutTakeUnitsSwitch).setVisibility(View.GONE);
            switchCompatTakeUnits.setChecked(false);
        } else {
            switchCompatTakeUnits.setChecked(true);
            unitsDescontar.setText(product.getConsumeUnits() + " unidad");
            unitsDays.setText(product.getConsumeCycle() + " día");
        }

        if (product.getMinStock() == -1) {
            findViewById(R.id.layoutAddToShoppingListSwitch).setVisibility(View.GONE);
            switchCompatAddToShoppingList.setChecked(false);
        } else {
            switchCompatAddToShoppingList.setChecked(true);
            unitsWhenHave.setText(product.getMinStock() + " unidades");
            unitsAddWhenHave.setText(product.getUnitsToAdd() + " unidad");
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
                Intent i = new Intent();
                i.putExtra(PRODUCT, product);
                setResult(RESULT_OK, i);
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void aumentarPantry(View view) {
        product.increaseStock();
        makeChanges(unitsPantry, "", true);
    }

    public void disminuirPantry(View view) {
        product.decreaseStock();
        makeChanges(unitsPantry, "", false);
    }

    public void aumentarShoppingList(View view) {
        product.increaseShoppingListUnits();
        makeChanges(unitsLista, "", true);
    }

    public void disminuirShoppingList(View view) {
        product.decreaseShoppingListUnits();
        makeChanges(unitsLista, "", false);

    }

    public void aumentarCartList(View view) {
        product.increaseCartUnits();
        makeChanges(unitsCarrito, "", true);
    }

    public void disminuirCartList(View view) {
        product.decreaseCartUnits();
        makeChanges(unitsCarrito, "", false);

    }

    public void aumentarUnitsDescontar(View view) {
        product.increaseConsumeUnits();
        makeChanges(unitsDescontar, " unidad", true);

    }

    public void disminuirUnitsDescontar(View view) {
        product.decreaseConsumeUnits();
        makeChanges(unitsDescontar, " unidad", false);
    }

    public void aumentarDays(View view) {
        product.increaseConsumeCycle();
        makeChanges(unitsDays, " día", true);
    }


    public void disminuirDays(View view) {
        product.decreaseConsumeCycle();
        makeChanges(unitsDays, " día", false);
    }

    public void aumentarWhenHave(View view) {
        product.increaseMinStock();
        makeChanges(unitsWhenHave, " unidades", true);
    }

    public void disminuirWhenHave(View view) {
        product.decreaseMinStock();
        makeChanges(unitsWhenHave, " unidades", false);
    }

    public void aumentarAddWhenHave(View view) {
        product.increaseUnitsToAdd();
        makeChanges(unitsAddWhenHave, " unidad", true);
    }

    public void disminuirAddWhenHave(View view) {
        product.decreaseUnitsToAdd();
        makeChanges(unitsAddWhenHave, " unidad", false);
    }

    private void makeChanges(TextView textView, String old, boolean sum) {
        String texto = textView.getText().toString();
        texto = texto.replace(old, "");
        int units = Integer.parseInt(texto);
        if (sum)
            units++;
        else {
            if (units > 0)
                units--;
        }
        textView.setText(units + old);
        guardarDatos();
        showDatabaseData();

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
                        "; Minstock: " + p.getMinStock()));
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
                product.setConsumeUnits(Integer.parseInt(unitsDescontar.getText().toString().replace(" unidad", "")));
                product.setConsumeCycle(Integer.parseInt(unitsDays.getText().toString().replace(" día", "")));
            } else {
                findViewById(R.id.layoutTakeUnitsSwitch).setVisibility(View.GONE);

                product.setLastUpdate(null);
            }
        else if (compoundButton.getId() == switchCompatAddToShoppingList.getId()) {
            if (b) {
                findViewById(R.id.layoutAddToShoppingListSwitch).setVisibility(View.VISIBLE);
                product.setMinStock(Integer.parseInt(unitsWhenHave.getText().toString().replace(" unidades", "")));
                product.setUnitsToAdd(Integer.parseInt(unitsAddWhenHave.getText().toString().replace(" unidad", "")));
            } else {
                findViewById(R.id.layoutAddToShoppingListSwitch).setVisibility(View.GONE);
                product.setMinStock(-1);
            }
        }
        guardarDatos();
        showDatabaseData();
    }
}
