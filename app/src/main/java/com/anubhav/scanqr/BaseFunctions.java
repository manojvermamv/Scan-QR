package com.anubhav.scanqr;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class BaseFunctions {

    public static String getContactByPhoneNumber(Context context, String phoneNumber) {
        try {
            //Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, phoneNumber);
            String[] projection = {ContactsContract.Contacts.DISPLAY_NAME};
            Cursor cursor = context.getContentResolver().query(lookupUri, projection, null, null, null);
            if (cursor == null) {
                return phoneNumber;
            } else {
                String name = phoneNumber;
                try {
                    if (cursor.moveToFirst())
                        name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                } finally {
                    cursor.close();
                }
                return name;
            }
        } catch (Exception e) {
            return "";
        }
    }

}