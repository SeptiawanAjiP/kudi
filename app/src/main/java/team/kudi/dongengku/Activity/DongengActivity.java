package team.kudi.dongengku.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.CirclePageIndicator;
import com.synnapps.carouselview.ViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import team.kudi.dongengku.Handler.AppContoller;
import team.kudi.dongengku.R;

public class DongengActivity extends AppCompatActivity {

    private final String url = "http://iyasayang.esy.es/kudi/index.php/dongeng/kontent/id_judul/";
    private List<String> isiDongeng = new ArrayList<String>();
    private TextView pagePos, pageCount;
    private LinearLayout pageIndicator;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dongeng);
        pageCount = (TextView) findViewById(R.id.page_count);
        pagePos = (TextView) findViewById(R.id.page_pos);
        pageIndicator = (LinearLayout) findViewById(R.id.page_indicator);
        String id_judul = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_judul = extras.getString("id_judul");
        }
        final CarouselView vp = (CarouselView) findViewById(R.id.kontent_view);
        CirclePageIndicator indicator = (CirclePageIndicator) vp.findViewById(R.id.indicator);
        if(indicator !=null){
            indicator.setVisibility(View.GONE);
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading . . .");
        progressDialog.show();

        Log.d("url kntent", url + id_judul);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url + id_judul, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray kontent = response.getJSONArray("kontent_dongeng");
                            for (int i = 0; i < kontent.length(); i++) {
                                String urlgambar = kontent.getJSONObject(i).getString("path_foto");
                                isiDongeng.add(urlgambar);
                                Log.e("url agambar", urlgambar);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("kontent", response.toString());
                        progressDialog.dismiss();
                        vp.setPageCount(isiDongeng.size());
                        pageCount.setText(String.valueOf(isiDongeng.size()));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("kontent eror", "Error: " + error.getMessage());
                        pageIndicator.setVisibility(View.GONE);
                        if(!isNetworkAvailable()){
                            Toast.makeText(context, "No Connection", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
        AppContoller.getInstance(this).addToRequestQueue(jsonObjectRequest);

        vp.setViewListener(new ViewListener() {
            @Override
            public View setViewForPosition(int position) {
                View customView = getLayoutInflater().inflate(R.layout.view_dongeng, null);
                final ProgressBar loadingGambar = (ProgressBar) customView.findViewById(R.id.gambar_progress);
                loadingGambar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);

                final ImageView gambar = (ImageView) customView.findViewById(R.id.gambar_dongeng);
                Picasso.with(getApplication())
                        .load(isiDongeng.get(position))
                        .into(gambar, new Callback() {
                            @Override
                            public void onSuccess() {
                                loadingGambar.setVisibility(View.GONE);
                                Log.d("Dongeng", "Image Load Succecfully");
                            }

                            @Override
                            public void onError() {
                                loadingGambar.setVisibility(View.GONE);
                                Log.d("Dongeng", "Image Load Succecfully");
                            }
                        });


                return customView;
            }
        });
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("position", String.valueOf(position));
                pagePos.setText(String.valueOf(position+1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
