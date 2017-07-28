package team.kudi.dongengku.Object;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HD on 4/22/2017.
 */

public class GSON {

    @SerializedName("kegiatan_kudi")
    public List<DataKegiatan> dataKegiatanList;
    public class DataKegiatan {
        @SerializedName("id_kegiatan")
        public String id;
        @SerializedName("judul_kegiatan")
        public String judul;
        @SerializedName("tanggal")
        public String tanggal;
        @SerializedName("teks")
        public String teks;
        @SerializedName("foto_utama")
        public String foto;
    }
}
