package grupomoviles.quelista.igu;

import grupomoviles.quelista.R;

/**
 * Created by Alperi on 05/01/2016.
 */
public class Image {
    private String nombre;
    private int idDrawable;

    public Image (String nombre, int idDrawable) {
        this.nombre = nombre;
        this.idDrawable = idDrawable;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIdDrawable() {
        return idDrawable;
    }

    public int getId() {
        return nombre.hashCode();
    }

    public static Image[] ITEMS = {

            new Image("Jaguar F-Type 2015", R.drawable.wood2),
            new Image("Jaguar F-Type 2014", R.drawable.cereales_miel_pops)
    };

    /**
     * Obtiene item basado en su identificador
     *
     * @param id identificador
     * @return Coche
     */
    public static Image getItem(int id) {
        for (Image item : ITEMS) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
