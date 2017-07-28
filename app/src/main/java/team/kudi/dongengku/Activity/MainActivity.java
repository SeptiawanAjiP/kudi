package team.kudi.dongengku.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import team.kudi.dongengku.Handler.CustomTypefaceSpan;
import team.kudi.dongengku.R;


public class MainActivity extends AppCompatActivity {

    //final ArrayList<MenuItem> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/Moon-Bold.otf");
        SpannableStringBuilder SS = new SpannableStringBuilder(this.getTitle());
        SS.setSpan(new CustomTypefaceSpan("", font2), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(SS);

        RelativeLayout rl1 = (RelativeLayout) findViewById(R.id.buku_dongeng);
        ImageView iv1 = (ImageView) findViewById(R.id.menu_backgroud_ld);
        Picasso.with(this)
                .load(R.drawable.favorite_books)
                .into(iv1);
        iv1.setColorFilter(ContextCompat.getColor(this,R.color.blue_1),PorterDuff.Mode.MULTIPLY);
        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, DongengListActivity.class);
                startActivity(in);
            }
        });
        RelativeLayout rl2 = (RelativeLayout) findViewById(R.id.taman_baca);
        ImageView iv2 = (ImageView) findViewById(R.id.menu_backgroud_taman);
        Picasso.with(this)
                .load(R.drawable.taman_baca_kudi)
                .into(iv2);
        iv2.setColorFilter(ContextCompat.getColor(this,R.color.green_1),PorterDuff.Mode.MULTIPLY);
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, TentangKudiActivity.class);
                startActivity(in);
            }
        });

        RelativeLayout rl3 = (RelativeLayout) findViewById(R.id.ikut_donasi);
        ImageView iv3 = (ImageView) findViewById(R.id.menu_backgroud_donasi);
        Picasso.with(this)
                .load(R.drawable.donate_hand)
                .into(iv3);
        iv3.setColorFilter(ContextCompat.getColor(this,R.color.pink_1),PorterDuff.Mode.MULTIPLY);
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, DonasiKudiActivity.class);
                startActivity(in);
            }
        });
        RelativeLayout rl4 = (RelativeLayout) findViewById(R.id.dongeng_fav);
        ImageView iv4 = (ImageView) findViewById(R.id.menu_backgroud);
        Picasso.with(this)
                .load(R.drawable.taman_baca)
                .into(iv4);
        iv4.setColorFilter(ContextCompat.getColor(this,R.color.white),PorterDuff.Mode.MULTIPLY);
        rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, DongengFavActivity.class);
                startActivity(in);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Intent in = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(in);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean hasActiveInternetConnection() {
        final boolean[] ok = {false};

                if (isNetworkAvailable()) {
                    try {
                        URL url = new URL("http://www.google.com/");
                        HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                        urlc.setRequestProperty("User-Agent", "test");
                        urlc.setRequestProperty("Connection", "close");
                        urlc.setConnectTimeout(3000); // mTimeout is in seconds
                        urlc.connect();
                        if (urlc.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            ok[0] = true;
                        }
                    } catch (IOException e) {
                        Log.i("warning", "Error checking internet connection", e);
                        ok[0] = false;
                    }
                }
                ok[0] = false;
        return ok[0];
    }
}
