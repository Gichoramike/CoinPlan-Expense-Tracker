package com.example.coinplan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ImageButton button_income, button_expense, button_budget;
    TextView balance_amount, expense_amount, income_amount;

    private ArrayList<expense> expenseList = new ArrayList<>();
    private ExpenseAdapter expenseAdapter;

    private int totalIncome = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeactivity);

        listView = findViewById(R.id.listView);
        balance_amount = findViewById(R.id.balance_amount);
        expense_amount = findViewById(R.id.expense_amount);
        income_amount = findViewById(R.id.income_amount);

        button_income = findViewById(R.id.button_income);
        button_expense = findViewById(R.id.button_expense);
        button_budget = findViewById(R.id.button_budget);

        // initializing the ExpenseAdapter and set it to the ListView
        expenseAdapter = new ExpenseAdapter(this, R.layout.list_row, expenseList);
        listView.setAdapter(expenseAdapter);

        // fetching expense data from Firebase
        fetchExpenseData();

        // fetching income data from Firebase
        fetchIncomeData();

        // Getting value for addExpense class
        if (getIntent().hasExtra("totalExpense")) {
            int totalExpense = getIntent().getIntExtra("totalExpense", 0);

            // update the TextView with the total expense
            expense_amount.setText("Ksh " + String.valueOf(totalExpense));
        }

        // Getting value for addIncome class
        if (getIntent().hasExtra("totalIncome")) {
            int totalIncome = getIntent().getIntExtra("totalIncome", 0);

            // update the TextView with the total expense
            income_amount.setText("Ksh " + String.valueOf(totalIncome));
        }

        // bottom Navigation functionality
        button_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, addIncome.class));
            }
        });

        button_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, addExpense.class));
            }
        });

        button_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Budget.class));
            }
        });
    }

    private void fetchIncomeData() {
        DatabaseReference incomeRef = FirebaseDatabase.getInstance().getReference("Incomes");
        incomeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalIncome = 0;

                for (DataSnapshot incomeSnapshot : snapshot.getChildren()) {
                    Incomes incomeData = incomeSnapshot.getValue(Incomes.class);
                    if (incomeData != null && incomeData.getKshAmounts() != null) {
                        try {
                            totalIncome += Integer.parseInt(incomeData.getKshAmounts());
                        } catch (NumberFormatException e) {
                            Log.e("NumberFormatException", "Error parsing income amount: " + e.getMessage());
                        }
                    }
                }

                // updating the TextView with total income
                income_amount.setText("Ksh " + String.valueOf(totalIncome));

                // calculate and update the balance
                calculateBalance();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error retrieving data from Firebase: " + error.getMessage());
                Toast.makeText(MainActivity.this, "Error retrieving income data from Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchExpenseData() {
        DatabaseReference expensesRef = FirebaseDatabase.getInstance().getReference("Expenses");
        expensesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                expenseList.clear();
                int totalExpense = 0;

                for (DataSnapshot expenseSnapshot : snapshot.getChildren()) {
                    expense expenseData = expenseSnapshot.getValue(expense.class);
                    if (expenseData != null && expenseData.getKshAmount() != null) {
                        try {
                            int expenseAmount = Integer.parseInt(expenseData.getKshAmount());
                            totalExpense += expenseAmount;
                            expenseList.add(expenseData);
                        } catch (NumberFormatException e) {
                            Log.e("NumberFormatException", "Error parsing expense amount: " + e.getMessage());
                        }
                    }
                }

                // updating the TextView with total expense
                expense_amount.setText("Ksh " + String.valueOf(totalExpense));

                calculateBalance();

                // Notify the adapter that the data has changed
                Collections.reverse(expenseList);
                expenseAdapter.notifyDataSetChanged();

                // Set an item click listener for the ListView
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Handle item click, e.g., show a dialog for update/delete
                        showUpdateDeleteDialog(position);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
                Log.e("FirebaseError", "Error retrieving data from Firebase: " + error.getMessage());

                // Display an error message using Toast
                Toast.makeText(MainActivity.this, "Error retrieving data from Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateBalance() {
        int balance = totalIncome - Integer.parseInt(expense_amount.getText().toString().replace("Ksh ", ""));

        // Update the TextView with the balance
        balance_amount.setText("Ksh " + String.valueOf(balance));
    }

    private void showUpdateDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an action");
        builder.setItems(new CharSequence[]{"Update", "Delete"}, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Update item: implement a method to show an update dialog
                        showUpdateDialog(position);
                        break;
                    case 1:
                        // Delete item: implement a method to delete the item
                        deleteExpense(position);
                        break;
                }
            }
        });
        builder.show();
    }




    //show update dialog
    private void showUpdateDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Expense");

        // Inflate a layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.update_expense_dialog, null);
        builder.setView(dialogView);

        // Retrieve the current values of the selected expense
        final expense selectedExpense = expenseList.get(position);
        final EditText updatedCategory = dialogView.findViewById(R.id.updatedCategory);
        final EditText updatedDescription= dialogView.findViewById(R.id.updatedDescription);
        final EditText updatedAmount= dialogView.findViewById(R.id.updatedAmount);

        // Set the current values in the dialog
        updatedCategory.setText(selectedExpense.getCategory());
        updatedDescription.setText(selectedExpense.getDescription());
        updatedAmount.setText(selectedExpense.getKshAmount());

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Get the updated values from the dialog
                String updatedC = updatedCategory.getText().toString().trim();
                String updatedD = updatedDescription.getText().toString().trim();
                String updatedA= updatedAmount.getText().toString().trim();


                // Update the selected expense item
                expense updatedExpense = new expense(updatedC, updatedD, updatedA);
                expenseAdapter.updateItem(position, updatedExpense);

                // Dismiss the dialog
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Dismiss the dialog
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }


    private void deleteExpense(int position) {
        DatabaseReference expensesRef = FirebaseDatabase.getInstance().getReference("Expenses");
        String expenseKey = expenseList.get(position).getKey();

        // Remove the item from the database
        expensesRef.child(expenseKey).removeValue();

        // Remove the item from the ArrayList
        expenseAdapter.removeItem(position);

        // Recalculate balance after deletion
        calculateBalance();
    }
}
