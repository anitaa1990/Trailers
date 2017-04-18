package com.an.trailers.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.an.trailers.Constants;
import com.an.trailers.R;
import com.an.trailers.activity.DetailActivity;
import com.an.trailers.activity.VideoActivity;
import com.an.trailers.model.Movie;
import com.bumptech.glide.Glide;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context context;
    private List<Movie> movies;
    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        String imageUrl = String.format(Constants.IMAGE_URL, movie.getPosterPath());
        Glide.with(context).load(imageUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Movie movie = movies.get(getLayoutPosition());
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_IMAGE_URL, String.format(Constants.IMAGE_URL, movie.getPosterPath()));
            intent.putExtra(DetailActivity.EXTRA_MAP, movie);
            context.startActivity(intent);
        }
    }
}
