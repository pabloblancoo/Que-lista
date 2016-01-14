package grupomoviles.quelista.logic;

import android.content.Context;

import com.annimon.stream.Stream;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import grupomoviles.quelista.igu.MainActivity;
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

        if(product.getStock() == Product.NOT_IN_PANTRY && product.getCartUnits() == Product.NOT_IN_CART && product.getShoppingListUnits() == Product.NOT_IN_SHOPPING_LIST)
            new ProductDataSource(context).deleteProduct(product.getCode());
        else
            new ProductDataSource(context).update(product);
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
}
