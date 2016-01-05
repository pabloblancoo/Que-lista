package grupomoviles.quelista.logic;

import com.mongodb.util.Hash;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Nauce on 28/12/15.
 */
public class Cart {

    private Map<String, Product> products = new HashMap<String, Product>();

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
    }

    public Product find(String content) {
        return products.get(content);
    }

    public void add(Product product) {
        products.put(product.getCode(), product);
    }
}
