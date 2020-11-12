package com.example.morsel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


public class aboutus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);


        Element adsElement = new Element();
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.morsel_logo)
                .setDescription(" Morsel:No more left overs " +
                        "An initiative to feed all the hungry people." +
                        "Make a difference through just one click")
                .addItem(new Element().setTitle("Version 4.0"))
                .addGroup("CONNECT WITH US!")
                .addEmail("sudarshana0602@gmail.com")
                .addWebsite("https://console.firebase.google.com/u/0/project/morsel-a2477/storage/morsel-a2477.appspot.com/files~2Fimages")
                .addPlayStore("com.example.morsel")
                .addInstagram("instagram")
                .addItem(createCopyright())
                .create();
        setContentView(aboutPage);
    }

    private Element createCopyright() {
        Element copyright = new Element();
        @SuppressLint("DefaultLocale") final String copyrightString = String.format("Copyright %d Morsel", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        copyright.setIconDrawable(R.drawable.morsel_logo);
//        copyright.setIcon(R.mipmap.ic_launcher);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(aboutus.this, copyrightString, Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;
    }
}