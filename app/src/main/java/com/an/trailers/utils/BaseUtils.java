package com.an.trailers.utils;


import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;

import com.an.trailers.R;
import com.an.trailers.model.APIResponse;
import com.an.trailers.model.Cast;
import com.an.trailers.model.Crew;
import com.an.trailers.model.MovieResponse;
import com.an.trailers.model.Movie;
import com.an.trailers.model.Video;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BaseUtils {

    private static List<Map> getGenres(Context context) {
        String rulesJson = getJSONStringFromRaw(context, R.raw.genres);
        Type listType = new TypeToken<Map>() {}.getType();
        Map categories = new Gson().fromJson(rulesJson, listType);
        List<Map> genres = (List<Map>) categories.get("genres");
        return genres;
    }

    public static List<Movie> getMovies(Context context) {
        String rulesJson = getJSONStringFromRaw(context, R.raw.sample);
        Type listType = new TypeToken<MovieResponse>() {}.getType();
        MovieResponse apiResponseMessage = new Gson().fromJson(rulesJson, listType);
        List<Movie> movies = apiResponseMessage.getResults();
        return movies;
    }

    public static Movie getMovieDetails(Context context) {
        String rulesJson = getJSONStringFromRaw(context, R.raw.sample_detail);
        Type listType = new TypeToken<Movie>() {}.getType();
        Movie movie = new Gson().fromJson(rulesJson, listType);
        return movie;
    }

    public static List<Video> getSampleVideos(Context context) {
        String rulesJson = getJSONStringFromRaw(context, R.raw.video);
        Type listType = new TypeToken<APIResponse>() {}.getType();
        APIResponse apiResponse = new Gson().fromJson(rulesJson, listType);
        List<Video> videos = apiResponse.getResults();
        return videos;
    }

    public static APIResponse getSampleCast(Context context) {
        String rulesJson = getJSONStringFromRaw(context, R.raw.sample_cast);
        Type listType = new TypeToken<APIResponse>() {}.getType();
        APIResponse apiResponse = new Gson().fromJson(rulesJson, listType);
        return apiResponse;
    }

    public static List<String> getGenresByMovie(Context context,
                                                List<Integer> genreIds) {
        List<String> genreNames = new ArrayList<>();
        List<Map> genres = getGenres(context);
        for(Map map : genres) {
            Double id = ((Number)map.get("id")).doubleValue();
            if(genreIds.contains(id)) genreNames.add(String.valueOf(map.get("name")));
        }
        return genreNames;
    }

    public static Pair<String, String> getCasts(Context context) {
        List<String> castNames = new ArrayList<>();
        String director = null;
        APIResponse apiResponse = getSampleCast(context);
        List<Crew> crewList = apiResponse.getCrew();
        List<Cast> castList = apiResponse.getCast();
        for(Cast cast : castList) {
            if(castNames.size() > 4) break;
            castNames.add(cast.getName());
        }
        for(Crew crew : crewList) {
            if(crew.getJob().equalsIgnoreCase("Director")) {
                director = crew.getName();
            }
        }
        return new Pair<>(TextUtils.join(",", castNames), director);
    }

    private static String getJSONStringFromRaw(Context context, int rawId) {

        InputStream content = context.getResources().openRawResource(rawId);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
        String respString = "";
        try {
            String s = "";
            while ((s = buffer.readLine()) != null) {
                respString += s;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return respString;
    }

    public static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static String getFormattedDate(String dateString) {
        Date date = getDate(dateString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int day = cal.get(Calendar.DATE);
        switch (day % 10) {
            case 1:
                return new SimpleDateFormat("MMMM d'st', yyyy").format(date);
            case 2:
                return new SimpleDateFormat("MMMM d'nd', yyyy").format(date);
            case 3:
                return new SimpleDateFormat("MMMM d'rd', yyyy").format(date);
            default:
                return new SimpleDateFormat("MMMM d'th', yyyy").format(date);
        }
    }

    private static Date getDate(String aDate) {
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }

    public static MovieResponse getMovieList(String jsonString) {
        Type listType = new TypeToken<MovieResponse>() {}.getType();
        MovieResponse apiResponseMessage = new Gson().fromJson(jsonString, listType);
        return apiResponseMessage;
    }

    public static Movie getMovieDetails(String jsonString) {
        Type listType = new TypeToken<Movie>() {}.getType();
        Movie movie = new Gson().fromJson(jsonString, listType);
        return movie;
    }

    public static List<Video> getMovieVideos(String jsonString) {
        Type listType = new TypeToken<APIResponse>() {}.getType();
        APIResponse apiResponse = new Gson().fromJson(jsonString, listType);
        List<Video> videos = apiResponse.getResults();
        return videos;
    }

    public static Pair<String, String> getMovieCast(String jsonString) {
        List<String> castNames = new ArrayList<>();
        String director = null;

        Type listType = new TypeToken<APIResponse>() {}.getType();
        APIResponse apiResponse = new Gson().fromJson(jsonString, listType);
        List<Crew> crewList = apiResponse.getCrew();
        List<Cast> castList = apiResponse.getCast();
        for(Cast cast : castList) {
            if(castNames.size() > 4) break;
            castNames.add(cast.getName());
        }
        for(Crew crew : crewList) {
            if(crew.getJob().equalsIgnoreCase("Director")) {
                director = crew.getName();
            }
        }
        return new Pair<>(TextUtils.join(",", castNames), director);
    }
}
