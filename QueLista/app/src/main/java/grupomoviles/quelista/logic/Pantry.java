package grupomoviles.quelista.logic;

import com.annimon.stream.Stream;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Nauce on 28/12/15.
 */
public class Pantry {

    private Map<String, Product> products = new HashMap<String, Product>();

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
        products.remove(product.getCode());
    }

    public Product find(String code) {
        return products.get(code);
    }
}
