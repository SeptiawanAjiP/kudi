package team.kudi.dongengku.Object;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

/**
 * Created by user on 3/26/2017.
 */

public class MenuItem {
    private String judul;
    private int tintColor;
    private int idDrawable;

    public MenuItem(String judul, int idImageResource, int tintColor){
        this.judul = judul;
        this.idDrawable = idImageResource;
        this.tintColor = tintColor;
    }

    public int getIdDrawable() {
        return idDrawable;
    }

    public int getTintColor() {
        return tintColor;
    }

    public String getJudul() {
        return judul;
    }
}
