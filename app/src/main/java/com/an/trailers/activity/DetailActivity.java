package com.an.trailers.activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.an.trailers.Constants;
import com.an.trailers.R;
import com.an.trailers.adapter.CreditAdapter;
import com.an.trailers.adapter.MovieAdapter;
import com.an.trailers.adapter.VideoAdapter;
import com.an.trailers.callback.MovieResponseListener;
import com.an.trailers.callback.RESTListener;
import com.an.trailers.model.Cast;
import com.an.trailers.model.Crew;
import com.an.trailers.model.Movie;
import com.an.trailers.model.MovieDb;
import com.an.trailers.model.Rating;
import com.an.trailers.model.Video;
import com.an.trailers.service.RESTExecutorService;
import com.an.trailers.service.VolleyTask;
import com.an.trailers.utils.BaseUtils;
import com.an.trailers.views.collectionpicker.CollectionPicker;
import com.an.trailers.views.expandablelayout.ExpandableLayout;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;
import java.util.Arrays;
import java.util.List;


public class DetailActivity extends FragmentActivity implements RESTListener, View.OnClickListener, Constants {

    public static final String EXTRA_IMAGE_URL = "detailImageUrl";
    public static final String EXTRA_MAP = "map";

    public static final String IMAGE_TRANSITION_NAME = "transitionImage";
    public static final String TITLE_TRANSITION_NAME = "title";
    public static final String DESC_TRANSITION_NAME = "desc";
    public static final String RATINGBAR_TRANSITION_NAME = "ratingBar";


    private TextView movieTitle, movieDesc;
    private ImageView imageView;

    private CollectionPicker picker;
    private CollectionPicker movieStatusTxt;

    private LinearLayout listContainer;
    private RecyclerView recyclerView;

    private RecyclerView similarMoviesView;
    private TextView similarMoviesTitle;

    private RecyclerView castView, crewView;
    private TextView runtimeTxt;
    private TextView imdbRatingTxt;

    private View imdbLayout;
    private Movie movie;

    private View favView;
    private ShineButton shineButton;

    private TextView moreBtn;
    private ExpandableLayout expandableLayout;

