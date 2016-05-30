package com.example.bang.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {
    TextView contactTextView;
    private static final int PICK_CONTACT_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);

        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.content);

        layout.addView(textView);
//        contact text view
        contactTextView = (TextView) findViewById(R.id.contact);
        String defaultValue = getResources().getString(R.string.saved_contact);
//        shared preference
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
//        contact
        String contact = sharedPref.getString(getString(R.string.saved_contact),defaultValue);
//        set text
        contactTextView.setText(contact);

    }

    public void pickContact(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_CONTACT_REQUEST) {
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.Contacts.DISPLAY_NAME}, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                String name = cursor.getString(columnIndex);
                Log.d("CONTACTS", "onActivityResult: " + name);

                contactTextView.setText(name);
//                save to shared pref
                SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.saved_contact), name);
//                commit save
                editor.commit();
            }

        }
    }
}
