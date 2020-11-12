package com.example.morsel;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrackActivity extends AppCompatActivity {
    View con_div_1,view_order_processed,view_order_pickup,con_div_2,view_order_pickup_1;
    ImageView img_orderconfirmed,orderprocessed,orderpickup,orderpickup1;
    TextView textorderpickup,text_confirmed,textorderprocessed,textorderpickedup1,tv4,pd,pd1,prd;
    String id;
    String status;
    Button b;
    DatabaseReference ref;
    private View view_order_placed,view_order_confirmed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        tv4=findViewById(R.id.textView4);
        b=findViewById(R.id.Picked);
        textorderprocessed=findViewById(R.id.textorderprocessed);
        prd=findViewById(R.id.processed_desc);

        textorderpickup=findViewById(R.id.textorderpickup);
        pd=findViewById(R.id.pickup_desc);

        textorderpickedup1=findViewById(R.id.textorderpickup1);
        pd1=findViewById(R.id.pickup_desc1);

        view_order_placed=findViewById(R.id.view_order_placed);
        view_order_confirmed=findViewById(R.id.view_order_confirmed);
        view_order_processed=findViewById(R.id.view_order_processed);
        view_order_pickup=findViewById(R.id.view_order_pickup);
        view_order_pickup_1=findViewById(R.id.view_order_pickup1);
        con_div_1=findViewById(R.id.con_divider1);
        con_div_2=findViewById(R.id.con_divider2);

       // ready_divider=findViewById(R.id.ready_divider);


        text_confirmed=findViewById(R.id.text_confirmed);


        img_orderconfirmed=findViewById(R.id.img_orderconfirmed);
        orderprocessed=findViewById(R.id.orderprocessed);
        orderpickup=findViewById(R.id.orderpickup);
        orderpickup1=findViewById(R.id.orderpickup1);
        Intent intent=getIntent();
        String orderStatus=intent.getStringExtra("tripid");
        ref= FirebaseDatabase.getInstance().getReference().child("dnmapping").child(orderStatus).child("status");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                status=snapshot.getValue().toString();
                getOrderStatus(status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.setValue("3");
                status="3";
                getOrderStatus("3");
            }
        });
        tv4.setText(id);





    }

    private void getOrderStatus(String orderStatus) {
        if (orderStatus.equals("0")){
            float alfa= (float) 0.5;
            setStatus(alfa);

        }else if (orderStatus.equals("1")){
            float alfa= (float) 1;
            setStatus1(alfa);



        }else if (orderStatus.equals("2")){
            float alfa= (float) 1;
            setStatus2(alfa);


        }else if (orderStatus.equals("3")){
            float alfa= (float) 1;
            setStatus3(alfa);
        }
        else if(orderStatus.equals("4")){
            float alfa=(float) 1;
            setStatus4(alfa);
        }
    }


    private void setStatus(float alfa) {
        float myf= (float) 0.5;
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        orderprocessed.setAlpha(alfa);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        con_div_1.setBackground(getResources().getDrawable(R.drawable.shape_status_current));

        img_orderconfirmed.setAlpha(alfa);
        text_confirmed.setAlpha(alfa);
        textorderprocessed.setAlpha(alfa);
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        orderpickup.setAlpha(alfa);
        textorderpickup.setAlpha(myf);




    }

    private void setStatus1(float alfa) {
        float myf= (float) 0.5;
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        orderprocessed.setAlpha(myf);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        con_div_1.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        img_orderconfirmed.setAlpha(alfa);

        text_confirmed.setAlpha(alfa);
        textorderprocessed.setAlpha(myf);
        view_order_pickup.setAlpha(myf);
        //ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        orderpickup.setAlpha(myf);
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        textorderpickup.setAlpha(myf);
    }

    private void setStatus2(float alfa) {
        float myf= (float) 0.5;
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        orderprocessed.setAlpha(myf);
        img_orderconfirmed.setAlpha(alfa);

        text_confirmed.setAlpha(alfa);
        textorderprocessed.setAlpha(myf);

        con_div_1.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));

        orderprocessed.setAlpha(alfa);
        textorderprocessed.setAlpha(1);
        prd.setAlpha(1);


    }

    private void setStatus3(float alfa) {

        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        orderprocessed.setAlpha(alfa);
        img_orderconfirmed.setAlpha(alfa);

        text_confirmed.setAlpha(alfa);
        textorderprocessed.setAlpha(alfa);

        con_div_1.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));

        orderprocessed.setAlpha(alfa);
        textorderprocessed.setAlpha(1);
        prd.setAlpha(1);

        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));

        orderpickup.setAlpha(alfa);
        textorderpickup.setAlpha(1);
        pd.setAlpha(1);
    }

    private void setStatus4(float alfa)
    {
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        orderprocessed.setAlpha(alfa);
        img_orderconfirmed.setAlpha(alfa);

        text_confirmed.setAlpha(alfa);
        textorderprocessed.setAlpha(alfa);

        con_div_1.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));

        orderprocessed.setAlpha(alfa);
        textorderprocessed.setAlpha(alfa);
        prd.setAlpha(alfa);

        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));

        orderpickup.setAlpha(alfa);
        textorderpickup.setAlpha(alfa);
        pd.setAlpha(alfa);

        con_div_2.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_pickup_1.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));

        orderpickup1.setAlpha(alfa);
        textorderpickedup1.setAlpha(1);
        pd1.setAlpha(1);
    }
}