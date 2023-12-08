package com.example.coinplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class addExpense extends AppCompatActivity {

    ImageButton back_button;
    EditText ksh_Amount,categories,description_input;

    Button btnaddexp;


    //firebase
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense);

        back_button=findViewById(R.id.back_button);

        ksh_Amount=findViewById(R.id.ksh_Amount);
        categories=findViewById(R.id.categories);
        description_input=findViewById(R.id.description_input);

        btnaddexp = findViewById(R.id.btnaddexp);



        // initializing Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Expenses");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String limitAmount = sharedPreferences.getString("limitKey", "0");



        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(addExpense.this, MainActivity.class));
            }
        });

        //adding expense functionality
        btnaddexp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String kshAmount = ksh_Amount.getText().toString();
                String category = categories.getText().toString();
                String description = description_input.getText().toString();

                if (Integer.parseInt(kshAmount) > Integer.parseInt(limitAmount)) {
                    // Display an error message if the amount exceeds the limit
                    Toast.makeText(addExpense.this, "Expense amount exceeds the budget limit!", Toast.LENGTH_SHORT).show();
                }
                else {
                    saveExpensesData(kshAmount, category, description);
                    retrieveExpenseData();
                }



            }

            private void retrieveExpenseData() {
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int totalExpense = 0;

                        for(DataSnapshot myRefSnapshot : snapshot.getChildren())
                        {
                            Expenses expenses = myRefSnapshot.getValue(Expenses.class);
                            if(expenses != null){
                                totalExpense += Integer.parseInt(expenses.getKshAmount());
                            }
                        }




                        //pass the totalExpense value back to Main Activity
                        Intent intent = new Intent(addExpense.this, MainActivity.class);
                        intent.putExtra("totalExpense", totalExpense);
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle errors
                        Log.e("FirebaseError", "Error retrieving data from Firebase: " + error.getMessage());

                        // Display an error message using Toast
                        Toast.makeText(addExpense.this, "Error retrieving data from Firebase", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            private void saveExpensesData(String kshAmount, String categories, String description) {
                if (TextUtils.isEmpty(kshAmount) || TextUtils.isEmpty(categories) || TextUtils.isEmpty(description)) {
                    // Show an error message if any of the fields is empty
                    Toast.makeText(addExpense.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Proceed to save the expense data if all fields are filled
                    // Create a unique key for each expense entry
                    String expenseId = myRef.push().getKey();

                    // Create an Expense object
                    Expenses expense = new Expenses(kshAmount, categories, description);

                    Log.d("Debug", "Expense ID: " + expenseId);
                    Log.d("Debug", "Expense object: " + expense.toString());

                    // Save the expense data in Firebase
                    myRef.child(expenseId).setValue(expense)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d("Debug", "Expense data saved successfully!");

                                    // Clear the input fields
                                    ksh_Amount.setText("");
                                    // category_input.setText("");
                                    description_input.setText("");

                                    // Display a success message using Toast
                                    Toast.makeText(addExpense.this, "Expense data saved successfully!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e("Error", "Error saving expense data: " + task.getException());
                                }
                            });
                }
            }


        });


    }
}