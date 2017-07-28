package team.kudi.dongengku.Fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import team.kudi.dongengku.Handler.AppContoller;
import team.kudi.dongengku.R;

/**
 * Created by HD on 4/8/2017.
 */

public class TestFragment extends Fragment {

    private TextView alamatKudi;
    private TextView websiteKudi;
    private TextView intagramKudi;
    private ProgressBar alamatLoading;
    private ProgressBar websiteLoading;
    private ProgressBar instagramLoading;

    final String url ="http://iyasayang.esy.es/kudi/index.php/kegiatankudi/tentang_kudi";
    public TestFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.test_layout,
                container, false);
        inisiate(rootView);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject tentangKudiJson = null;
                        try {
                            tentangKudiJson = response.getJSONObject("tentang_kudi");
                            String quotes = tentangKudiJson.getString("quotes");
                            String alamat = tentangKudiJson.getString("alamat");
                            String website = tentangKudiJson.getString("website");
                            String instagram = tentangKudiJson.getString("instagram");
                            if(quotes != ""){
                                goneVisible();
                            }
                            alamatKudi.setText(alamat);
                            intagramKudi.setText(instagram);
                            websiteKudi.setText(website);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        AppContoller.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
        return rootView;
    }

    private void inisiate(View view){
        alamatKudi = (TextView) view.findViewById(R.id.alamat_kudit);
        websiteKudi = (TextView) view.findViewById(R.id.website_kudi);
        intagramKudi = (TextView) view.findViewById(R.id.instagram_kudi);

        alamatLoading = (ProgressBar) view.findViewById(R.id.alamat_loading);
        websiteLoading = (ProgressBar) view.findViewById(R.id.website_loading);
        instagramLoading = (ProgressBar) view.findViewById(R.id.instagram_loading);

        alamatKudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });

        websiteKudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLinkWebsite();
            }
        });

        intagramKudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInstagram();
            }
        });
    }

    private void goneVisible(){
        instagramLoading.setVisibility(View.GONE);
        intagramKudi.setVisibility(View.VISIBLE);

        websiteLoading.setVisibility(View.GONE);
        websiteKudi.setVisibility(View.VISIBLE);

        alamatLoading.setVisibility(View.GONE);
        alamatKudi.setVisibility(View.VISIBLE);
    }

    private void openInstagram(){
        String igAccount = "tamanbacakudi";

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://instagram.com/_u/" +igAccount));
            intent.setPackage("com.instagram.android");
            startActivity(intent);
        }
        catch (android.content.ActivityNotFoundException anfe)
        {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/"+igAccount)));
        }
    }

    private void openLinkWebsite(){
        Intent i = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://tamanbacakudi.org"));
        startActivity(i);
    }

    private void openMap(){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com//maps?daddr=" + "-7.487481, 109.237569"));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }
}
