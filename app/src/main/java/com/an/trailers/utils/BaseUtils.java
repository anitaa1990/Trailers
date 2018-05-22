package com.an.trailers.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.util.Pair;
import android.view.Display;
import android.view.WindowManager;

import com.an.trailers.Constants;
import com.an.trailers.R;
import com.an.trailers.fragment.CommonFragment;
import com.an.trailers.model.APIResponse;
import com.an.trailers.model.Cast;
import com.an.trailers.model.Crew;
import com.an.trailers.model.MovieDb;
import com.an.trailers.model.MovieResponse;
import com.an.trailers.model.Movie;
import com.an.trailers.model.Rating;
import com.an.trailers.model.Video;
import com.an.trailers.views.menu.SlideMenuItem;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.an.trailers.Constants.LOCALE_CACHE_PATH;

public class BaseUtils {

    public static String getJSONStringFromRaw(Context context, int rawId) {

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

    public static float dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale;
    }

    public static int getScreenWidth(Context mContext) {
        boolean width = false;
        WindowManager wm = (WindowManager)mContext.getSystemService("window");
        Display display = wm.getDefaultDisplay();
        int width1;
        if(Build.VERSION.SDK_INT > 12) {
            Point size = new Point();
            display.getSize(size);
            width1 = size.x;
        } else {
            width1 = display.getWidth();
        }

        return width1;
    }

    public static int getScreenHeight(Context mContext) {
        boolean height = false;
        WindowManager wm = (WindowManager)mContext.getSystemService("window");
        Display display = wm.getDefaultDisplay();
        int height1;
        if(Build.VERSION.SDK_INT > 12) {
            Point size = new Point();
            display.getSize(size);
            height1 = size.y;
        } else {
            height1 = display.getHeight();
        }

        return height1;
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

    public static Pair<List<Cast>, List<Crew>> getMovieCast(String jsonString) {
        Type listType = new TypeToken<APIResponse>() {}.getType();
        APIResponse apiResponse = new Gson().fromJson(jsonString, listType);
        return new Pair<>(apiResponse.getCast(), apiResponse.getCrew());
    }

    public static Rating getRatings(String jsonString) {
        Type listType = new TypeToken<Rating>() {}.getType();
        Rating rating = new Gson().fromJson(jsonString, listType);
        return rating;
    }

    public static List<String> getGenres(List<Object> genres) {
        List<String> genreNames = new ArrayList<>(genres.size());
        for(Object obj : genres) {
            if(obj instanceof String)
                genreNames.add(Objects.toString(obj, null));
            else genreNames.add(String.valueOf(((Map)obj).get("name")));
        }
        return genreNames;
    }


    public static List<SlideMenuItem> getMenuList(Context context) {
        List<SlideMenuItem> slideMenuItems = new ArrayList<>();
        List<String> menuTitles = Arrays.asList(context.getResources().getStringArray(R.array.menu_names));
        TypedArray menuIcons = context.getResources().obtainTypedArray(R.array.menu_icons);

        for(int i = 0; i< menuTitles.size(); i++) {
            SlideMenuItem slideMenuItem = new SlideMenuItem(menuTitles.get(i), menuIcons.getResourceId(i, -1));
            slideMenuItems.add(slideMenuItem);
        }

        menuIcons.recycle();
        return slideMenuItems;
    }

    public static void shareMovie(Activity activity,
                                  String videoId) {
        String shareText = String.format(Constants.YOUTUBE_VIDEO_PATH, videoId);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.setType("text/*");
        activity.startActivity(Intent.createChooser(shareIntent, "Share via:"));
    }

    public static Map<String, String> getRatingSource(List<Map<String, String>> list,
                                                      String source) {
        for(Map<String, String> map : list) {
            if(source.equalsIgnoreCase(map.get("Source"))) {
                return map;
            }
        }
        return null;
    }

    public static List<Movie> getFavMovies() {
        List<Movie> movies = new ArrayList<>();
        Map<Long, Movie> favMovieMap = MovieDb.getInstance().getFavMovies();
        Iterator<Map.Entry<Long,Movie>> iterator = favMovieMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long,Movie> entry = iterator.next();
            movies.add(entry.getValue());
        }
        return movies;
    }


    public static List<CommonFragment> getFragments(List<Movie> movies) {
        List<CommonFragment> commonFragments = new ArrayList<>();
        for (Movie movie : movies) {
            if(movie.getPosterPath() != null)
                commonFragments.add(CommonFragment.newInstance(movie));
        }
        return commonFragments;
    }



    /* You can use this method to store the
 * request response from your local cache  */
    public static void writeObjectToDisk(final Object object) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ObjectUtil objDataStream = new ObjectUtil();
                objDataStream.writeObjects(object, LOCALE_CACHE_PATH);
            }
        }).start();
    }

    /* You can use this method to retrieve the
     * request response from your local cache  */
    public static Object readObjectFromDisk() {
        ObjectUtil objDataStream = new ObjectUtil();
        return objDataStream.readObjects(LOCALE_CACHE_PATH);
    }

    public static void loadExternalBrowser(Activity activity, String url) {
        try {
            if (!url.startsWith("https://") && !url.startsWith("http://")) {
                url = "http://" + url;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            activity.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
