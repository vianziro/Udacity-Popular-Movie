/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.ec2ainun.udacitypopmovies.utilities;

import android.net.Uri;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    final static String POP_BASE_URL =
            "http://api.themoviedb.org/3/movie/popular";

    final static String TOP_BASE_URL =
            "http://api.themoviedb.org/3/movie/top_rated";

    final static String PARAM_QUERY = "api_key";

    /**
     * Builds the URL used to query GitHub.
     *
     * @param API The keyword that will be needed.
     * @return The URL to use to query the GitHub.
     */
    public static URL buildUrl(String SearchQuery, String API) {

        Uri builtUri;
        if(SearchQuery.equals("popular")){
            builtUri =Uri.parse(POP_BASE_URL).buildUpon()
                    .appendQueryParameter(PARAM_QUERY, API)
                    .build();
        }
        else {
            builtUri =Uri.parse(TOP_BASE_URL).buildUpon()
                    .appendQueryParameter(PARAM_QUERY, API)
                    .build();
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
    public static URL buildUrlId(String SearchQuery, String API, String id) {
        Uri builtUri;

        String BaseURL = "http://api.themoviedb.org/3/movie/";
        if(SearchQuery.equals("videos")){
            String video = BaseURL.concat(id).concat("/videos");
            builtUri =Uri.parse(video).buildUpon()
                    .appendQueryParameter(PARAM_QUERY, API)
                    .build();

        }
        else{
            String reviews = BaseURL.concat(id).concat("/reviews");
            builtUri =Uri.parse(reviews).buildUpon()
                    .appendQueryParameter(PARAM_QUERY, API)
                    .build();
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(5000);
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}