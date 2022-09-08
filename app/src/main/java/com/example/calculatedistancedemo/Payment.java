package com.example.calculatedistancedemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

public class Payment extends AppCompatActivity implements PaymentResultWithDataListener {

        Button btpay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        btpay = findViewById(R.id.bt_pay);
        String sAmount = "100";

        int amount = Math.round(Float.parseFloat(sAmount) * 100);
        btpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Checkout checkout = new Checkout();

                checkout.setKeyID("rzp_test_sNummj8smufOdn");
                checkout.setImage(com.razorpay.R.drawable.rzp_logo);
                JSONObject object = new JSONObject();
                try {
                    object.put("name","Android Coding");
                    object.put("Description","Test Paymnet");
                    object.put("theme.color","0093DD");
                    object.put("Currency","INR");
                    object.put("amount",amount);
                    checkout.open(Payment.this,object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment ID");
        builder.setMessage(s);
        builder.show();

    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}