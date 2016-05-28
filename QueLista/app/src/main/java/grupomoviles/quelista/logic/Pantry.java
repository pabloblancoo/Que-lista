package grupomoviles.quelista.logic;

import android.content.Context;

import com.annimon.stream.Stream;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import grupomoviles.quelista.localDatabase.ProductDataSource;

/**
 * Created by Nauce on 28/12/15.
 */
public class Pantry {

    private Map<String, Product> products = new HashMap<String, Product>();
    private Context context;

    public Pantry(Context context) {
        this.context = context;
    }

    public Map<String, Product> getProducts() {
        return products;
    }

    public boolean onResultProductInfoActivity(Product product) {
        products.remove(product.getCode());

        if (product.getStock() > Product.NOT_IN_PANTRY) {
            products.put(product.getCode(), product);
            return true;
        }

        return false;
    }

    public void remove(Product product) {
        product.setStock(Product.NOT_IN_PANTRY);
        products.remove(product.getCode());

        ProductDataSource database = new ProductDataSource(context);
        database.openDatabase();

        if (product.getStock() == Product.NOT_IN_PANTRY && product.getCartUnits() == Product.NOT_IN_CART && product.getShoppingListUnits() == Product.NOT_IN_SHOPPING_LIST)
            database.deleteProduct(product.getCode());
        else
            database.update(product);

        database.close();
    }

    public Product find(String code) {
        return products.get(code);
    }

    public void refresh() {
        long currentDate = new Date().getTime();

        Stream.of(products).forEach(m -> {
            m.getValue().spendUnits();
        });
    }

    public void onResultNfcActivity(Product product) {
        Product p = products.get(product.getCode());

        ProductDataSource database = new ProductDataSource(context);
        database.openDatabase();

        if (p != null) {
            p.setStock(p.getStock() + (product.getStock() * p.getUnits()));
            database.update(p);
        } else {
            products.put(product.getCode(), product);
            database.insertProduct(product);
        }

        database.close();
    }
}
