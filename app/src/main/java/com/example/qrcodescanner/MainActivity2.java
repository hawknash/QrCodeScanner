package com.example.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shreyaspatil.easyupipayment.EasyUpiPayment;
import com.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import com.shreyaspatil.easyupipayment.model.PaymentApp;
import com.shreyaspatil.easyupipayment.model.TransactionDetails;

public class MainActivity2 extends AppCompatActivity implements PaymentStatusListener {
    TextView textView;
    EditText amount;
    Button payButton;
    PaymentApp paymentApp;
    private EasyUpiPayment easyUpiPayment;
    String name = "", vpa = "", am = "";
    String transactionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        textView = findViewById(R.id.text);
        amount = findViewById(R.id.amount);
        payButton = findViewById(R.id.payButton);

        Intent i = getIntent();
        //  textView.setText(i.getStringExtra("value"));
        String url = i.getStringExtra("value");
        String string = url.replace("#", "?");
        vpa = Uri.parse(string).getQueryParameter("pa");
        name = Uri.parse(string).getQueryParameter("pn");
        textView.setText(name);
        Toast.makeText(MainActivity2.this, "Name: " + name + " Uri: " + vpa + " " + url, Toast.LENGTH_LONG).show();
        amount.addTextChangedListener(filterTextWatcher);


        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                transactionId = "TID" + System.currentTimeMillis();
                // Toast.makeText(MainActivity2.this,am,Toast.LENGTH_LONG).show();
                startPayment();
            }
        });


    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // DO THE CALCULATIONS HERE AND SHOW THE RESULT AS PER YOUR CALCULATIONS
            if (s.length() != 0) {
                am = String.format("%.2f", Double.parseDouble(s.toString()));
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void startPayment() {
      //  paymentApp = PaymentApp.PHONE_PE;
        EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(MainActivity2.this)
                .setPayeeVpa(vpa)
                .setPayeeName(name)
                .setPayeeMerchantCode("")
                .setTransactionId(transactionId)
                .setTransactionRefId(transactionId)
                .setDescription("pay")
                .setAmount(am);

        try {
            // Build instance
            easyUpiPayment = builder.build();

            // Register Listener for Events
            easyUpiPayment.setPaymentStatusListener(MainActivity2.this);

            // Start payment / transaction
            easyUpiPayment.startPayment();
        } catch (Exception exception) {
            exception.printStackTrace();

        }
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        // Transaction Completed
        Log.d("TransactionDetails", transactionDetails.toString());


        switch (transactionDetails.getTransactionStatus()) {
            case SUCCESS:
                onTransactionSuccess();
                break;
            case FAILURE:
                onTransactionFailed();
                break;
            case SUBMITTED:
                onTransactionSubmitted();
                break;
            default:
                toast("Cancelled");
                break;
        }
    }

    @Override
    public void onTransactionCancelled() {
        // Payment Cancelled by User
        toast("Cancelled");
    }

    private void onTransactionSuccess() {
        // Payment Success
        toast("Success");

    }

    private void onTransactionSubmitted() {
        // Payment Pending
        toast("Pending | Submitted");

    }

    private void onTransactionFailed() {
        // Payment Failed
        toast("Failed");

    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    }