    private TextView downloadBtn;
    private boolean isFavourite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//        imageView = (ImageView) findViewById(R.id.image);
//        recyclerView = (RecyclerView) findViewById(R.id.list);
//        recyclerView.setNestedScrollingEnabled(false);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.smoothScrollToPosition(1);
//
//        movieTitle = (TextView) findViewById(R.id.movie_title);
//        movieDesc = (TextView) findViewById(R.id.movie_desc);
//        listContainer = (LinearLayout) findViewById(R.id.detail_list_container);
//        picker = (CollectionPicker) findViewById(R.id.collection_item_picker);
//        movieStatusTxt = (CollectionPicker) findViewById(R.id.movie_status);
//        runtimeTxt = (TextView) findViewById(R.id.txt_runtime);
//        imdbLayout = findViewById(R.id.layout_imdb);
//        imdbRatingTxt = (TextView) findViewById(R.id.imdbRating);
//        favView = findViewById(R.id.fav_view);
//        shineButton = (ShineButton) findViewById(R.id.po_image1);
//        shineButton.setOnClickListener(this);
//        moreBtn = (TextView) findViewById(R.id.expand_button);
//        moreBtn.setPaintFlags(moreBtn.getPaintFlags() |  Paint.UNDERLINE_TEXT_FLAG);
//        moreBtn.setOnClickListener(this);
//        expandableLayout = (ExpandableLayout) findViewById(R.id.expandable_layout_0);
//        downloadBtn = (TextView) findViewById(R.id.download_btn);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getWindow();
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
//
//        movie = (Movie) getIntent().getSerializableExtra(EXTRA_MAP);
//        movieTitle.setText(movie.getTitle());
//        movieDesc.setText(movie.getDescription());
//
//        String imageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
//        Picasso.with(this).load(imageUrl).into(imageView);
//
//        ViewCompat.setTransitionName(imageView, IMAGE_TRANSITION_NAME);
//        ViewCompat.setTransitionName(movieTitle, TITLE_TRANSITION_NAME);
//        ViewCompat.setTransitionName(movieDesc, DESC_TRANSITION_NAME);
//
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                List<Video> videos = movie.getTrailers();
//
//                if(!videos.isEmpty()) {
//                    moreBtn.setVisibility(View.GONE);
//                    if(movie.getUrl() != null) {
//                        downloadBtn.setVisibility(View.VISIBLE);
//                        String s = getString(R.string.download_first_txt).concat(getString(R.string.download_second_txt)).concat(getString(R.string.download_third_txt));
//                        SpannableString ss = new SpannableString(s);
//                        ss.setSpan(clickableSpan, getString(R.string.download_first_txt).length(),
//                                getString(R.string.download_first_txt).concat(getString(R.string.download_second_txt))
//                                        .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        downloadBtn.setText(ss);
//                        downloadBtn.setMovementMethod(LinkMovementMethod.getInstance());
//                    }
//                    onVideoResponse(videos);
//                    RESTExecutorService.submit(new VolleyTask(DetailActivity.this, METHOD_RATING, movie.getImdbId(), DetailActivity.this));
//                }
//                else {
//                    loadSimilarMovies();
//                    RESTExecutorService.submit(new VolleyTask(DetailActivity.this, METHOD_MOVIE, String.valueOf(movie.getId()), DetailActivity.this));
//                    RESTExecutorService.submit(new VolleyTask(DetailActivity.this, METHOD_VIDEO, String.valueOf(movie.getId()), DetailActivity.this));
//                    RESTExecutorService.submit(new VolleyTask(DetailActivity.this, METHOD_CAST, String.valueOf(movie.getId()), DetailActivity.this));
//                    RESTExecutorService.submit(new VolleyTask(DetailActivity.this, METHOD_MOVIE_SIMILAR, String.valueOf(movie.getId()), movieResponseListener));
//                }
//
//                dealListView();
//                isFavourite = MovieDb.getInstance().isFavourite(movie.getId());
//                shineButton.setChecked(isFavourite);
//                handleFavUI();
//            }
//        });
    }

    private void loadSimilarMovies() {
        LayoutInflater layoutInflater = LayoutInflater.from(DetailActivity.this);
        View childView = layoutInflater.inflate(R.layout.similiar_movies_list, null);

        similarMoviesView = (RecyclerView) childView.findViewById(R.id.movies_list);
        similarMoviesView.setNestedScrollingEnabled(false);
        LinearLayoutManager similarMoviesLayout = new LinearLayoutManager(DetailActivity.this);
        similarMoviesLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        similarMoviesView.setLayoutManager(similarMoviesLayout);
        similarMoviesView.smoothScrollToPosition(1);

        similarMoviesTitle = (TextView) childView.findViewById(R.id.movie_similar_title);

        listContainer.addView(childView);
    }

    private void dealListView() {
        castView = (RecyclerView) findViewById(R.id.cast_list);
        castView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        castView.setLayoutManager(linearLayoutManager1);
        castView.smoothScrollToPosition(1);

        crewView = (RecyclerView) findViewById(R.id.crew_list);
        crewView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        crewView.setLayoutManager(linearLayoutManager2);
        crewView.smoothScrollToPosition(1);
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

        picker.setUseRandomColor(true);
        picker.setItems(movie.getGenreNames());
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
        if(movie.getStatus() == null || (rating != null && MOVIE_STATUS_RELEASED.equalsIgnoreCase(movie.getStatus()))) {
            imdbLayout.setVisibility(View.VISIBLE);
            imdbRatingTxt.setText(rating.getImdbRating());
        } else imdbLayout.setVisibility(View.GONE);
    }

    private MovieResponseListener movieResponseListener = new MovieResponseListener() {
        @Override
        public void onMoviesResponse(List<Movie> movies, int currentPage, long totalPages) {
            MovieAdapter movieAdapter = new MovieAdapter(DetailActivity.this, movies);
            similarMoviesView.setAdapter(movieAdapter);
            if(movies == null || movies.isEmpty()) {
                similarMoviesTitle.setVisibility(View.GONE);
            } else similarMoviesTitle.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onClick(View view) {
        if(view == shineButton) {
            handleFavUI();

        } else if(view == moreBtn) {
            if (expandableLayout.isExpanded()) {
                moreBtn.setText(getString(R.string.read_more));
                expandableLayout.collapse();
            } else {
                moreBtn.setText(getString(R.string.read_less));
                expandableLayout.expand();
            }
        }
    }

    private void handleFavUI() {
        if(shineButton.isChecked()) {
            favView.setBackgroundColor(Color.TRANSPARENT);
            MovieDb.getInstance().addToFavMovies(movie);
        }
        else {
            favView.setBackgroundResource(R.drawable.ic_fav);
            MovieDb.getInstance().removeFromFavMovies(movie);
        }
    }

    private ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View textView) {
            BaseUtils.loadExternalBrowser(DetailActivity.this, movie.getUrl());
        }
    };
}