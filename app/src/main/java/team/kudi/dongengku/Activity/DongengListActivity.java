package team.kudi.dongengku.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import team.kudi.dongengku.Adapter.AdapterListDogeng;
import team.kudi.dongengku.Handler.AppContoller;
import team.kudi.dongengku.Handler.CustomTypefaceSpan;
import team.kudi.dongengku.Handler.DbHandler;
import team.kudi.dongengku.Object.DongengItem;
import team.kudi.dongengku.R;

public class DongengListActivity extends AppCompatActivity {

    private List<DongengItem> itemList = new ArrayList<>();
    final String url = "http://iyasayang.esy.es/kudi/index.php/dongeng/Identitas/page/";
    final String tag = "json-request-list";
    private GridView gv;
    private int pages = 1;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dongeng_list);
        final DbHandler db = DbHandler.getInstance(getApplicationContext());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading . . .");
        progressDialog.show();
        progressDialog.setCancelable(true);

        gv = (GridView) findViewById(R.id.dongeng_item);

        if (isNetworkAvailable()){
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    url+1, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray identitasDongeng = response.getJSONArray("identitas_dongeng");
                                int page = response.getInt("page");
                                for (int i = 0; i<identitasDongeng.length() ;i++){
                                    String judul = identitasDongeng.getJSONObject(i).getString("judul");
                                    String idJudul = identitasDongeng.getJSONObject(i).getString("id_judul");
                                    int likes = identitasDongeng.getJSONObject(i).getInt("likes");
                                    String image = identitasDongeng.getJSONObject(i).getString("path_photo");
                                    //String deskripsi = identitasDongeng.getJSONObject(i).getString("deskripsi");
                                    int desk = R.string.dongeng_1d;
                                    db.insertDongeng(new DongengItem(idJudul,String.valueOf(page), judul, desk, likes, image, "0"));

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e(tag, response.toString());
                            progressDialog.dismiss();
                            itemList = db.getDongengsByPage(String.valueOf(pages));
                            gv.setAdapter(new AdapterListDogeng(getApplicationContext(), itemList));

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(tag, "Error: " + error.getMessage());
                            Toast.makeText(getApplication(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                            itemList = db.getDongengsByPage(String.valueOf(pages));
                            gv.setAdapter(new AdapterListDogeng(getApplicationContext(), itemList));
                            // hide the progress dialog
                            progressDialog.dismiss();
                        }
                    });
            AppContoller.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }else{
            itemList = db.getDongengsByPage(String.valueOf(pages));
            gv.setAdapter(new AdapterListDogeng(getApplicationContext(), itemList));
            progressDialog.dismiss();
            Toast.makeText(getApplication(), "No Connection", Toast.LENGTH_SHORT).show();
        }


        final Button buttonPrev = (Button) findViewById(R.id.button_prev);
        Button buttonNext = (Button) findViewById(R.id.button_next);

        if (pages == 1){
            buttonPrev.setTextColor(getResources().getColor(R.color.clak));
            buttonPrev.setClickable(false);
        }
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pages += 1;
                getDongengListPage(pages, url);
                buttonPrev.setTextColor(getResources().getColor(R.color.colorAccent));
                buttonPrev.setClickable(true);

            }
        });


        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pages <= 0 || pages == 2 || pages == 1){
                    buttonPrev.setTextColor(getResources().getColor(R.color.clak));
                    buttonPrev.setClickable(false);
                    pages = 1;
                }else{
                    buttonPrev.setTextColor(getResources().getColor(R.color.colorAccent));
                    pages -= 1;
                }
                getDongengListPage(pages, url);
            }
        });




        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/Moon-Bold.otf");
        SpannableStringBuilder SS = new SpannableStringBuilder(this.getTitle());
        SS.setSpan(new CustomTypefaceSpan("", font2), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        SS.setSpan(new ForegroundColorSpan(Color.WHITE), 0, SS.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        getSupportActionBar().setTitle(SS);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id_dongeng = itemList.get(i).getIdDongeng();
                Intent in = new Intent(DongengListActivity.this, DongengActivity.class);
                in.putExtra("id_judul",id_dongeng);
                startActivity(in);
            }
        });
    }



    public void getDongengListPage(final int page, String url){
        final DbHandler db = DbHandler.getInstance(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url+page, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray identitasDongeng = response.getJSONArray("identitas_dongeng");
                            for (int i = 0; i<identitasDongeng.length() ;i++){
                                String judul = identitasDongeng.getJSONObject(i).getString("judul");
                                String idJudul = identitasDongeng.getJSONObject(i).getString("id_judul");
                                int likes = identitasDongeng.getJSONObject(i).getInt("likes");
                                String image = identitasDongeng.getJSONObject(i).getString("path_photo");
                                int desk = R.string.dongeng_1d;
                                db.addDongengItem(new DongengItem(idJudul,String.valueOf(page), judul, desk, likes, image, "0"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e(tag, response.toString());
                        itemList = db.getDongengsByPage(String.valueOf(page));
                        gv.setAdapter(new AdapterListDogeng(getApplicationContext(), itemList));


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(tag, "Error: " + error.getMessage());
                        // hide the progress dialog
                    }
                });
        AppContoller.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
