package com.shuli.apps.shemesh;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

class Person {
    private String name;
    private String address1;
    private String address2;
    private String[] phoneNums;


    Person(String name, String address1, String address2, String[] phoneNums) {
        this.name = name;
        this.address1 = address1;
        this.address2 = address2;
        this.phoneNums = phoneNums;
    }

    public String getName() {
        return name;
    }

    String getAddress1() {
        return address1;
    }

    String getAddress2() {
        return address2;
    }

    String[] getPhoneNums() {
        return phoneNums;
    }

    void addToContacts(final Context context) {
        new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_person_add)
                .setTitle(R.string.add_to_contacts_dialog_title)
                .setMessage(context.getString(R.string.add_contact_dialog_add) + name +
                        context.getString(R.string.add_contact_dialog_to_your_contacts))
                .setPositiveButton(R.string.dialog_yes_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
                        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                        intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address1 + context.getString(R.string.beit_shemesh));
                        ArrayList<ContentValues> data = new ArrayList<>();
                        for (String phoneNum : phoneNums) {
                            phoneNum = "+972-" + phoneNum.substring(1);//use international standard format
                            ContentValues row = new ContentValues();
                            row.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
                            row.put(Phone.NUMBER, phoneNum);
                            data.add(row);
                        }
                        intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.dialog_no_button, null)
                .show();
    }
}
