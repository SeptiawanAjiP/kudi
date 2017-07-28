package team.kudi.dongengku.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import team.kudi.dongengku.Handler.AppContoller;
import team.kudi.dongengku.Handler.CustomTypefaceSpan;
import team.kudi.dongengku.R;

public class DonasiKudiActivity extends AppCompatActivity {

    String no_telp ;
    String url = "http://iyasayang.esy.es/kudi/index.php/kegiatankudi/donasi";
    private List<String> image = new ArrayList<>();
    private Context mContext;
    private CarouselView fotoDonasi;
    private ProgressBar progresRekening;
    private TextView rekeningKudi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donasi_kudi);


        fotoDonasi = (CarouselView) findViewById(R.id.donasi_foto_kudi);
        progresRekening = (ProgressBar) findViewById(R.id.rekening_loading);
        rekeningKudi = (TextView) findViewById(R.id.rekening_kudi);

        progresRekening.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.red_1_dark), PorterDuff.Mode.SRC_IN);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            no_telp = response.getJSONObject("info_donasi").getString("kontak_sms");
                            String rekening = response.getJSONObject("info_donasi").getString("rekening");
                            rekeningKudi.setText(rekening);
                            no_telp = "62" + no_telp.substring(1);
                            JSONArray foto = response.getJSONArray("foto_tentang_kudi");
                            for (int i = 0; i<foto.length(); i++){
                                image.add(foto.getJSONObject(i).getString("path_foto"));
                            }
                            fotoDonasi.setPageCount(foto.length());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progresRekening.setVisibility(View.GONE);
                        rekeningKudi.setVisibility(View.VISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progresRekening.setVisibility(View.GONE);
                        VolleyLog.d("kontent eror", "Error: " + error.getMessage());
                    }
                });
        AppContoller.getInstance(this).addToRequestQueue(jsonObjectRequest);
        fotoDonasi.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Picasso.with(getApplication())
                        .load(image.get(position))
                        .into(imageView);
            }
        });



        Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/Moon-Bold.otf");
        SpannableStringBuilder SS = new SpannableStringBuilder(this.getTitle());
        SS.setSpan(new CustomTypefaceSpan("", font2), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle(SS);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        try {
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }catch (Exception e){
            Log.d("Donasi Kudi", "set Arrow eror");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;
        ImageView wa = (ImageView) findViewById(R.id.hub_wa);
        ImageView tel = (ImageView) findViewById(R.id.hub_telfon);
        ImageView sms = (ImageView) findViewById(R.id.hub_sms);

        wa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWhatsApp(no_telp, mContext);
            }
        });

        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callContact(no_telp, mContext);
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(no_telp, mContext);
            }
        });
    }

    public void callContact(String nomor, Context mContext){
        Uri uri = Uri.parse("tel:" + "+"+nomor);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);

        try {
            mContext.startActivity(it);
        }catch(android.content.ActivityNotFoundException ex){
            Toast.makeText(mContext,"Failed Call",Toast.LENGTH_SHORT).show();
        }
    }

    private void openWhatsApp(String smsNumber, Context mContext ) {

        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix
            mContext.startActivity(sendIntent);
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(mContext, "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();
            mContext.startActivity(goToMarket);
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = mContext.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public void sendMessage(String nomor, Context mContext){
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address"  , nomor);
        smsIntent.putExtra("sms_body"  , "");

        try {
            mContext.startActivity(smsIntent);

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(mContext,
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }
}
