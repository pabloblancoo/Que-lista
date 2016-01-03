package grupomoviles.quelista.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Nauce on 22/12/15.
 */
public class Product implements Serializable {

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


    //Constructor con todos los valores inicializados, hay que borrarlo
    public  Product(String code){
        this.code = code;
        description = "";
        brand = "";
        netValue="";
        units = 1;  //(por ejemplo, la caja de yogures contiene 8 unidades. Por defecto 1)

        category="";
        subcategory="";
        stock = 0;		// (-1 == no)
        minStock=0;
        unitsToAdd=1;	//Unidades a añadir a la lista de la compra automáticamente (Def: 1)

        lastUpdate = new Date();	// (null == no añadir automáticamente)
        consumeCycle=2;
        consumeUnits=1;

        shoppingListUnits=1;
        cartUnits=1;

    }

    /**
     * Constructor con todos los parametros
     * @param code
     * @param description
     * @param brand
     * @param netValue
     * @param units
     * @param category
     * @param subcategory
     * @param stock
     * @param minStock
     * @param unitsToAdd
     * @param lastUpdate
     * @param consumeCycle
     * @param consumeUnits
     * @param shoppingListUnits
     * @param cartUnits
     */
    public Product(String code, String description, String brand, String netValue, int units, String category, String subcategory, int stock, int minStock, int unitsToAdd, Date lastUpdate, int consumeCycle, int consumeUnits, int shoppingListUnits, int cartUnits) {
        this.code = code;
        this.description = description;
        this.brand = brand;
        this.netValue = netValue;
        this.units = units;
        this.category = category;
        this.subcategory = subcategory;
        this.stock = stock;
        this.minStock = minStock;
        this.unitsToAdd = unitsToAdd;
        this.lastUpdate = lastUpdate;
        this.consumeCycle = consumeCycle;
        this.consumeUnits = consumeUnits;
        this.shoppingListUnits = shoppingListUnits;
        this.cartUnits = cartUnits;
    }

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

    public int increaseStock() {
        stock++;
        return stock;
    }

    public int decreaseStock() {
        if(stock >1)
            stock--;
        return stock;
    }

    public int increaseShoppingListUnits(){
        shoppingListUnits++;
        return  shoppingListUnits;
    }

    public int decreaseShoppingListUnits(){
        if(shoppingListUnits > 0 )
            shoppingListUnits--;
        return  shoppingListUnits;
    }

    public int increaseCartUnits(){
        cartUnits++;
        return  cartUnits;
    }

    public int decreaseCartUnits(){
        if(cartUnits > 0 )
        cartUnits--;
        return  cartUnits;
    }

    public int increaseConsumeCycle(){
        consumeCycle++;
        return  consumeCycle;
    }

    public int decreaseConsumeCycle(){
        if(consumeCycle > 0)
        consumeCycle--;
        return  consumeCycle;
    }

    public int increaseConsumeUnits(){
        consumeUnits++;
        return  consumeUnits;
    }

    public int decreaseConsumeUnits(){
       if(consumeUnits > 0)
        consumeUnits--;
        return  consumeUnits;
    }

    public int increaseUnitsToAdd(){
        unitsToAdd++;
        return  unitsToAdd;
    }

    public int decreaseUnitsToAdd(){
        if (unitsToAdd > 0)
        unitsToAdd--;
        return  unitsToAdd;
    }

    public int increaseMinStock(){
        minStock++;
        return  unitsToAdd;
    }

    public int decreaseMinStock(){
       if(minStock > 0)
        minStock--;
        return  unitsToAdd;
    }

    public Bitmap getImage(Context context) {
        Bitmap bitmap = null;

        try {
            FileInputStream fileInputStream =
                    new FileInputStream(context.getApplicationContext().getFilesDir().getPath()+ "/" + this.code + ".png");
            bitmap = BitmapFactory.decodeStream(fileInputStream);
        } catch (IOException io){
            io.printStackTrace();
        }

        return bitmap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return code.equals(product.code);

    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
