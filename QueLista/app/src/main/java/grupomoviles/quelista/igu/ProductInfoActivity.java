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
        product.increaseStock();
        makeChanges(unitsPantry,"",true);
    }

    public void disminuirPantry(View view) {
        product.decreaseStock();
        makeChanges(unitsPantry,"",false);
    }

    public void aumentarShoppingList(View view) {
        
        makeChanges(unitsLista, "", true);
    }

    public void disminuirShoppingList(View view) {

        makeChanges(unitsLista,"",false);

    }

    public void aumentarCartList(View view) {

        makeChanges(unitsCarrito, "", true);
    }

    public void disminuirCartList(View view) {

        makeChanges(unitsCarrito, "", false);

    }

    public void aumentarUnitsDescontar(View view) {

        makeChanges(unitsDescontar, " unidad", true);

    }

    public void disminuirUnitsDescontar(View view) {

        makeChanges(unitsDescontar, " unidad", false);
    }

    public void aumentarDays(View view) {

        makeChanges(unitsDays, " día", true);
    }


    public void disminuirDays(View view) {

        makeChanges(unitsDays, " día", false);
    }

    public  void aumentarWhenHave(View view){
        makeChanges(unitsWhenHave, " unidades", true);
    }

    public  void disminuirWhenHave(View view){
        makeChanges(unitsWhenHave, " unidades", false);
    }

    public  void aumentarAddWhenHave(View view){
        makeChanges(unitsAddWhenHave, " unidad", true);
    }

    public  void disminuirAddWhenHave(View view) {
        makeChanges(unitsAddWhenHave, " unidad", false);
    }

    private void makeChanges(TextView textView,String old,boolean sum){
        String texto =  textView.getText().toString();
        texto = texto.replace(old,"");
        int units = Integer.parseInt(texto);
        if(sum)
            units++;
        else
        {
            if(units > 0)
                units--;
        }
        textView.setText(units + old);
        guardarDatos();
    }

    private void guardarDatos(){
        ProductDataSource database = new ProductDataSource(this);
        database.openDatabase();
        database.update(product);
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
