package team.kudi.dongengku.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import team.kudi.dongengku.Object.GSON;
import team.kudi.dongengku.Handler.AppContoller;
import team.kudi.dongengku.R;

/**
 * Created by HD on 4/11/2017.
 */

public class FragmentKegiatanKudi extends Fragment {
    private GSON gson;
    private String url = "http://iyasayang.esy.es/kudi/index.php/kegiatankudi/kegiatan/page/1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_kegiatan_kudi,
                container, false);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        final RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_kegiatan);
        mRecyclerView.setLayoutManager(mLayoutManager);


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Gson gson1 = new GsonBuilder().create();
                            gson = gson1.fromJson(response, GSON.class);
                            AdapterKegiatan kegiatan = new AdapterKegiatan(getActivity(),gson.dataKegiatanList);
                            mRecyclerView.setAdapter(kegiatan);
                        }
                        catch (Exception e){
                            Log.e("FragmentKegiatan", "eror on request", e);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("kontent eror", "Error: " + error.getMessage());
                    }
                });

        AppContoller.getInstance(getActivity()).addToRequestQueue(stringRequest);

        return rootView;
    }

    public class ViewHolderKegiatan extends RecyclerView.ViewHolder{
        protected TextView judul;
        protected ImageView gambar;
        protected TextView deskripsi;
        protected TextView tanggal;
        protected ProgressBar progres;
        //protected DilatingDotsProgressBar mDilatingDotsProgressBar;

        public ViewHolderKegiatan(View itemView) {
            super(itemView);
            progres = (ProgressBar) itemView.findViewById(R.id.progress);
           // mDilatingDotsProgressBar = (DilatingDotsProgressBar) itemView.findViewById(R.id.progress);
            judul = (TextView) itemView.findViewById(R.id.judul_kegiatan_kudi);
            gambar = (ImageView) itemView.findViewById(R.id.image_kegiatan_kudi);
            deskripsi = (TextView) itemView.findViewById(R.id.des);
            tanggal = (TextView) itemView.findViewById(R.id.tanggal);
        }
    }

    public class AdapterKegiatan extends RecyclerView.Adapter<ViewHolderKegiatan>{
        private Context mContext;
        private List<GSON.DataKegiatan> dataKegiatanList;

        public AdapterKegiatan(Activity mContext, List<GSON.DataKegiatan> dataKegiatanList){
            this.mContext = mContext;
            this.dataKegiatanList = dataKegiatanList;
        }

        @Override
        public ViewHolderKegiatan onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.kegiatan_kudi_list, parent, false);

            return new ViewHolderKegiatan(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderKegiatan holder, int position) {
            holder.judul.setText(dataKegiatanList.get(position).judul);
//            Glide.with(getContext())
//                    .load(dataKegiatanList.get(position).foto)
//                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            holder.progres.setVisibility(View.GONE);
//                            holder.gambar.setVisibility(View.VISIBLE);
//                            return false;
//                        }
//                    })
//                    .into(holder.gambar);
            Picasso.with(getContext())
                    .load(dataKegiatanList.get(position).foto)
                    .into(holder.gambar, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progres.setVisibility(View.GONE);
                            holder.gambar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
            holder.deskripsi.setText(dataKegiatanList.get(position).teks);
            holder.tanggal.setText(dataKegiatanList.get(position).tanggal);
        }


        @Override
        public int getItemCount() {
            return dataKegiatanList.size();
        }
    }
}
