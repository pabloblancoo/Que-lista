package grupomoviles.quelista.igu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import grupomoviles.quelista.R;
import grupomoviles.quelista.logic.Product;
import grupomoviles.quelista.onlineDatabase.GetProducts;

public class NFCActivity extends AppCompatActivity {

    public static final String PRODUCTS = "products";
    public static final int REQUEST_CODE = 6;

    List<Product> products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ArrayList lineas = (ArrayList) getIntent().getExtras().get(PRODUCTS);
        int firstProduct  = 1;
        products = new ArrayList<Product>();
        String[] codes = new String[lineas.size()];
        GetProducts getProduct = new GetProducts();
        for (int i = firstProduct; i < lineas.size() - 1 ; i++){

            String[] line = lineas.get(i).toString().split(";");
            codes[i-firstProduct] = line[0];
        }

        try {
            products = getProduct.execute(codes).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Stream.of(products).forEach(p ->
        { System.out.println("Codigo: " + p.getCode() + ", Descripcion: " + p.getDescription());
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_nfc);


    }

}
