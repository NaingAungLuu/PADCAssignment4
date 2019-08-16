package com.androboy.padcassignment3;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.VideoView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO = 1;
    private static final int REQUEST_SELECT_CONTACT = 2;
    public static final String PROFILE_URI = "VIDEO_URI";
    public static final String NAME = "NAME";
    public static final String PHONE_NO = "PHONE_NO";
    public static final String EMAIL_ADDRESS = "EMAIL_ADDRESS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting the onClickListener for cvCreateTimer
        CardView cvCreateTimer = findViewById(R.id.cv_create_timer);
        cvCreateTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildNumberInputDialog();
            }
        });

        CardView cvCreateEvent = findViewById(R.id.cv_create_event);
        cvCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { createEvent("Assignment 4" , "Yangon");
            }
        });

        CardView cvCaptureVideo = findViewById(R.id.cv_capture_video);
        cvCaptureVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureVideo();
            }
        });

        CardView cvSelectContact = findViewById(R.id.cv_view_contact);
        cvSelectContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContactData();
            }
        });

        CardView cvSearchWeb = findViewById(R.id.cv_search_web);
        cvSearchWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildTextInputDialog();
            }
        });

    }



    void createTimer(int timerSeconds)
    {
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_MESSAGE , "Timer from App")
                .putExtra(AlarmClock.EXTRA_LENGTH , timerSeconds)
                .putExtra(AlarmClock.EXTRA_SKIP_UI , false);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    void buildNumberInputDialog()
    {
        AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Enter Seconds To Set Timer");
        final EditText etSeconds = new EditText(getApplicationContext());
        etSeconds.setInputType(InputType.TYPE_CLASS_NUMBER);
        alertDialog.setView(etSeconds);
        alertDialog.setPositiveButton("Start Timer", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {
                createTimer(Integer.parseInt(etSeconds.getText().toString()));
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    void buildTextInputDialog()
    {
        AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Enter what you want to search on Web");
        final EditText etQuery = new EditText(getApplicationContext());
        etQuery.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialog.setView(etQuery);
        alertDialog.setPositiveButton("Search", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {
               searchWeb(etQuery.getText().toString());
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    void createEvent(String Title , String Location)
    {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, Title)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, Location);
        startActivity(intent);
    }

    void captureVideo()
    {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_VIDEO);
        }
    }

    void searchWeb(String query)
    {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    void getContactData()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_CONTACT);
        }
    }

    void showContactData(Uri contactUri)
    {
        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.NUMBER
                };

        Cursor cursor = getContentResolver().query(contactUri, projection,
                null, null, null);

        String number  = "";
        if (cursor.moveToFirst()) {
            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            number = cursor.getString(numberIndex);
        }
        cursor.close();

        Intent showContactIntent = new Intent(getApplicationContext() , ContactViewActivity.class);
        showContactIntent.putExtra(NAME , retrieveContactName(contactUri));
        showContactIntent.putExtra(PHONE_NO , number);
        startActivity(showContactIntent);
    }

    String retrieveContactName(Uri uriContact) {

        String contactName = null;
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();
        return contactName;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_VIDEO && resultCode == RESULT_OK & data != null)
        {
            Uri videoUri =data.getData();
            Intent VideoViewIntent = new Intent(getApplicationContext() , VideoViewerActivity.class);
            VideoViewIntent.setData(videoUri);
            startActivity(VideoViewIntent);
        }
        else if(requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK && data != null)
        {
            Uri contactUri = data.getData();
            showContactData(contactUri);
        }
    }
}
