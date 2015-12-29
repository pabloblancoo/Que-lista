package grupomoviles.quelista.onlineDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

import grupomoviles.quelista.logic.Product;


/**
 * Created by Nauce on 18/11/15.
 */
public class GestorBD {

    public static Product FindProduct(String barcode) throws ExecutionException, InterruptedException {
        return new GetProduct().execute(barcode).get();
    }

    public static List<Product> FindProducts(String... barcodes) throws ExecutionException, InterruptedException {
        return new GetProducts().execute(barcodes).get();
    }
}
