package team.kudi.dongengku.Adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

import team.kudi.dongengku.Handler.DbHandler;
import team.kudi.dongengku.Object.DongengItem;
import team.kudi.dongengku.R;

/**
 * Created by user on 3/27/2017.
 */

public class AdapterListDogeng extends ArrayAdapter<DongengItem> {
    private Context mContext;

    public AdapterListDogeng(Context context, List<DongengItem> objects) {
        super(context, 0, objects);
        this.mContext = context;
    }

    @Override
    public void clear() {
        super.clear();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;


        final DongengItem item = getItem(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            listItemView = layoutInflater.inflate(R.layout.dongeng_list,parent,false);
        }

        ImageView dongengCover = (ImageView) listItemView.findViewById(R.id.dongeng_cover);

        Picasso.with(mContext)
                .load(item.getIdCoverResource())
                .into(dongengCover);
        TextView dongengJudul = (TextView) listItemView.findViewById(R.id.dongeng_judul);
        dongengJudul.setText(item.getJudulDongeng());

//        TextView dongengDesk = (TextView) listItemView.findViewById(R.id.dongeng_desk);
//        dongengDesk.setText(item.getDeskDongeng());

        final ImageView favorite = (ImageView) listItemView.findViewById(R.id.fav_image);
        final DbHandler db = DbHandler.getInstance(mContext);
        if(db.isFavorite(item.getIdDongeng())){
            favorite.setImageResource(R.drawable.ic_star_yellow);
        }else {
            favorite.setImageResource(R.drawable.ic_star_brown);
        }
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.isFavorite(item.getIdDongeng())){
                    boolean b = db.updateDongengFav(item, "0");
                    Log.e("update 0 "+item.getIdDongeng(), String.valueOf(b));
                    favorite.setImageResource(R.drawable.ic_star_brown);
                    notifyDataSetChanged();
                }else {
                    boolean b = db.updateDongengFav(item, "1");
                    Log.e("update 1 "+item.getIdDongeng(), String.valueOf(b));
                    favorite.setImageResource(R.drawable.ic_star_yellow);
                    notifyDataSetChanged();
                }

            }
        });

        return listItemView;
    }
}
