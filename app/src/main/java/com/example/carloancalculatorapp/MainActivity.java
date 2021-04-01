//Liping Wu 300958061 COMP304 2020Summer
package com.example.carloancalculatorapp;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity<date> extends AppCompatActivity {
    //Variable Declaration
    EditText editTextLoanAmount;
    Spinner spinnerDuration;
    TextView textViewStartDate;
    TextView textViewResult1;
    TextView textViewResult2;
    private int mYear, mMonth, mDay;
    double rate =2.0;
    int totalLoan=10000;
    int duration=1;
    Integer[] durations ={1,2,3,4,5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Setting up the references to various views
        textViewStartDate =  findViewById(R.id.textViewStartDate);
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        textViewStartDate.setText(mDay + "/" + (mMonth + 1) + "/" + mYear );
        editTextLoanAmount = findViewById(R.id.editTextLoanAmount);
        //editTextLoanAmount.setText(totalLoan);

        spinnerDuration = findViewById(R.id.spinnerDuration);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, durations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDuration.setAdapter(adapter);

        textViewResult1 = findViewById(R.id.textViewTotalLoan);
        textViewResult2 = findViewById(R.id.textViewMonthlyInstall);
    }

    //select start date
    public void onStartDateClick(View v) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if(compare(year,  monthOfYear,  dayOfMonth) >= 0){   //if select a date of past
                            //textViewStartDate.setText("DD/MM/YYYY");
                            textViewStartDate.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);  //OR set current Date
                            Toast.makeText(MainActivity.this, "Please Select a Date of Future.", Toast.LENGTH_LONG).show();
                        } else {
                            textViewStartDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);  //if select a date of current or future
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    //method compare two date( current date and selected date)
    public int compare(int year, int month, int day) {
        if (mYear != year )
            return mYear - year;
        if (mMonth != month)
            return mMonth - month;
        return mDay - day;
    }

    //method to get interest rate
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton1:
                if(checked)
                     rate = 2.0;
                break;
            case R.id.radioButton2:
                if(checked)
                     rate = 2.5;
                break;
            case R.id.radioButton3:
                if(checked)
                     rate = 3.0;
                break;
        }
    }

    //method calculate alert dialog
    public void onCalculateBtnClick(View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure want to calculate loan?");
        alertDialogBuilder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        calculateLoan();
                    }
                });

        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"You clicked No button", Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //method getTotalLoan
    private int getTotalLoan()
    {
        // Getting input from editTextLoanAmount
        String s1 = editTextLoanAmount.getText().toString();
        // condition to check if box is not empty
        if (s1.equals(null)  || s1.equals("") || Integer.parseInt(s1) < 0) {
            String result = "Please enter a postive total loan amount";
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            return 0;
        }
        else {
            // converting string to int.
            return Integer.parseInt(s1);
        }
    }

    //method calculate loan
    private void calculateLoan() {
        // get the input numbers
        totalLoan = getTotalLoan();
        if (totalLoan>0) {
            duration = Integer.parseInt(spinnerDuration.getSelectedItem().toString());
            double monthlyInstallment = totalLoan * (1 + rate/100)/(duration *12);
            double totalInterest = totalLoan * rate/100;
            textViewResult1.setText("Total Loan Amount is $"+totalLoan);
          //  textViewResult2.setText("Monthly Installment is $" + (int)monthlyInstallment);  //monthlypay as int
            textViewResult2.setText("Monthly Installment is $" + String.format("%.2f", monthlyInstallment) );  // monthly pay as double .2f
            Toast.makeText(MainActivity.this,"Your Car loan total amount: $"+totalLoan+ ", total interest: $" + String.format("%.2f", totalInterest), Toast.LENGTH_LONG).show();
        } else {
            textViewResult1.setText("Total Loan Amount is $"+totalLoan);
            textViewResult2.setText("The Total Loan must be positive!");
        }
    }
}
