package com.shuli.apps.shemesh;

import android.content.Context;
import android.text.util.Linkify;
import android.widget.TextView;

import java.util.regex.Pattern;

class PhonNumTextView extends TextView {
    public PhonNumTextView(Context context, String number) {
        super(context);
        this.setText(number);
        this.setTextAppearance(context, R.style.PhoneNumText);
        Pattern pattern = Pattern.compile(".*");
        Linkify.addLinks(this, pattern, "tel:");
        this.setLinksClickable(true);
    }
}
