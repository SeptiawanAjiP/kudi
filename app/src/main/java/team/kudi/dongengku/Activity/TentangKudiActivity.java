package team.kudi.dongengku.Activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.CirclePageIndicator;
import com.synnapps.carouselview.ViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import team.kudi.dongengku.Adapter.SimpleFragmentAdapter;
import team.kudi.dongengku.Handler.AppContoller;
import team.kudi.dongengku.Handler.CustomTypefaceSpan;
import team.kudi.dongengku.R;

public class TentangKudiActivity extends AppCompatActivity{

    private CarouselView tentangKudi;

    private Context mContext;
    private Toolbar mToolbar;
    private SmartTabLayout mSmartTabLayout;
    private ViewPager mViewPager;
    private String url = "http://iyasayang.esy.es/kudi/index.php/kegiatankudi/tentang_kudi";
    private List<String> tentangKudiImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentangkudi_2);

        Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/Moon-Bold.otf");
        final SpannableStringBuilder SS = new SpannableStringBuilder(this.getTitle());
        SS.setSpan(new CustomTypefaceSpan("", font2), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        SS.setSpan(new ForegroundColorSpan(Color.BLACK), 0, SS.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        tentangKudi = (CarouselView) findViewById(R.id.foto_kudi);

        mSmartTabLayout = (SmartTabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mViewPager.setAdapter(new SimpleFragmentAdapter(getSupportFragmentManager()));
        mSmartTabLayout.setViewPager(mViewPager);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle(SS);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        try {
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }catch (Exception e){
            Log.d("Donasi Kudi", "set Arrow eror");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.appBar);

        mSmartTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1){
                    mAppBarLayout.setExpanded(false, true);
                }else if (position == 0){
                    mAppBarLayout.setExpanded(true, true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray fotoKudi = response.getJSONArray("foto_tentang_kudi");
                            for (int i= 0; i<fotoKudi.length();i++){
                                tentangKudiImages.add(fotoKudi.getJSONObject(i).getString("path_foto"));
                            }
                            tentangKudi.setPageCount(tentangKudiImages.size());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("kontent eror", "Error: " + error.getMessage());
                    }
                });
        AppContoller.getInstance(this).addToRequestQueue(jsonObjectRequest);


        CirclePageIndicator indicator = (CirclePageIndicator) tentangKudi.findViewById(R.id.indicator);
        if(indicator !=null){
            indicator.setVisibility(View.GONE);
        }


        tentangKudi.setViewListener(new ViewListener() {
            @Override
            public View setViewForPosition(int position) {
                View customView = getLayoutInflater().inflate(R.layout.tentang_kudi, null);
                //set view attributes here
                ImageView iv = (ImageView) customView.findViewById(R.id.image_kegiatan);
                if (tentangKudiImages != null){
                    Picasso.with(getApplication())
                            .load(tentangKudiImages.get(position))
                            .into(iv);
                }
                return customView;
            }
        });

    }
}
