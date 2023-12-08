package com.example.coinplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Budget extends AppCompatActivity {

    EditText budget_Amount,budget_Limit;
    Button  btnbudgetlimit,btnbudget;

    ImageButton back_button;

    TextView amount_budget, limit_budget;

    SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "MyPrefs";
    private static final String AMOUNT_KEY = "amountKey";
    private static final String LIMIT_KEY = "limitKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        budget_Amount = findViewById(R.id.budget_Amount);
        budget_Limit = findViewById(R.id.budget_Limit);

        amount_budget = findViewById(R.id.amount_budget);
        limit_budget = findViewById(R.id.limit_budget);

        back_button= findViewById(R.id.back_button);
        btnbudget = findViewById(R.id.btnbudget);
        btnbudgetlimit = findViewById(R.id.btnbudgetlimit);


        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Load saved values
        loadBudgetValues();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Budget.this, MainActivity.class));
            }
        });

        btnbudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String budgetAmount = budget_Amount.getText().toString();
                amount_budget.setText("Ksh " + budgetAmount);
                saveBudgetValue(AMOUNT_KEY, budgetAmount);
                Toast.makeText(Budget.this, "Budget Amount: " + budgetAmount, Toast.LENGTH_SHORT).show();

            }
        });

        btnbudgetlimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String budgetLimit = budget_Limit.getText().toString();
               limit_budget.setText("Ksh " + budgetLimit);
                saveBudgetValue(LIMIT_KEY, budgetLimit);
                Toast.makeText(Budget.this, "Budget Limit: " + budgetLimit, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadBudgetValues() {
        String savedAmount = sharedPreferences.getString(AMOUNT_KEY, "0");
        String savedLimit = sharedPreferences.getString(LIMIT_KEY, "0");

        amount_budget.setText("Ksh " + savedAmount);
        limit_budget.setText("Ksh " + savedLimit);
    }

    private void saveBudgetValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}