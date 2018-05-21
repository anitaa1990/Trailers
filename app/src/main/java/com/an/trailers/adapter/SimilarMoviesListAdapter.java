package com.an.trailers.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.an.trailers.activity.DetailActivity;
import com.an.trailers.activity.MovieDetailActivity;
import com.an.trailers.databinding.SimilarMoviesListItemBinding;
import com.an.trailers.model.Movie;
import com.an.trailers.utils.NavigationUtils;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class SimilarMoviesListAdapter extends RecyclerView.Adapter<SimilarMoviesListAdapter.CustomViewHolder> {

    private Activity activity;
    private List<Movie> movies;
    public SimilarMoviesListAdapter(Activity activity, List<Movie> movies) {
        this.activity = activity;
        this.movies = movies;
    }

    public SimilarMoviesListAdapter(Activity activity) {
        this.activity = activity;
        this.movies = Collections.emptyList();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        SimilarMoviesListItemBinding itemBinding = SimilarMoviesListItemBinding.inflate(layoutInflater, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(itemBinding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Movie movie = getItem(position);
        String imageUrl = movie.getPosterPath();
        Picasso.get().load(imageUrl).into(holder.binding.itemImg);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public Movie getItem(int position) {
        return movies.get(position);
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimilarMoviesListItemBinding binding;

        public CustomViewHolder(SimilarMoviesListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.itemImg.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Movie movie = movies.get(getLayoutPosition());
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                    new Pair(binding.itemImg, DetailActivity.IMAGE_TRANSITION_NAME));
            NavigationUtils.redirectToDetailScreen(activity, movie, options);
        }
    }
}
