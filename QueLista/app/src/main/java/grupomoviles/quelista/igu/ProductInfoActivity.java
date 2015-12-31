package grupomoviles.quelista.igu;

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

import java.util.Date;

import grupomoviles.quelista.R;
import grupomoviles.quelista.localDatabase.ProductDataSource;
import grupomoviles.quelista.logic.Product;

public class ProductInfoActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    public static final String PRODUCT = "PRODUCT";

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

        description =  (TextView) findViewById(R.id.txDescription);
        brand =  (TextView) findViewById(R.id.txBrand);
        netValue =  (TextView) findViewById(R.id.txNetValue);
        category =  (TextView) findViewById(R.id.txCategory);

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

        unitsAddWhenHave= (TextView) findViewById(R.id.txUnitsAddWhenHave);
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


        unitsPantry.setText(product.getStock()+"");
        unitsLista.setText(product.getShoppingListUnits()+"");
        unitsCarrito.setText(product.getCartUnits()+"");

        Date date = product.getLastUpdate();
        if(date == null) {
            findViewById(R.id.layoutTakeUnitsSwitch).setVisibility(View.GONE);
            switchCompatTakeUnits.setChecked(false);
        }
        else{
            switchCompatTakeUnits.setChecked(true);
            unitsDescontar.setText(product.getConsumeUnits()+" unidad");
            unitsDays.setText(product.getConsumeCycle()+" día");
        }

        if(product.getMinStock() == -1){
            findViewById(R.id.layoutAddToShoppingListSwitch).setVisibility(View.GONE);
            switchCompatAddToShoppingList.setChecked(false);
        }
        else{
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
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void aumentarPantry(View view) {

        int units = Integer.parseInt((String) unitsPantry.getText());
        units++;
        unitsPantry.setText(units + "");
    }

    public void disminuirPantry(View view) {

        int units = Integer.parseInt((String) unitsPantry.getText());
        if (units > 0)
            units--;
        unitsPantry.setText(units + "");
    }

    public void aumentarShoppingList(View view) {

        int units = Integer.parseInt((String) unitsLista.getText());
        units++;
        unitsLista.setText(units + "");
    }

    public void disminuirShoppingList(View view) {

        int units = Integer.parseInt((String) unitsLista.getText());
        if (units > 0)
            units--;
        unitsLista.setText(units + "");
    }

    public void aumentarCartList(View view) {

        int units = Integer.parseInt((String) unitsCarrito.getText());
        units++;
        unitsCarrito.setText(units + "");
    }

    public void disminuirCartList(View view) {

        int units = Integer.parseInt((String) unitsCarrito.getText());
        if (units > 0)
            units--;
        unitsCarrito.setText(units + "");
    }

    public void aumentarUnitsDescontar(View view) {

        String texto = unitsDescontar.getText().toString();
        texto = texto.replace(" unidad", "");
        int units = Integer.parseInt(texto);
        units++;
        unitsDescontar.setText(units + " unidad");
    }

    public void disminuirUnitsDescontar(View view) {

        String texto = unitsDescontar.getText().toString();
        texto = texto.replace(" unidad", "");
        int units = Integer.parseInt(texto);
        if (units > 0)
            units--;
        unitsDescontar.setText(units + " unidad");
    }

    public void aumentarDays(View view) {

        String texto = unitsDays.getText().toString();
        texto = texto.replace(" día", "");
        int units = Integer.parseInt(texto);
        units++;
        unitsDays.setText(units + " día");
    }


    public void disminuirDays(View view) {

        String texto = unitsDays.getText().toString();
        texto = texto.replace(" día", "");
        int units = Integer.parseInt(texto);
        if (units > 0)
            units--;
        unitsDays.setText(units + " día");
    }

    public  void aumentarWhenHave(View view){
        String texto = unitsWhenHave.getText().toString();
        texto = texto.replace(" unidades", "");
        int units = Integer.parseInt(texto);
        units++;
        unitsWhenHave.setText(units + " unidades");
    }

    public  void disminuirWhenHave(View view){
        String texto = unitsWhenHave.getText().toString();
        texto = texto.replace(" unidades", "");
        int units = Integer.parseInt(texto);
        if(units>0)
        units--;
        unitsWhenHave.setText(units + " unidades");
    }

    public  void aumentarAddWhenHave(View view){
        String texto = unitsAddWhenHave.getText().toString();
        texto = texto.replace(" unidad", "");
        int units = Integer.parseInt(texto);
        units++;
        unitsAddWhenHave.setText(units + " unidad");
    }

    public  void disminuirAddWhenHave(View view){
        String texto = unitsAddWhenHave.getText().toString();
        texto = texto.replace(" unidad", "");
        int units = Integer.parseInt(texto);
        if(units>0)
            units--;
        unitsAddWhenHave.setText(units + " unidad");
    }

    private void guardar(){
        ProductDataSource database = new ProductDataSource(this);
        database.openDatabase();
        //database.insertProduct(product);
        database.close();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == switchCompatTakeUnits.getId())
            if (b)
                findViewById(R.id.layoutTakeUnitsSwitch).setVisibility(View.VISIBLE);
            else
                findViewById(R.id.layoutTakeUnitsSwitch).setVisibility(View.GONE);

        else if (compoundButton.getId() == switchCompatAddToShoppingList.getId())
            if (b)
                findViewById(R.id.layoutAddToShoppingListSwitch).setVisibility(View.VISIBLE);
            else
                findViewById(R.id.layoutAddToShoppingListSwitch).setVisibility(View.GONE);
    }
}
