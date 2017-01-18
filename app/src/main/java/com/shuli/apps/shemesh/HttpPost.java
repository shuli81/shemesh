package com.shuli.apps.shemesh;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;

class HttpPost {

    private static final String TAG = "HttpPost";
    private boolean successful = false;
    private Document html;

    HttpPost(String name) {
        HashMap<String, String> header = new HashMap<>();
        header.put("Content-type", "application/x-www-form-urlencoded");
        header.put("Referer", "http://www.shemesh.co.il/cgi-bin/index.cgi");
        header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        try {
            html = Jsoup.connect("http://www.shemesh.co.il/cgi-bin/resdb.cgi")
                    .data(header)
                    .data("words", name)
                    .userAgent("Chrome/54.0.2840.99")
                    .post();
            successful = true;
            Log.v(TAG, "Connection successful");
        } catch (IOException e) {
            Log.v(TAG, "Connection unsuccessful");
            html = null;
        }
    }

    public boolean isSuccessful() {
        return successful;
    }

    public Document getHtml() {
        return html;
    }
}
