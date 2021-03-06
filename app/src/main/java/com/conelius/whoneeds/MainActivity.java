package com.conelius.whoneeds;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.conelius.whoneeds.data.DatabaseHandler;
import com.conelius.whoneeds.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveButton;
    private EditText item;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;
    private DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        databaseHandler = new DatabaseHandler(this);

        byPassActivity();
        //check if item was saved

        List<Item> items = databaseHandler.getAllItems();

        for (Item item : items) {
            Log.d("Main", "onCreate: "+item.getDateItemAdded());
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreatePopupDialog();

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void byPassActivity() {

        if (databaseHandler.getItemCount() > 0) {
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            finish();
        }


    }


    private void CreatePopupDialog() {

        builder = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.poup,null);

        item = view.findViewById(R.id.item);
        itemColor = view.findViewById(R.id.itemColor);
        itemQuantity = view.findViewById(R.id.itemQuantity);
        itemSize = view.findViewById(R.id.itemSize);

        saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!item.getText().toString().isEmpty()
                        && !itemColor.getText().toString().isEmpty()
                        && !itemQuantity.getText().toString().isEmpty()
                        && !itemSize.getText().toString().isEmpty()) {

                    saveItem(v);

                } else {

                    Snackbar.make(v,"Empty fields not alowed buddy !",Snackbar.LENGTH_SHORT).show();
                }


            }
        });

        builder.setView(view);
        dialog = builder.create();//creating our dialog object
        dialog.show();//important step
    }

    private void saveItem(View view) {
        //Todo: save each item to db
        Item saveitem = new Item();

        String newItem = item.getText().toString().trim();
        String newColor = itemColor.getText().toString().trim();
        int quantity = Integer.parseInt(itemQuantity.getText().toString().trim());
        int size = Integer.parseInt(itemSize.getText().toString().trim());

        saveitem.setItemName(newItem);
        saveitem.setItemColor(newColor);
        saveitem.setItemSize(size);
        saveitem.setItemQuantity(quantity);

        databaseHandler.addItem(saveitem);

        Snackbar.make(view,"item Saved",Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();

                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        },1200);

        //Todo: move to next screen
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
