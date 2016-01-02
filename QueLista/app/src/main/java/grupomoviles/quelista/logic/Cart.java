package grupomoviles.quelista.logic;

import com.mongodb.util.Hash;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Nauce on 28/12/15.
 */
public class Cart {

    private Set<Product> products = new HashSet<Product>();

    public Set<Product> getProducts() {
        return products;
    }

    public void onResultProductInfoActivity(Product product) {
        products.remove(product);

        if (product.getCartUnits() > 0)
            products.add(product);
    }
}
