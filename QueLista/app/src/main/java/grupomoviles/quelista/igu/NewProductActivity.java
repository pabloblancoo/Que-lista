package grupomoviles.quelista.igu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import grupomoviles.quelista.R;
import grupomoviles.quelista.localDatabase.ProductDataSource;
import grupomoviles.quelista.logic.Product;

public class NewProductActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    public static final String PRODUCT = "PRODUCT";
    public static final String NEWPRODUCT = "NEWPRODUCT";
    public static final String NEWPRODUCTCODE = "NEWPRODUCTCODE";
    public static final int REQUEST_CODE = 22;
    private boolean newProduct = true;

    private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;

    private static final int FILE_SELECT_CODE = 0;

    ImageView productImage;

    EditText code;
    EditText description;
    EditText brand;
    EditText netValue;
    EditText category;
    EditText subcategory;


    TextInputLayout codeLayout;
    TextInputLayout descriptionLayout;
    TextInputLayout brandLayout;
    TextInputLayout netValueLayout;
    TextInputLayout categoryLayout;
    TextInputLayout subcategoryLayout;


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
    Date date = null;
    int minStock = -1;
    int unitsToAdd = 1;
    private boolean imagenTomada = false;
    String imagePath;
    private Bitmap bMapFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_info));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImage = (ImageView) findViewById(R.id.imgProduct);

        code = (EditText) findViewById(R.id.txCode);
        if (getIntent().hasExtra(NEWPRODUCTCODE) && getIntent().getExtras().get(NEWPRODUCTCODE) != null) {
            code.setText(getIntent().getExtras().get(NEWPRODUCTCODE).toString());
            code.setEnabled(false);
        }
        codeLayout = (TextInputLayout) findViewById(R.id.input_layout_txCategory);

        description = (EditText) findViewById(R.id.txDescription);
        brand = (EditText) findViewById(R.id.txBrand);
        netValue = (EditText) findViewById(R.id.txNetValue);
        category = (EditText) findViewById(R.id.txCategory);

        descriptionLayout = (TextInputLayout) findViewById(R.id.input_layout_txDescripcion);
        brandLayout = (TextInputLayout) findViewById(R.id.input_layout_txBrand);
        netValueLayout = (TextInputLayout) findViewById(R.id.input_layout_txNetValue);
        categoryLayout = (TextInputLayout) findViewById(R.id.input_layout_txCategory);

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                while (bitmap == null) {
                    bitmap = product.getImage(getApplicationContext());
                    productImage.setImageBitmap(bitmap);
                }
            }
        });

        productImage.setImageResource(R.drawable.defecto);

        unitsPantry.setText(0 + "");
        unitsLista.setText(0 + "");
        unitsCarrito.setText(0 + "");

        //Date date = product.getLastUpdate();
        Date date = null;
        if (date == null) {
            findViewById(R.id.layoutTakeUnitsSwitch).setVisibility(View.GONE);
            switchCompatTakeUnits.setChecked(false);
        } else {
            switchCompatTakeUnits.setChecked(true);
            unitsDescontar.setText(product.getConsumeUnits() + " unidad");
            unitsDays.setText(product.getConsumeCycle() + " día");
        }

        minStock = -1;
        if (minStock == -1) {
            findViewById(R.id.layoutAddToShoppingListSwitch).setVisibility(View.GONE);
            switchCompatAddToShoppingList.setChecked(false);
        } else {
            switchCompatAddToShoppingList.setChecked(true);
            unitsWhenHave.setText(0 + " unidades");
            unitsAddWhenHave.setText(0 + " unidad");
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
            case R.id.action_add:
                if (!validaciones())
                    return true;
                confirmar();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void finalizar() {
        guardarImagen();
        guardarDatos();
        Intent i = new Intent();
        i.putExtra(PRODUCT, product);
        setResult(RESULT_OK, i);
        onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_product, menu);
        return true;
    }

    public void aumentarPantry(View view) {
        makeChanges(unitsPantry, "", true);
    }

    public void disminuirPantry(View view) {
        makeChanges(unitsPantry, "", false);
    }

    public void aumentarShoppingList(View view) {
        makeChanges(unitsLista, "", true);
    }

    public void disminuirShoppingList(View view) {
        makeChanges(unitsLista, "", false);
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

    public void aumentarWhenHave(View view) {
        //int num = Integer.parseInt(unitsWhenHave.getText().toString());
        //unitsWhenHave.setText(String.valueOf(num++));
        makeChanges(unitsWhenHave, " unidades", true);
        minStock = Integer.parseInt(unitsWhenHave.getText().toString().replace(" unidades", ""));
    }

    public void disminuirWhenHave(View view) {
        //int num = Integer.parseInt(unitsWhenHave.getText().toString());
        //unitsWhenHave.setText(String.valueOf(num--));
        makeChanges(unitsWhenHave, " unidades", false);
        minStock = Integer.parseInt(unitsWhenHave.getText().toString().replace(" unidades", ""));
    }

    public void aumentarAddWhenHave(View view) {
        //int num = Integer.parseInt(unitsAddWhenHave.getText().toString());
        //unitsAddWhenHave.setText(String.valueOf(num++));
        makeChanges(unitsAddWhenHave, " unidad", true);
    }

    public void disminuirAddWhenHave(View view) {
        //int num = Integer.parseInt(unitsAddWhenHave.getText().toString());
        // unitsAddWhenHave.setText(String.valueOf(num--));
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

    }

    private void guardarDatos() {
        product = new Product(
                code.getText().toString(),
                description.getText().toString(),
                brand.getText().toString(),
                netValue.getText().toString(),
                category.getText().toString(),
                category.getText().toString()
        );

        product.setStock(Integer.parseInt(unitsPantry.getText().toString()));
        product.setShoppingListUnits(Integer.parseInt(unitsLista.getText().toString()));
        product.setCartUnits(Integer.parseInt(unitsCarrito.getText().toString()));


        if (date != null) {
            product.setLastUpdate(date);
            product.setConsumeUnits(Integer.parseInt(unitsDescontar.getText().toString().replace(" unidad", "")));
            product.setConsumeCycle(Integer.parseInt(unitsDays.getText().toString().replace(" día", "")));
        }
        if (minStock > -1) {
            product.setMinStock(minStock);
            product.setUnitsToAdd(Integer.parseInt(unitsAddWhenHave.getText().toString().replace(" unidad", "")));
        }

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
                date = new Date();
            } else {
                findViewById(R.id.layoutTakeUnitsSwitch).setVisibility(View.GONE);
                date = null;
            }
        else if (compoundButton.getId() == switchCompatAddToShoppingList.getId()) {
            if (b) {
                findViewById(R.id.layoutAddToShoppingListSwitch).setVisibility(View.VISIBLE);
                minStock = Integer.parseInt(unitsWhenHave.getText().toString().replace(" unidades", ""));
            } else {
                findViewById(R.id.layoutAddToShoppingListSwitch).setVisibility(View.GONE);
                minStock = -1;
            }
        }
    }


    public void confirmar() {
        String[] items = {"Sí", "No"};

        AlertDialog.Builder builder =
                new AlertDialog.Builder(NewProductActivity.this);

        builder.setTitle("¿Desea guardar el producto? ")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Log.i("Dialogos", "Opción elegida: " + items[item]);
                        if (items[item].equals("Sí")) {
                            finalizar();
                        }
                    }
                }).show();

    }


    public void changeImage(View v) {
        String[] items = {"Cámara", "Galería"};

        AlertDialog.Builder builder =
                new AlertDialog.Builder(NewProductActivity.this);

        builder.setTitle("Seleccione la fuente de origen de la imágen")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Log.i("Dialogos", "Opción elegida: " + items[item]);
                        if (items[item].equals("Galería")) {
                           galeria();
                        } else {
                            camara();

                        }
                    }
                }).show();

    }

    public void camara() {

        //Creamos el Intent para llamar a la Camara
        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        //Creamos una carpeta en la memoria del movil
        File imagesFolder = new File(
                Environment.getExternalStorageDirectory(), "QueLista");

        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs();
        }


        //añadimos el nombre de la imagen
        File image = new File(imagesFolder, "foto.jpg");
        Uri uriSavedImage = Uri.fromFile(image);

        //Le decimos al Intent que queremos grabar la imagen
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

        //Lanzamos la aplicacion de la camara con retorno (forResult)
        startActivityForResult(cameraIntent, TAKE_PICTURE);

    }

    private void galeria(){
        /*Intent i = new Intent(this, Galeria.class);
        startActivityForResult(i, Galeria.REQUEST_CODE);*/

        boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        try {
            if (isKitKat) {
                Intent galeryIntent = new Intent();
                //galeryIntent.setType("*/*");
                galeryIntent.setType("image/jpeg");
                galeryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(galeryIntent, SELECT_PICTURE);

            } else {
                Intent galeryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galeryIntent.setType("image/jpeg");
                startActivityForResult(galeryIntent, SELECT_PICTURE);
            }
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Por favor, instala un explorador de archivos",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean guardarImagen() {
        int height;
        int width;
        float aspectRatio;

        File imagesFolder = new File(
                Environment.getExternalStorageDirectory(), "QueLista");

        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs();
        }

        File to = new File(imagesFolder, code.getText().toString() + ".jpg");

        try {
            if(bMapFinal == null){
                bMapFinal = BitmapFactory.decodeResource(getResources(),R.drawable.defecto);
                productImage.setImageBitmap(bMapFinal);
                imagenTomada = false;
            }
            if(bMapFinal.getHeight()>=bMapFinal.getWidth()) {
                aspectRatio = bMapFinal.getHeight() /
                        (float) bMapFinal.getWidth();
                height = 250;
                width =  Math.round(height / aspectRatio);
            }
            else {
                aspectRatio = bMapFinal.getWidth() /
                        (float) bMapFinal.getHeight();
                width = 250;
                height =  Math.round(width / aspectRatio);
            }

            bMapFinal = Bitmap.createScaledBitmap(bMapFinal,
                    width, height, false);

            FileOutputStream fos = new FileOutputStream(to);
            bMapFinal.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();

        } catch (FileNotFoundException e) {
            Log.d("FILE", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("FILE", "Error accessing file: " + e.getMessage());
        }

        if(imagenTomada==true) {
            File from = new File(imagesFolder, "foto.jpg");
            from.delete();
        }

        return true;

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Comprovamos que la foto se a realizado
        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            //Creamos un bitmap con la imagen recientemente
            //almacenada en la memoria
            bMapFinal = BitmapFactory.decodeFile(
                    Environment.getExternalStorageDirectory() +
                            "/QueLista/" + "foto.jpg");
            //Añadimos el bitmap al imageView para
            //mostrarlo por pantalla
            productImage.setImageBitmap(bMapFinal);

            Log.i("IMAGEN", productImage.getMeasuredHeight() + "H");
            Log.i("IMAGEN", productImage.getMeasuredWidth()+"W");
            imagenTomada = true;
        }
        else if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            //Creamos un bitmap con la imagen recientemente
            //almacenada en la memoria
                //productImage.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
            Uri selectedImageUri = data.getData();
            imagePath = getRealPathFromURI(selectedImageUri);

            bMapFinal = BitmapFactory.decodeFile(imagePath);
            productImage.setImageBitmap(bMapFinal);
            imagenTomada = false;
        }

    }
    public String getRealPathFromURI(Uri contentURI) {
        Uri contentUri = contentURI;

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = null;
        try {
            if (Build.VERSION.SDK_INT > 19) {
                // Will return "image:x*"
                String wholeID = DocumentsContract.getDocumentId(contentUri);
                // Split at colon, use second item in the array
                String id = wholeID.split(":")[1];
                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";

                cursor = this.getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection, sel, new String[] { id }, null);
            } else {
                cursor = this.getContentResolver().query(contentUri,
                        projection, null, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String path = null;
        try {
            int column_index = cursor
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index).toString();
            cursor.close();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return path;
    }



        private boolean validaciones(){
        if(!validate(description, descriptionLayout)) return false;
        if(!validate(brand, brandLayout)) return false;
        if(!validate(netValue, netValueLayout)) return false;
        if(!validate(category, categoryLayout)) return false;
        if(!validate(code, codeLayout)) return false;

        return true;
    }

    private boolean validate(EditText editText, TextInputLayout textInputLayout) {
        if (editText.getText().toString().trim().isEmpty()) {

            textInputLayout.setError(getString(R.string.err_msg_vacio));
            editText.requestFocus();
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

}
