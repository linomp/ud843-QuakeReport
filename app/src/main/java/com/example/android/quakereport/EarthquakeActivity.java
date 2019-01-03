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
package com.example.android.quakereport;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private ArrayAdapter<Earthquake> mAdapter;
    private ListView mEarthquakeListView;
    private TextView mEmptyStateTextView;
    private View mLoadingIndicator;
    public static final String REQUEST_URL =

            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2019-01-01&endtime=2019-03-01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        mEarthquakeListView = (ListView) findViewById(R.id.list);

        // Set the empty view
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        mEarthquakeListView.setEmptyView(mEmptyStateTextView);

        // Spinner
        mLoadingIndicator = findViewById(R.id.loading_spinner);

        ConnectivityManager cm =
                (ConnectivityManager) EarthquakeActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(!isConnected) {
            mLoadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_connection);
        }else {
            //new EarthquakesAsyncTask().execute( REQUEST_URL );
            Bundle bundle = new Bundle();
            bundle.putString("url", REQUEST_URL);
            getLoaderManager().initLoader(0, bundle, this);
        }
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        return new EarthquakeLoader(this, bundle.getString("url"));
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        mLoadingIndicator.setVisibility(View.GONE);
        if (earthquakes == null || earthquakes.size() == 0) {
            mEmptyStateTextView.setText(R.string.no_earthquakes);
            return;
        }
        updateUI(earthquakes);
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        updateUI(new ArrayList<Earthquake>());
    }

    /*private class EarthquakesAsyncTask extends AsyncTask<String, Void, List<Earthquake>> {
        @Override
        protected List<Earthquake> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Earthquake> earthquakes = QueryUtils.getEarthquakes(urls[0]);
            return earthquakes;
        }

        @Override
        protected void onPostExecute (List<Earthquake> earthquakes){
            if (earthquakes == null) {
                return;
            }
            updateUI(earthquakes);
        }
    }*/

    private void updateUI(List<Earthquake> earthquakes){
        if(mAdapter == null) {
            // Create a new {@link ArrayAdapter} of earthquakes
            mAdapter = new EarthquakeAdapter(this, earthquakes);
            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            mEarthquakeListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(earthquakes);
        }
        mEarthquakeListView.setOnItemClickListener((parent, view, position, id) -> {
            String detailsUrl = (String) view.getTag();
            startBrowserIntent(detailsUrl);
        });
    }

    public void startBrowserIntent(String detailsUrl){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(detailsUrl));
        startActivity(browserIntent);
    }

}



