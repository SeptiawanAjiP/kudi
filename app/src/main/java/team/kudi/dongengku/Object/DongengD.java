package team.kudi.dongengku.Object;

/**
 * Created by user on 3/29/2017.
 */

public class DongengD {

    private String id;
    private String judul;
    private String deskripsi;
    private String favorited; // 0 or 1

    public DongengD(String id, String judul, String deskripsi, String favorited){
        this.deskripsi = deskripsi;
        this.favorited = favorited;
        this.id = id;
        this.judul = judul;
    }

    public String getJudul() {
        return judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getId() {
        return id;
    }

    public String getFavorited() {
        return favorited;
    }

    public Boolean isFavorited(){
        if (this.favorited.equals("1")) {
            return true;
        }else{
            return false;
        }
    }
}
