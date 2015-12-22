package grupomoviles.quelista;

import java.util.Date;

/**
 * Created by Nauce on 22/12/15.
 */
public class Product {

    public static int NOT_IN_PANTRY = -1;

    private String code;
    private String description;
    private String brand;
    private String netValue;
    private int units = 1;  //(por ejemplo, la caja de yogures contiene 8 unidades. Por defecto 1)

    private String category;
    private String subcategory;

    private int stock = 0;		// (-1 == no)
    private int minStock;
    private int unitsToAdd;	//Unidades a añadir a la lista de la compra automáticamente (Def: 1)

    private Date lastUpdate;	// (null == no añadir automáticamente)
    private int consumeCycle;  //Período (a definir si van a ser días enteros)
    private int consumeUnits;  //Unidades a descontar cada período

    private int shoppingListUnits;	//Unidades en la lista de la compra (0 == no)
    private int cartUnits;		//Unidades en el carrito (0 == no)

    public Product(String code, String description, String brand, String netValue, String category, String subcategory) {
        this.code = code;
        this.description = description;
        this.brand = brand;
        this.netValue = netValue;
        this.category = category;
        this.subcategory = subcategory;
    }

    public Product(String code, String description, String brand, String netValue, String category, String subcategory, int units) {
        this.code = code;
        this.description = description;
        this.brand = brand;
        this.netValue = netValue;
        this.category = category;
        this.subcategory = subcategory;
        this.units = units;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }

    public String getNetValue() {
        return netValue;
    }

    public int getStock() {
        return stock;
    }
}
