package grupomoviles.quelista.logic;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Nauce on 28/12/15.
 */
public class Pantry {

    private Set<Product> products = new HashSet<Product>();

    public Set<Product> getProducts() {
        return products;
    }

    public boolean onResultProductInfoActivity(Product product) {
        products.remove(product);

        if (product.getStock() > Product.NOT_IN_PANTRY)
            return products.add(product);

        return false;
    }

    public void remove(Product product) {
        products.remove(product);
    }
}
