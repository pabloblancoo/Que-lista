package grupomoviles.quelista.logic;

import com.annimon.stream.Stream;

import java.util.Date;
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
        product.setStock(Product.NOT_IN_PANTRY);
        products.remove(product.getCode());
    }

    public Product find(String code) {
        return products.get(code);
    }

    public void actualizar() {
//        if(!products.isEmpty())
        long currentDate = new Date().getTime();

        Stream.of(products).forEach(m -> {
            if (m.getValue().getLastUpdate() != null) {
                Long productLastDate = m.getValue().getLastUpdate().getTime();
                long time = currentDate - productLastDate;
                long hours = time / 1000 / 60 / 60;
                long hoursConsume = m.getValue().getConsumeCycle() * 24;
                if (hours >= hoursConsume) {
                    if (m.getValue().getStock() >= m.getValue().getConsumeUnits()) {
                        m.getValue().setStock(m.getValue().getStock() - m.getValue().getConsumeUnits());

                    } else {
                        m.getValue().setStock(0);
                    }
                    m.getValue().setLastUpdate(new Date()); 
                }
            }
        });

    }
}
