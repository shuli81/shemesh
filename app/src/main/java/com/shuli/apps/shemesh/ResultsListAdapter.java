package com.shuli.apps.shemesh;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


class ResultsListAdapter extends ArrayAdapter<Person> {
    private MainActivity mMainActivity;

    ResultsListAdapter(MainActivity mainActivity) {
        super(mainActivity, R.layout.item_view, mainActivity.results);
        this.mMainActivity = mainActivity;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = mMainActivity.getLayoutInflater().inflate(R.layout.item_view, parent, false);
        }
        final Person currentPerson = mMainActivity.results.get(position);
        TextView mPersonName = (TextView) itemView.findViewById(R.id.person_name);
        mPersonName.setText(currentPerson.getName());
        LinearLayout mPersonAddress = (LinearLayout) itemView.findViewById(R.id.person_address);
        if (!currentPerson.getAddress1().equals("")) {
            mPersonAddress.setVisibility(View.VISIBLE);
            TextView mPersonAddress1 = (TextView) mPersonAddress.findViewById(R.id.person_address1);
            TextView mPersonAddress2 = (TextView) mPersonAddress.findViewById(R.id.person_address2);
            mPersonAddress1.setText(currentPerson.getAddress1());
            mPersonAddress2.setText(currentPerson.getAddress2());
        } else {
            mPersonAddress.setVisibility(View.GONE);
        }
        LinearLayout mPersonPhoneNums = (LinearLayout) itemView.findViewById(R.id.person_phone_nums);
        if (mPersonPhoneNums.getChildCount() > 0)
            mPersonPhoneNums.removeAllViews();
        int numOfPhoneNums = currentPerson.getPhoneNums().length;
        for (int i = 0; i < numOfPhoneNums; i++) {
            PhonNumTextView phoneNumTextView = new PhonNumTextView(mMainActivity, currentPerson.getPhoneNums()[i]);
            mPersonPhoneNums.addView(phoneNumTextView);
        }
        ImageView mAddContactButton = (ImageView) itemView.findViewById(R.id.add_person_button);
        mAddContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPerson.addToContacts(mMainActivity);
            }
        });
        return itemView;
    }
}
