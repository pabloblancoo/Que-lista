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

    //GETTERS and SETTERS
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setNetValue(String netValue) {
        this.netValue = netValue;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public int getMinStock() {
        return minStock;
    }

    public void setMinStock(int minStock) {
        this.minStock = minStock;
    }

    public int getUnitsToAdd() {
        return unitsToAdd;
    }

    public void setUnitsToAdd(int unitsToAdd) {
        this.unitsToAdd = unitsToAdd;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getConsumeCycle() {
        return consumeCycle;
    }

    public void setConsumeCycle(int consumeCycle) {
        this.consumeCycle = consumeCycle;
    }

    public int getConsumeUnits() {
        return consumeUnits;
    }

    public void setConsumeUnits(int consumeUnits) {
        this.consumeUnits = consumeUnits;
    }

    public int getShoppingListUnits() {
        return shoppingListUnits;
    }

    public void setShoppingListUnits(int shoppingListUnits) {
        this.shoppingListUnits = shoppingListUnits;
    }

    public int getCartUnits() {
        return cartUnits;
    }

    public void setCartUnits(int cartUnits) {
        this.cartUnits = cartUnits;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }


    //Metodos de logica
    public int increaseUnits() {
        stock++;
        return stock;
    }

    public int decreaseUnits() {
        stock--;
        return stock;
    }


}
