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
public class ShoppingList {

    private Context context;
    private Map<String, Product> products = new HashMap<String, Product>();

    public ShoppingList(Context context) {
        this.context = context;
    }

    public Map<String, Product> getProducts() {
        return products;
    }

    public boolean onResultProductInfoActivity(Product product) {
        products.remove(product.getCode());

        if (product.getShoppingListUnits() > 0) {
            products.put(product.getCode(), product);
            return true;
        }

        return false;
    }

    public void remove(Product product) {
        product.setShoppingListUnits(Product.NOT_IN_SHOPPING_LIST);
        products.remove(product.getCode());

        ProductDataSource database = new ProductDataSource(context);
        database.openDatabase();

        if (product.getStock() == Product.NOT_IN_PANTRY && product.getCartUnits() == Product.NOT_IN_CART && product.getShoppingListUnits() == Product.NOT_IN_SHOPPING_LIST)
            database.deleteProduct(product.getCode());
        else
            database.update(product);

        database.close();
    }

    public Product find(String content) {
        return products.get(content);
    }

    public void add(Product product) {
        products.put(product.getCode(), product);
    }

    public void refresh() {
        long currentDate = new Date().getTime();

        Stream.of(products).forEach(m -> {
            m.getValue().spendUnits();
        });
    }

    public void onResultNfcActivity(Product product) {
        Product p = products.get(product.getCode());

        if (p == null)
            if (product.getShoppingListUnits() != Product.NOT_IN_SHOPPING_LIST)
                products.put(product.getCode(), product);

    }
}
