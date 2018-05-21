package com.an.trailers.activity;

import android.app.SearchManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.an.trailers.Constants;
import com.an.trailers.R;
import com.an.trailers.adapter.CommonPagerAdapter;
import com.an.trailers.callback.OnPageSelectedListener;
import com.an.trailers.databinding.SearchActivityBinding;
import com.an.trailers.fragment.CommonFragment;
import com.an.trailers.viewmodel.SearchMovieViewModel;
import com.an.trailers.views.CustomerPagerTransformer;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, Observer, OnPageSelectedListener, Constants {

    private SearchMovieViewModel searchMovieViewModel;
    private SearchActivityBinding searchActivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeDataBinding();
        setUpViewPager();
        setUpSearch();
        setupViewModel();
    }


    private void initializeDataBinding() {
        searchActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
    }


    private void setUpViewPager() {
        searchActivityBinding.includedSimilarLayout.viewpager.setPageTransformer(false, new CustomerPagerTransformer(getApplicationContext()));
    }


    private void setUpSearch() {
        searchActivityBinding.searchIcon.setVisibility(View.GONE);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchActivityBinding.search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchActivityBinding.search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchActivityBinding.search.setIconifiedByDefault(false);
        searchActivityBinding.search.setOnQueryTextListener(this);

        EditText searchEditText = searchActivityBinding.search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(android.R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(android.R.color.white));
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(),"fonts/gt_medium.otf");
        searchEditText.setTypeface(myCustomFont);
    }


    private void setupViewModel() {
        searchMovieViewModel = new SearchMovieViewModel(getApplicationContext());
        searchMovieViewModel.addObserver(this);
    }


    private void displayLoader() {
        searchActivityBinding.includedSimilarLayout.progressView.setVisibility(View.VISIBLE);
    }


    private void hideLoader() {
        searchActivityBinding.includedSimilarLayout.progressView.setVisibility(View.GONE);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {

        displayLoader();
        CommonPagerAdapter commonPagerAdapter = (CommonPagerAdapter) searchActivityBinding.includedSimilarLayout.viewpager.getAdapter();
        if(commonPagerAdapter != null) {
            commonPagerAdapter.removeFragments();
            searchActivityBinding.includedSimilarLayout.viewpager.clearBackground();
        }
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        searchMovieViewModel.searchMovie(query);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    @Override
    public void onPageSelected(int position) {
        searchMovieViewModel.updateData(searchActivityBinding.includedSimilarLayout.viewpager.getCurrentItem(),
                searchActivityBinding.includedSimilarLayout.viewpager.getAdapter().getCount());
    }


    @Override
    public void update(Observable observable, Object o) {
        hideLoader();
        if(o instanceof List<?>) {
            List<CommonFragment> fragments = (List<CommonFragment>) o;
            CommonPagerAdapter commonPagerAdapter = (CommonPagerAdapter) searchActivityBinding.includedSimilarLayout.viewpager.getAdapter();
            if(fragments.size() == 0 && (commonPagerAdapter == null || commonPagerAdapter.getCount() == 0)) {
                searchActivityBinding.includedSimilarLayout.emptyContainer.setVisibility(View.VISIBLE);
                searchActivityBinding.includedSimilarLayout.favDisplayText.setText(getString(R.string.empty_search));
                searchActivityBinding.includedSimilarLayout.viewpager.setVisibility(View.GONE);

            } else {
                searchActivityBinding.includedSimilarLayout.emptyContainer.setVisibility(View.GONE);
                searchActivityBinding.includedSimilarLayout.viewpager.setVisibility(View.VISIBLE);

                if(commonPagerAdapter == null || !searchMovieViewModel.isPaginating()) {
                    commonPagerAdapter = new CommonPagerAdapter(getSupportFragmentManager(), fragments);
                    searchActivityBinding.includedSimilarLayout.viewpager.setAdapter(commonPagerAdapter);
                    searchActivityBinding.includedSimilarLayout.viewpager.addOnPageSelectedListener(this);
                    searchActivityBinding.includedSimilarLayout.viewpager.addOnBackgroundSwitchView(searchActivityBinding.includedSimilarLayout.overlay);

                } else {
                    commonPagerAdapter.setFragments(fragments);
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchMovieViewModel.reset();
    }

}
