package com.shuli.apps.shemesh;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    InputMethodManager imm;
    AutoCompleteTextView mNameTextField;
    LinearLayout mResultsLayout;
    TextView mResultsFoundText;
    List<Person> results;
    ProgressBar mProgressBar;
    ArrayAdapter<String> adapter;
    History history;
    LinearLayout mLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        history = new History(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, history.getHistory());
        mNameTextField = (AutoCompleteTextView) findViewById(R.id.name_text_field);
        mNameTextField.setAdapter(adapter);
        mNameTextField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                doSearch(mNameTextField);
            }
        });
        mNameTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch(mNameTextField);
                    return true;
                }
                return false;
            }
        });
        mLogo = (LinearLayout) findViewById(R.id.shemesh_logo);
        mResultsFoundText = (TextView) findViewById(R.id.results_found_text);
        mResultsLayout = (LinearLayout) findViewById(R.id.results_layout);
        mProgressBar = (ProgressBar) this.findViewById(R.id.progressBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            /*case R.id.share_app_button:
                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
                return true;*/
            case R.id.clear_history:
                history.clearHistory(getApplicationContext());
                return true;
            /*case R.id.about:
                Toast.makeText(this, "about", Toast.LENGTH_SHORT).show();
                return true;*/
            default:
                return false;
        }
    }

    public void doSearch(View view) {
        imm.hideSoftInputFromWindow(mNameTextField.getWindowToken(), 0);
        new Search(this).execute(mNameTextField.getText().toString());
    }
}
