package com.example.quicksaveapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText editName, editOrder, editLocation;
    Button buttonSave;
    TextView textViewResult;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        database = openOrCreateDatabase("app", MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS orders(name VARCHAR,order_number VARCHAR,location VARCHAR)");

        EditText editName = findViewById(R.id.editName);
        EditText editOrder = findViewById(R.id.editOrder);
        EditText editLocation = findViewById(R.id.editLocation);
        Button buttonSave = findViewById(R.id.buttonSave);
        TextView textViewResult = findViewById(R.id.textViewResult);


        buttonSave.setOnClickListener(v -> {
            String name = editName.getText().toString();
            String order = editOrder.getText().toString();
            String location = editLocation.getText().toString();

            String sql = ("INSERT INTO orders(name,order_number,location) VALUES (?,?,?)");
            SQLiteStatement stmt = database.compileStatement(sql);

            stmt.bindString(1, name);
            stmt.bindString(2, order);
            stmt.bindString(3, location);
            stmt.execute();

            Cursor cursor = database.rawQuery("SELECT * FROM orders", null);
            StringBuilder result = new StringBuilder();
            while (cursor.moveToNext()) {
                result.append("Name: ").append(cursor.getString(0))
                        .append("Order: ").append(cursor.getString(1))
                        .append("Location: ").append(cursor.getString(2))
                        .append("\n\n");
            }
            cursor.close();
            textViewResult.setText(result.toString());
        });

    }
}