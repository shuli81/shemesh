package com.shuli.apps.shemesh;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

class Search extends AsyncTask<String, Void, Parser> {
    private HttpPost httpPost;
    private MainActivity mMainActivity;

    Search(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
    }

    @Override
    protected void onPreExecute() {
        if (mMainActivity.mLogo.getVisibility() == View.VISIBLE)
            mMainActivity.mLogo.setVisibility(View.GONE);
        mMainActivity.mProgressBar.setVisibility(View.VISIBLE);
        mMainActivity.mResultsLayout.setVisibility(View.GONE);
    }

    @Override
    protected Parser doInBackground(String... name) {
        httpPost = new HttpPost(name[0]);
        if (httpPost.isSuccessful()) {
            return new Parser(httpPost.getHtml());
        } else {
            this.cancel(true);
            return null;
        }
    }

    @Override
    protected void onCancelled() {
        if (!httpPost.isSuccessful()) {
            Toast.makeText(mMainActivity, R.string.no_connection_toast, Toast.LENGTH_SHORT).show();
        }
        mMainActivity.mProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onPostExecute(Parser result) {
        mMainActivity.history.add(mMainActivity.mNameTextField.getText().toString(), mMainActivity.getApplicationContext());
        mMainActivity.mProgressBar.setVisibility(View.GONE);
        mMainActivity.mResultsLayout.setVisibility(View.VISIBLE);
        mMainActivity.mResultsFoundText.setText(result.getResultsFound() + mMainActivity.getString(R.string.results_found) +
                (result.getResultsFound() > 40 ? mMainActivity.getString(R.string.only_showing_40) : "."));
        mMainActivity.results = new ArrayList<>(Arrays.asList(result.getPeople()));
        populateListView();
    }

    private void populateListView() {
        ArrayAdapter<Person> adapter = new ResultsListAdapter(mMainActivity);
        ListView list = (ListView) mMainActivity.findViewById(R.id.results_list_view);
        list.setAdapter(adapter);
    }
}