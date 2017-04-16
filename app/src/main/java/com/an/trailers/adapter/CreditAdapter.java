package com.an.trailers.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.an.trailers.Constants;
import com.an.trailers.R;
import com.an.trailers.model.Cast;
import com.an.trailers.model.Crew;
import com.bumptech.glide.Glide;

import java.util.List;

public class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.ViewHolder> {

    private Context context;
    private String type;
    private List<Cast> casts;
    private List<Crew> crews;
    public CreditAdapter(Context context, String type, List<Cast> casts, List<Crew> crews) {
        this.context = context;
        this.type = type;
        this.casts = casts;
        this.crews = crews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.credits_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(isCast()) {
            Cast cast = casts.get(position);
            Glide.with(context).load(String.format(Constants.IMAGE_URL, cast.getProfilePath())).error(R.drawable.profile_placeholder).into(holder.imageView);
            holder.nameTxt.setText(cast.getName());
            holder.infoTxt.setText(cast.getCharacter());
        } else {
            Crew crew = crews.get(position);
            Glide.with(context).load(String.format(Constants.IMAGE_URL, crew.getProfilePath())).error(R.drawable.profile_placeholder).into(holder.imageView);
            holder.nameTxt.setText(crew.getName());
            holder.infoTxt.setText(crew.getJob());
        }
    }

    @Override
    public int getItemCount() {
        if(isCast())
            return casts.size();
        return crews.size();
    }

    public Boolean isCast() {
        if(type.equalsIgnoreCase(Constants.CREDIT_CAST))
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView nameTxt;
        private TextView infoTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.profile_image);
            nameTxt = (TextView) itemView.findViewById(R.id.txt_name);
            infoTxt = (TextView) itemView.findViewById(R.id.txt_info);
        }
    }
}
