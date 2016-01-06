package grupomoviles.quelista.logic;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pablo on 06/01/2016.
 */
public class Ticket {

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
        product.setStock(Product.NOT_IN_PANTRY);
        products.remove(product.getCode());
    }

    public Product find(String code) {
        return products.get(code);
    }

}
