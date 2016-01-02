package grupomoviles.quelista.logic;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Nauce on 28/12/15.
 */
public class ShoppingList {

    private Set<Product> products = new HashSet<Product>();

    public Set<Product> getProducts() {
        return products;
    }

    public void onResultProductInfoActivity(Product product) {
        products.remove(product);

        if (product.getShoppingListUnits() > 0)
            products.add(product);
    }
}
