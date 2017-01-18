package com.shuli.apps.shemesh;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

//manages search history. creates history file, adds and retrives items from history and clears history.
//TODO: implement an expiry mechanism for search history.

class History {
    private static final String TAG = "History";
    private final static String HISTORY_FILE = "history.shemesh";
    private MainActivity mMainActivity;
    private File historyFile;
    private Set<String> history = new HashSet<>();

    History(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
        historyFile = new File(mMainActivity.getFilesDir(), HISTORY_FILE);
        try {
            historyFile.createNewFile();
        } catch (IOException e) {
            Log.e(TAG, "File could not be created");
        }
        retrieve();
    }

    void add(String name, Context context) {//adds new searches to the history file
        if (!history.contains(name)) {
            history.add(name);
            save();
            loadHistory(context);
        }
    }

    void clearHistory(final Context context) {
        new AlertDialog.Builder(mMainActivity)
                .setIcon(R.drawable.ic_warning)
                .setTitle(R.string.clear_history_dialog_title)
                .setMessage(R.string.clear_history_dialog_message)
                .setPositiveButton(R.string.dialog_yes_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        history.clear();
                        save();
                        loadHistory(context);
                        Toast.makeText(context, R.string.history_cleared_toast, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.dialog_no_button, null)
                .show();
    }

    private void loadHistory(Context context) {//loads history to the adapter
        this.mMainActivity.adapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, getHistory());
        this.mMainActivity.mNameTextField.setAdapter(this.mMainActivity.adapter);
    }

    private void save() {//serializes the the history Set
        try {
            FileOutputStream fos = new FileOutputStream(historyFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(history);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void retrieve() {//deserializes history and loads to the history Set
        try {
            FileInputStream fis = new FileInputStream(historyFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            history = (Set<String>) ois.readObject();
        } catch (Exception e) {
            Log.i(TAG, "History file is empty");
        }
    }

    String[] getHistory() {
        return history.toArray(new String[history.size()]);//returned as String[] because its required by the adapter
    }
}
