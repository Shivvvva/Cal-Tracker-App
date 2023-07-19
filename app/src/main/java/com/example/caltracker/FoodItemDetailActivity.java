package com.example.caltracker;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.caltracker.Data.DatabaseHandler;
import com.example.caltracker.Model.Food;

public class FoodItemDetailActivity extends AppCompatActivity {
    private TextView detFoodName, detCalories, detDate;
    private int foodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_detail);

        setSupportActionBar((Toolbar) findViewById(R.id.detToolbar));

        detFoodName = (TextView) findViewById(R.id.detFoodTxt);
        detCalories = (TextView) findViewById(R.id.detCaloriesValueTxt);
        detDate = (TextView) findViewById(R.id.detDateTxt);

        Button detShareBtn = (Button) findViewById(R.id.detShareBtn);
        Intent prevIntent = getIntent();
        Food myFood = (Food) prevIntent.getExtras().getSerializable("userObj");

        detFoodName.setText(myFood.getName());
        detDate.setText(myFood.getRecordDate());
        detCalories.setText(String.valueOf(myFood.getCalories()));

        foodId = myFood.getId();

        detCalories.setTextSize(34.9f);
        detCalories.setTextColor(Color.RED);


        detShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareFoodRecord();
            }
        });

    }

    private void shareFoodRecord() {

        String dataString = " Food: " + detFoodName.getText().toString() + "\n" +
                " Calories: " + detCalories.getText().toString() + "\n" +
                " Eaten on: " + detDate.getText().toString() + "\n";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_SUBJECT, "I Ate This Food: Caloric Report");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"recipient@example.com"});
        intent.putExtra(Intent.EXTRA_TEXT, dataString);

        try{
            startActivity(Intent.createChooser(intent,"Send Email"));

        }catch (ActivityNotFoundException e){
            Toast.makeText(getApplicationContext(),
                    "Install Email client application to share",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.deleteItem){
            deleteAlertDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAlertDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(FoodItemDetailActivity.this);
        alertBuilder.setTitle("Delete?");
        alertBuilder.setMessage("Are you sure you wish to delete this item?");
        alertBuilder.setNegativeButton("No", null);
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.deleteFood(foodId);

                Toast.makeText(getApplicationContext(),
                        "Food Item Deleted", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(
                        FoodItemDetailActivity.this, DisplayFoodActivity.class));

                FoodItemDetailActivity.this.finish();

                db.close();

            }
        });
        alertBuilder.show();
    }
}
