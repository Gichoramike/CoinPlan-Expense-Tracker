package com.example.coinplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class addIncome extends AppCompatActivity {

    ImageButton back_button;
    EditText ksh_Amount, categories, description_input;
    Button btnaddincome;

    // Firebase
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_income);

        back_button = findViewById(R.id.back_button);
        ksh_Amount = findViewById(R.id.ksh_Amount);
        categories = findViewById(R.id.categories);
        description_input = findViewById(R.id.description_input);
        btnaddincome = findViewById(R.id.btnaddincome);

        // Initializing Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Incomes");

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(addIncome.this, MainActivity.class));
            }
        });

        btnaddincome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String kshAmount = ksh_Amount.getText().toString();
                String category = categories.getText().toString();
                String description = description_input.getText().toString();

                saveIncomeData(kshAmount, category, description);

                if (kshAmount.isEmpty()) {
                    Toast.makeText(addIncome.this, "Please enter the income amount", Toast.LENGTH_SHORT).show();
                    saveIncomeData(kshAmount, category, description);
                }

            }




            private void saveIncomeData(String kshAmount, String category, String description) {
                if (TextUtils.isEmpty(kshAmount) || TextUtils.isEmpty(category) || TextUtils.isEmpty(description)) {
                    // Show an error message if any of the fields is empty
                    Toast.makeText(addIncome.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Proceed to save the income data if all fields are filled
                    // Create a unique key for each income entry
                    String incomeId = myRef.push().getKey();

                    // Create an Income object
                    Incomes income = new Incomes(kshAmount, category, description);

                    Log.d("Debug", "Income ID: " + incomeId);
                    Log.d("Debug", "Income object: " + income.toString());

                    // Save the income data in Firebase
                    myRef.child(incomeId).setValue(income)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d("Debug", "Income data saved successfully!");

                                    // Clear the input fields
                                    ksh_Amount.setText("");
                                    // category_input.setText("");
                                    description_input.setText("");

                                    // Display a success message using Toast
                                    Toast.makeText(addIncome.this, "Income data saved successfully!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e("Error", "Error saving income data: " + task.getException());
                                }
                            });
                }
            }

        });
    }
}
