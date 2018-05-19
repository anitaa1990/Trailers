package com.an.trailers.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.an.trailers.Constants;
import com.an.trailers.R;
import com.an.trailers.databinding.CreditListWithItemBinding;
import com.an.trailers.model.Cast;
import com.an.trailers.model.Crew;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class CreditListAdapter extends RecyclerView.Adapter<CreditListAdapter.CustomViewHolder> {

    private String type;
    private Context context;
    private List<Cast> casts;
    private List<Crew> crews;
    public CreditListAdapter(Context context, String type, List<Cast> casts, List<Crew> crews) {
        this.context = context;
        this.type = type;
        this.casts = casts;
        this.crews = crews;
    }


    public CreditListAdapter(Context context, String type) {
        this.type = type;
        this.context = context;
        this.casts = Collections.emptyList();
        this.crews = Collections.emptyList();
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CreditListWithItemBinding itemBinding = CreditListWithItemBinding.inflate(layoutInflater, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(itemBinding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        if(isCast()) {
            Cast cast = getCastItem(position);
            Picasso.get().load(String.format(Constants.IMAGE_URL, cast.getProfilePath())).error(R.drawable.profile_placeholder).into(holder.binding.profileImage);
            holder.binding.txtName.setText(cast.getName());
            holder.binding.txtInfo.setText(cast.getCharacter());

        } else {
            Crew crew = getCrewItem(position);
            Picasso.get().load(String.format(Constants.IMAGE_URL, crew.getProfilePath())).error(R.drawable.profile_placeholder).into(holder.binding.profileImage);
            holder.binding.txtName.setText(crew.getName());
            holder.binding.txtInfo.setText(crew.getJob());
        }
    }

    @Override
    public int getItemCount() {
        if(isCast()) return casts.size();
        return crews.size();
    }

    public Boolean isCast() {
        if(type.equalsIgnoreCase(Constants.CREDIT_CAST))
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    public Cast getCastItem(int position) {
        return casts.get(position);
    }

    public Crew getCrewItem(int position) {
        return crews.get(position);
    }


    public void setCasts(List<Cast> casts) {
        this.casts = casts;
        notifyDataSetChanged();
    }

    public void setCrews(List<Crew> crews) {
        this.crews = crews;
        notifyDataSetChanged();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private CreditListWithItemBinding binding;

        public CustomViewHolder(CreditListWithItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
