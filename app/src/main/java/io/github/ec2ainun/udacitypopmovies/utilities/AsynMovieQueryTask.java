package io.github.ec2ainun.udacitypopmovies.utilities;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by ec2ainun on 7/13/2017.
 */

public class AsynMovieQueryTask extends AsyncTask<URL, Void, String> {
    private Context context;
    private AsyncTaskCompleteListener<String> listener;

    public AsynMovieQueryTask(Context context, AsyncTaskCompleteListener<String> listener){
        this.context = context;
        this.listener= listener;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(URL... params) {
        URL searchUrl = params[0];
        String SearchResults = null;
        try {
            SearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SearchResults;
    }

    @Override
    protected void onPostExecute(String SearchResults) {
        if (SearchResults != null && !SearchResults.equals("")) {
            super.onPostExecute(SearchResults);
            try {
                this.listener.onTaskComplete(SearchResults);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //showJsonDataView(SearchResults);
        } else {

        }
    }
}
