package grupomoviles.quelista.logic;

import android.content.Context;

import com.annimon.stream.Stream;
import com.mongodb.util.Hash;

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
public class Cart {

    private Context context;

    private Map<String, Product> products = new HashMap<String, Product>();

    public Cart(Context context){
        this.context = context;
    }

    public Map<String, Product> getProducts() {
        return products;
    }

    public boolean onResultProductInfoActivity(Product product) {
        products.remove(product.getCode());

        if (product.getCartUnits() > 0) {
            products.put(product.getCode(), product);
            return true;
        }

        return false;
    }

    public void remove(Product product) {
        product.setCartUnits(Product.NOT_IN_CART);
        products.remove(product.getCode());

        ProductDataSource database = new ProductDataSource(context);
        database.openDatabase();

        if(product.getStock() == Product.NOT_IN_PANTRY && product.getCartUnits() == Product.NOT_IN_CART && product.getShoppingListUnits() == Product.NOT_IN_SHOPPING_LIST)
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
            if (product.getCartUnits() != Product.NOT_IN_CART)
                products.put(product.getCode(), product);

    }

    public void acceptShopCart() {
        ProductDataSource database = new ProductDataSource(context);
        database.openDatabase();

        Stream.of(products.values()).forEach(p -> {
            p.setStock(p.getStock() + p.getCartUnits());
            p.setCartUnits(Product.NOT_IN_CART);
            ((MainActivity)context).getShoppingListAdapter().getShoppingList().remove(p);
            ((MainActivity)context).getShoppingListAdapter().getShoppingList().refresh();
            database.update(p);
        });

        products.clear();

        database.close();
    }
}
