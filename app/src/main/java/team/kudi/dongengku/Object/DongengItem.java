package team.kudi.dongengku.Object;

/**
 * Created by user on 3/27/2017.
 */

public class DongengItem {
    private String idDongeng;
    private String favorited;
    private String idCoverResource;
    private String judulDongeng;
    private int deskDongeng;
    private int likes;
    private String page;

    public DongengItem(String idDongeng, String page, String judulDongeng, int deskDongeng, int likes , String idCoverResource, String favorited){
        this.likes = likes;
        this.page = page;
        this.idDongeng = idDongeng;
        this.favorited = favorited;
        this.judulDongeng = judulDongeng;
        this.deskDongeng = deskDongeng;
        this.idCoverResource = idCoverResource;
    }

    public String getPage() {
        return page;
    }

    public int getLikes() {
        return likes;
    }

    public String getIdCoverResource() {
        return idCoverResource;
    }

    public int getDeskDongeng() {
        return deskDongeng;
    }

    public String getJudulDongeng() {
        return judulDongeng;
    }

    public String getFavorited() {
        return favorited;
    }

    public String getIdDongeng() {
        return idDongeng;
    }

    public Boolean isFavorited(){
        if (this.favorited.equals("1")) {
            return true;
        }else{
            return false;
        }
    }
}
