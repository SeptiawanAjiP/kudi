package team.kudi.dongengku.Object;

/**
 * Created by user on 5/21/2017.
 */

public class TentangKudi {
    private int iconSrc;
    private String title;
    private String text;

    public TentangKudi(int iconSrc, String title, String text){
        this.iconSrc = iconSrc;
        this.text = text;
        this.title = title;
    }

    public int getIconSrc() {
        return iconSrc;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }
}
