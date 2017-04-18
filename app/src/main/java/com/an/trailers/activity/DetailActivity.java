package com.an.trailers.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.an.trailers.Constants;
import com.an.trailers.R;
import com.an.trailers.adapter.CreditAdapter;
import com.an.trailers.adapter.VideoAdapter;
import com.an.trailers.callback.RESTListener;
import com.an.trailers.model.Cast;
import com.an.trailers.model.Crew;
import com.an.trailers.model.Genre;
import com.an.trailers.model.Movie;
import com.an.trailers.model.Rating;
import com.an.trailers.model.Video;
import com.an.trailers.service.RESTExecutorService;
import com.an.trailers.service.VolleyTask;
import com.an.trailers.utils.BaseUtils;
import com.an.trailers.views.CollectionPicker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class DetailActivity extends FragmentActivity implements RESTListener, Constants {

    public static final String EXTRA_IMAGE_URL = "detailImageUrl";
    public static final String EXTRA_MAP = "map";

    public static final String IMAGE_TRANSITION_NAME = "transitionImage";
    public static final String TITLE_TRANSITION_NAME = "title";
    public static final String DESC_TRANSITION_NAME = "desc";
    public static final String RATINGBAR_TRANSITION_NAME = "ratingBar";


    private TextView movieTitle, movieDesc;
    private ImageView imageView;
    private RatingBar ratingBar;

    private CollectionPicker picker;
    private CollectionPicker movieStatusTxt;

    private LinearLayout listContainer;
    private RecyclerView recyclerView;
    private RecyclerView castView, crewView;
    private TextView runtimeTxt;
//    private TextView imdbTxt;
    private TextView imdbRatingTxt;

    private View imdbLayout;

    private Movie movie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        imageView = (ImageView) findViewById(R.id.image);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.smoothScrollToPosition(1);

        movieTitle = (TextView) findViewById(R.id.movie_title);
        movieDesc = (TextView) findViewById(R.id.movie_desc);
//        ratingBar = (RatingBar) findViewById(R.id.rating);
        listContainer = (LinearLayout) findViewById(R.id.detail_list_container);
        picker = (CollectionPicker) findViewById(R.id.collection_item_picker);
        movieStatusTxt = (CollectionPicker) findViewById(R.id.movie_status);
        runtimeTxt = (TextView) findViewById(R.id.txt_runtime);
        imdbLayout = findViewById(R.id.layout_imdb);
        imdbRatingTxt = (TextView) findViewById(R.id.imdbRating);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        movie = (Movie) getIntent().getSerializableExtra(EXTRA_MAP);
        movieTitle.setText(movie.getTitle());
        movieDesc.setText(movie.getOverview());
//        if(movie.getVoteAverage() != null) {
//            Float value = (movie.getVoteAverage() / 10) * 5;
//            ratingBar.setRating(value);
//        }

        String imageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        Picasso.with(this).load(imageUrl).into(imageView);

        ViewCompat.setTransitionName(imageView, IMAGE_TRANSITION_NAME);
        ViewCompat.setTransitionName(movieTitle, TITLE_TRANSITION_NAME);
        ViewCompat.setTransitionName(movieDesc, DESC_TRANSITION_NAME);
//        ViewCompat.setTransitionName(ratingBar, RATINGBAR_TRANSITION_NAME);


        RESTExecutorService.submit(new VolleyTask(this, METHOD_MOVIE, String.valueOf(movie.getId()), this));
        RESTExecutorService.submit(new VolleyTask(this, METHOD_VIDEO, String.valueOf(movie.getId()), this));
//        RESTExecutorService.submit(new VolleyTask(this, METHOD_CAST, String.valueOf(movie.getId()), this));
//        dealListView();
    }

    private void dealListView() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View childView = layoutInflater.inflate(R.layout.detail_list_item, null);

        TextView releaseTxt = (TextView) childView.findViewById(R.id.txt_release);
        releaseTxt.setText(BaseUtils.getFormattedDate(movie.getReleaseDate()));

//        imdbTxt = (TextView) childView.findViewById(R.id.txt_imdb);

//        recyclerView = (RecyclerView) childView.findViewById(R.id.list);
//        recyclerView.setNestedScrollingEnabled(false);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.smoothScrollToPosition(1);

        castView = (RecyclerView) childView.findViewById(R.id.cast_list);
        castView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        castView.setLayoutManager(linearLayoutManager1);
        castView.smoothScrollToPosition(1);

        crewView = (RecyclerView) childView.findViewById(R.id.crew_list);
        crewView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        crewView.setLayoutManager(linearLayoutManager2);
        crewView.smoothScrollToPosition(1);

        listContainer.addView(childView);
    }

    @Override
    public void onVideoResponse(List<Video> videos) {
        VideoAdapter adapter = new VideoAdapter(DetailActivity.this, videos);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onMovieDetailResponse(Movie movie) {
        this.movie = movie;
        RESTExecutorService.submit(new VolleyTask(DetailActivity.this, METHOD_RATING, movie.getImdbId(), DetailActivity.this));

        String status = movie.getStatus();
        movieStatusTxt.setItems(Arrays.asList(new String[]{ status }));

        String runTxt = status.equalsIgnoreCase(MOVIE_STATUS_RELEASED) ? String.format("%s mins", String.valueOf(movie.getRuntime())) :
                BaseUtils.getFormattedDate(movie.getReleaseDate());
        runtimeTxt.setText(runTxt);

        List<Genre> genres = movie.getGenres();
        List<String> genreNames = new ArrayList<>();
        for(Genre genre : genres) {
            genreNames.add(genre.getName());
        }
        picker.setUseRandomColor(true);
        picker.setItems(genreNames);
    }

    @Override
    public void onCreditsResponse(Pair<List<Cast>, List<Crew>> creditPair) {
        CreditAdapter creditAdapter = new CreditAdapter(this, CREDIT_CAST, creditPair.first, null);
        castView.setAdapter(creditAdapter);
        CreditAdapter crewAdapter = new CreditAdapter(this, CREDIT_CREW, null, creditPair.second);
        crewView.setAdapter(crewAdapter);
    }

    @Override
    public void onRatingsResponse(Rating rating) {
        if(rating != null && MOVIE_STATUS_RELEASED.equalsIgnoreCase(movie.getStatus())) {
            imdbLayout.setVisibility(View.VISIBLE);
            imdbRatingTxt.setText(rating.getImdbRating());
        } else imdbLayout.setVisibility(View.GONE);
    }
}