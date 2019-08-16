package com.androboy.padcassignment3;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ContactViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_view);


        TextView tvName = findViewById(R.id.tv_full_name);
        tvName.setText(getIntent().getStringExtra(MainActivity.NAME));

        TextView tvPhone = findViewById(R.id.tv_phone);
        tvPhone.setText(getIntent().getStringExtra(MainActivity.PHONE_NO));
    }
}
