package team.kudi.dongengku.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import team.kudi.dongengku.Adapter.AdapterListDogeng;
import team.kudi.dongengku.Adapter.AdapterListFavDongeng;
import team.kudi.dongengku.Handler.DbHandler;
import team.kudi.dongengku.Handler.CustomTypefaceSpan;
import team.kudi.dongengku.Object.DongengItem;
import team.kudi.dongengku.R;

public class DongengFavActivity extends AppCompatActivity {

    private List<DongengItem> itemList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dongeng_fav);

        Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/Moon-Bold.otf");
        SpannableStringBuilder SS = new SpannableStringBuilder(this.getTitle());
        SS.setSpan(new CustomTypefaceSpan("", font2), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        SS.setSpan(new ForegroundColorSpan(Color.WHITE), 0, SS.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(SS);

        DbHandler db = DbHandler.getInstance(getApplicationContext());
        LinearLayout ln = (LinearLayout) findViewById(R.id.no_list);
        GridView gv = (GridView) findViewById(R.id.dongeng_item);

        itemList = db.getAllFavDongeng();
        if (itemList.size() == 0){
            ln.setVisibility(View.VISIBLE);
            gv.setVisibility(View.GONE);
        }else{
            ln.setVisibility(View.GONE);
            gv.setVisibility(View.VISIBLE);
        }

        gv.setAdapter(new AdapterListFavDongeng(this, itemList));
        gv.setEmptyView(ln);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id_dongeng = itemList.get(i).getIdDongeng();
                Intent in = new Intent(DongengFavActivity.this, DongengActivity.class);
                in.putExtra("id_judul",id_dongeng);
                startActivity(in);
            }
        });
    }
}
