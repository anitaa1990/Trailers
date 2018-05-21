package com.an.trailers.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.an.trailers.Constants;
import com.an.trailers.R;
import com.an.trailers.adapter.CreditListAdapter;
import com.an.trailers.adapter.SimilarMoviesListAdapter;
import com.an.trailers.adapter.VideoListAdapter;
import com.an.trailers.databinding.DetailActivityBinding;
import com.an.trailers.model.APIResponse;
import com.an.trailers.model.Movie;
import com.an.trailers.model.MovieResponse;
import com.an.trailers.model.Rating;
import com.an.trailers.viewmodel.MovieDetailViewModel;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import static com.an.trailers.Constants.CREDIT_CAST;
import static com.an.trailers.Constants.CREDIT_CREW;
import static com.an.trailers.Constants.MOVIE_STATUS_RELEASED;
import static com.an.trailers.activity.DetailActivity.DESC_TRANSITION_NAME;
import static com.an.trailers.activity.DetailActivity.IMAGE_TRANSITION_NAME;
import static com.an.trailers.activity.DetailActivity.TITLE_TRANSITION_NAME;


public class MovieDetailActivity extends AppCompatActivity implements Observer, View.OnClickListener {

    private MovieDetailViewModel movieDetailViewModel;
    private DetailActivityBinding detailActivityBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeDataBinding();
        initializeView();
        setUpVideos(detailActivityBinding.recyclerView);
        setUpCastInfo(detailActivityBinding.includedLayout.castList);
        setUpCrewInfo(detailActivityBinding.includedLayout.crewList);
        setUpSimilarMoviesList(detailActivityBinding.includedSimilarLayout.moviesList);
        setUpObserver(movieDetailViewModel);
    }


    private void initializeView() {
        ViewCompat.setTransitionName(detailActivityBinding.image, IMAGE_TRANSITION_NAME);
        ViewCompat.setTransitionName(detailActivityBinding.movieTitle, TITLE_TRANSITION_NAME);
        ViewCompat.setTransitionName(detailActivityBinding.movieDesc, DESC_TRANSITION_NAME);
        detailActivityBinding.expandButton.setPaintFlags(detailActivityBinding.expandButton.getPaintFlags() |  Paint.UNDERLINE_TEXT_FLAG);
    }


    private void initializeDataBinding() {
        Movie movie = (Movie) getIntent().getSerializableExtra(Constants.EXTRA_MAP);
        movieDetailViewModel = new MovieDetailViewModel(this, movie);

        detailActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        detailActivityBinding.setMovie(movie);
        detailActivityBinding.shineButton.setChecked(movieDetailViewModel.isFavourite(movie.getId()));
        handleFavouriteAction();
        detailActivityBinding.shineButton.setOnClickListener(this);
    }


    private void setUpVideos(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.smoothScrollToPosition(1);

        VideoListAdapter videoListAdapter = new VideoListAdapter(getApplicationContext());
        recyclerView.setAdapter(videoListAdapter);
    }


    private void setUpSimilarMoviesList(RecyclerView recyclerView) {
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager similarMoviesLayout = new LinearLayoutManager(this);
        similarMoviesLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(similarMoviesLayout);
        recyclerView.smoothScrollToPosition(1);

        SimilarMoviesListAdapter similarMoviesListAdapter = new SimilarMoviesListAdapter(this);
        recyclerView.setAdapter(similarMoviesListAdapter);
    }


    private void setUpCastInfo(RecyclerView recyclerView) {
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.smoothScrollToPosition(1);

        CreditListAdapter creditListAdapter = new CreditListAdapter(getApplicationContext(), CREDIT_CAST);
        recyclerView.setAdapter(creditListAdapter);
    }


    private void setUpCrewInfo(RecyclerView recyclerView) {
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.smoothScrollToPosition(1);

        CreditListAdapter creditListAdapter = new CreditListAdapter(getApplicationContext(), CREDIT_CREW);
        recyclerView.setAdapter(creditListAdapter);
    }


    private void setUpObserver(Observable observable) {
        observable.addObserver(this);
    }


    @Override
    public void update(Observable observable, Object object) {
        if(object instanceof Movie) {
            Movie movie = (Movie) object;
            detailActivityBinding.setMovie(movie);
            Picasso.get().load(movie.getPosterPath()).into(detailActivityBinding.image);

            detailActivityBinding.movieStatus.setItems(Arrays.asList(new String[]{ movie.getStatus() }));
            detailActivityBinding.collectionItemPicker.setUseRandomColor(true);
            detailActivityBinding.collectionItemPicker.setItems(movie.getGenreNames());
            detailActivityBinding.txtRuntime.setText(movie.getRuntimeInMins());

        } else if(object instanceof MovieResponse) {
            MovieResponse movieResponse = (MovieResponse) object;
            SimilarMoviesListAdapter similarMoviesListAdapter = (SimilarMoviesListAdapter) detailActivityBinding.includedSimilarLayout.moviesList.getAdapter();
            similarMoviesListAdapter.setMovies(movieResponse.getResults());
            int visibility = movieResponse.getResults() != null & !movieResponse.getResults().isEmpty() ? View.VISIBLE : View.GONE;
            detailActivityBinding.includedSimilarLayout.movieSimilarTitle.setVisibility(visibility);

        } else if(object instanceof APIResponse) {
            APIResponse apiResponse = (APIResponse) object;

            if(apiResponse.getResults() != null) {
                VideoListAdapter videoListAdapter = (VideoListAdapter) detailActivityBinding.recyclerView.getAdapter();
                videoListAdapter.setVideos(apiResponse.getResults());

            }
            if(apiResponse.getCast() != null) {
                CreditListAdapter creditListAdapter = (CreditListAdapter) detailActivityBinding.includedLayout.castList.getAdapter();
                creditListAdapter.setCasts(apiResponse.getCast());

            }
            if(apiResponse.getCrew() != null) {
                CreditListAdapter creditListAdapter = (CreditListAdapter) detailActivityBinding.includedLayout.crewList.getAdapter();
                creditListAdapter.setCrews(apiResponse.getCrew());
            }

        } else if(object instanceof Rating) {
            Rating rating = (Rating) object;
            int displayRating = (detailActivityBinding.getMovie().getStatus() == null ||
                    (rating != null && MOVIE_STATUS_RELEASED.equalsIgnoreCase(detailActivityBinding.getMovie().getStatus())))
                    ? View.VISIBLE : View.GONE;

            detailActivityBinding.layoutImdb.setVisibility(displayRating);
            String ratingValue = rating != null ? rating.getImdbRating() : "";
            detailActivityBinding.imdbRating.setText(ratingValue);
        }
    }


    public void handleExpandAction(View view) {
        if (detailActivityBinding.includedLayout.expandableLayout.isExpanded()) {
            detailActivityBinding.expandButton.setText(getString(R.string.read_more));
            detailActivityBinding.includedLayout.expandableLayout.collapse();
        } else {
            detailActivityBinding.expandButton.setText(getString(R.string.read_less));
            detailActivityBinding.includedLayout.expandableLayout.expand();
        }
    }


    public void handleFavouriteAction() {
        if(detailActivityBinding.shineButton.isChecked()) {
            detailActivityBinding.favView.setBackgroundColor(Color.TRANSPARENT);
        } else {
            detailActivityBinding.favView.setBackgroundResource(R.drawable.ic_fav);
        }
        movieDetailViewModel.updateDb(detailActivityBinding.shineButton.isChecked());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        movieDetailViewModel.reset();
    }

    @Override
    public void onClick(View view) {
        handleFavouriteAction();
    }
}
