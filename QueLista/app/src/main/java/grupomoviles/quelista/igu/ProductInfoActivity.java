package grupomoviles.quelista.igu;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import grupomoviles.quelista.R;
import grupomoviles.quelista.localDatabase.ProductDataSource;
import grupomoviles.quelista.logic.Product;

public class ProductInfoActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_info));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        unitsPantry = (TextView) findViewById(R.id.txUnitsPantry);
        unitsLista = (TextView) findViewById(R.id.txUnitsShoppingList);
        unitsCarrito = (TextView) findViewById(R.id.txUnitsCart);
        unitsDescontar = (TextView) findViewById(R.id.txUnitsTakes);
        unitsDays = (TextView) findViewById(R.id.txDaysTakes);
        unitsWhenHave = (TextView) findViewById(R.id.txUnitsWhenHave);
        unitsAddWhenHave= (TextView) findViewById(R.id.txUnitsAddWhenHave);
        switchCompatTakeUnits = (SwitchCompat) findViewById(R.id.switchTakeUnits);
        switchCompatAddToShoppingList = (SwitchCompat) findViewById(R.id.switchAddToShoppingList);
        buttonPlusDescontar = (Button) findViewById(R.id.btnPlusUnitsTakes);
        buttonMinusDescontar = (Button) findViewById(R.id.btnMinusUnitsTakes);
        buttonPlusDays = (Button) findViewById(R.id.btnPlusDaysTakes);
        buttonMinusDays = (Button) findViewById(R.id.btnMinusDaysTakes);
        buttonPlusWhenHave = (Button) findViewById(R.id.btnPlusWhenHave);
        buttonMinusWhenHave = (Button) findViewById(R.id.btnMinusWhenHave);
        buttonPlusAddWhenHave = (Button) findViewById(R.id.btnPlusAddWhenHave);
        buttonMinusAddWhenHave = (Button) findViewById(R.id.btnMinusAddWhenHave);

       //Product p = (Product) savedInstanceState.get("product");
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

    public void activarDesactivarTakeUnits(View view) {
        if (switchCompatTakeUnits.isChecked())

            changeStateTakeUnits(true);
        else
            changeStateTakeUnits(false);
    }

    private void changeStateTakeUnits(Boolean value) {
        buttonPlusDays.setEnabled(value);
        buttonMinusDays.setEnabled(value);
        buttonPlusDescontar.setEnabled(value);
        buttonMinusDescontar.setEnabled(value);
    }

    public void activarDescativarWhenHave(View view){
        if (switchCompatAddToShoppingList.isChecked())

            changeStateWhenHave(true);
        else
            changeStateWhenHave(false);
    }

    private void changeStateWhenHave(Boolean value) {
        buttonPlusWhenHave.setEnabled(value);
        buttonMinusWhenHave.setEnabled(value);
        buttonPlusAddWhenHave.setEnabled(value);
        buttonMinusAddWhenHave.setEnabled(value);
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
}
