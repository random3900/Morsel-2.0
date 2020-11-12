package com.example.morsel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class vol_6 extends AppCompatActivity implements View.OnClickListener {
    ImageView img_src1, img_dst1;
    final Double[] sh_loc = new Double[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vol_6);
        img_src1 = findViewById(R.id.img_src);
        img_dst1 = findViewById(R.id.img_dst);
        img_src1.setOnClickListener(this);
        img_dst1.setOnClickListener(this);
        Bundle b = getIntent().getExtras();
        sh_loc[0] = b.getDouble("slat");
        sh_loc[1] = b.getDouble("slon");
        sh_loc[2] = b.getDouble("dlat");
        sh_loc[3] = b.getDouble("dlon");
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == img_src1.getId()) {
            Intent mapIntent;
            Uri location;
            String url = "http://maps.google.com/maps?q=loc:" + sh_loc[0] + "," + sh_loc[1] + " (" + "Porur" + ")";
            //String url=("geo:"+String.valueOf(sh_loc[0])+","+String.valueOf(sh_loc[1])+"?z=14");
            // Or map point based on latitude/longitude
            // location = Uri.parse("geo:37.422219,-122.08364?z=14");
            location = Uri.parse(url);
            // z param is zoom level
           /*
                1: World
                5: Landmass/continent
                10: City
                15: Streets
                20: Buildings
               */

            mapIntent = new Intent(Intent.ACTION_VIEW, location);
            startActivity(mapIntent);
            /*String orderStatus="1";
            Intent intent=new Intent(getApplicationContext(),TrackActivity.class);
            intent.putExtra("orderStatus",orderStatus);
            startActivity(intent);*/

        }
        else if(view.getId() == img_dst1.getId())
        {
            Intent mapIntent;
            Uri location;
            String url = "http://maps.google.com/maps?q=loc:" + sh_loc[2] + "," + sh_loc[3] + " (" + "Ambattur" + ")";
            //String url=("geo:"+String.valueOf(sh_loc[0])+","+String.valueOf(sh_loc[1])+"?z=14");
            // Or map point based on latitude/longitude
            // location = Uri.parse("geo:37.422219,-122.08364?z=14");
            location = Uri.parse(url);
            // z param is zoom level
           /*
                1: World
                5: Landmass/continent
                10: City
                15: Streets
                20: Buildings
               */

            mapIntent = new Intent(Intent.ACTION_VIEW, location);
            startActivity(mapIntent);
            /*String orderStatus="1";
            Intent intent=new Intent(getApplicationContext(),TrackActivity.class);
            intent.putExtra("orderStatus",orderStatus);
            startActivity(intent);*/
        }
    }
}