/*

Tested on:
GT-I9100 API 16
SM-N9005 API 21

 */

package com.example.peter.counter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    protected int counter = 0;
    public static final String CounterValue = "CounterValue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        SharedPreferences prefs = getSharedPreferences(CounterValue, MODE_PRIVATE);
        counter = prefs.getInt("counterValue", 0); //0 is the default value.

        //Előző érték kiolvasása a mentésből
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Visszaolvasott érték ellenőrzése, kell-e méretet módosítani
        changeTextSize();

        //Három gomb létrehozása, tulajdonságainak átírása
        Button increaseButton = (Button)findViewById(R.id.increaseButton);
        increaseButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        TextView counterText = (TextView) findViewById(R.id.counterText);
                        counter++;
                        changeTextSize();
                        counterText.setText(String.valueOf(counter));
                        saveValue();
                        checkZeroValueForDisable();
                    }
                }

        );

        Button decreaseButton = (Button)findViewById(R.id.decreaseButton);
        decreaseButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        TextView counterText = (TextView) findViewById(R.id.counterText);
                        counter--;
                        changeTextSize();
                        counterText.setText(String.valueOf(counter));
                        saveValue();
                        checkZeroValueForDisable();
                    }
                }
        );
        decreaseButton.setOnLongClickListener(
                new Button.OnLongClickListener(){
                    public boolean onLongClick(View v){
                        if (counter != 0) askAndReset();
                        return true;
                    }
                }
        );

        Button resetButton = (Button)findViewById(R.id.resetButton);
        resetButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        askAndReset();
                    }
                }
        );
    }

    private void checkZeroValueForDisable() {
        //Disables the reset button if the value is 0
        Button resetButton = (Button)findViewById(R.id.resetButton);
        if (counter == 0 ) resetButton.setEnabled(false);
        else resetButton.setEnabled(true);
    }

    private void saveValue() {
        //Saves the value for exit
        SharedPreferences.Editor editor = getSharedPreferences(CounterValue, MODE_PRIVATE).edit();
        editor.putInt("counterValue", counter);
        editor.commit();
    }

    private void changeTextSize() {
        //Checks if the text size should be changed, and changes it if it has many digits.
        TextView counterTextView=(TextView)findViewById(R.id.counterText);
        counterTextView.setText(String.valueOf(counter));
        if (counter<=-10 && counter>-100) counterTextView.setTextSize(150);
        else if (counter<=-100 && counter>-1000) counterTextView.setTextSize(100);
        else if (counter<=-1000 && counter>-10000) counterTextView.setTextSize(80);
        else if (counter>=100 && counter <1000) counterTextView.setTextSize(150); // Három számjegyes, tökéletes
        else if (counter>=1000 && counter < 10000) counterTextView.setTextSize(80);
        else if (counter>=10000 || counter <=-10000) counterTextView.setTextSize(75);
        else counterTextView.setTextSize(200);

    }


    //MENU

    public static final int MENU_RESET = Menu.FIRST;
    public static final int MENU_EXIT = Menu.FIRST + 1;
    public static final int MENU_ABOUT = Menu.FIRST + 2;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(Menu.NONE, MENU_ABOUT, Menu.NONE, "About");
        menu.add(Menu.NONE, MENU_RESET, Menu.NONE, "Reset");
        menu.add(Menu.NONE, MENU_EXIT, Menu.NONE, "Exit");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case MENU_RESET:
                if (counter != 0) askAndReset();
                return true;
            case MENU_EXIT:
                askAndExit();
                return true;
            case MENU_ABOUT:
                showAboutScreen();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showAboutScreen(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This is a simple counter application you can use for counting items or events.\n\n" +
                "This application is completely free, fully functional, and doesn't contain any ads.\n\n" +
                "Please give feedback in the Play Store!.\n\n" +
                "Thank you!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void askAndReset(){
        AlertDialog.Builder resetAlertDialogBuilder = new AlertDialog.Builder(this);

        resetAlertDialogBuilder.setTitle("Reset");
        resetAlertDialogBuilder.setMessage("Reset the counter?");


        resetAlertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                TextView counterText = (TextView) findViewById(R.id.counterText);
                counter = 0;
                changeTextSize();
                counterText.setText(String.valueOf(counter));

                saveValue();
                checkZeroValueForDisable();

                dialog.dismiss();
            }

        });

        resetAlertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Code that is executed when clicking NO

                dialog.dismiss();
            }

        });


        AlertDialog alert = resetAlertDialogBuilder.create();
        alert.show();
    }

    private void askAndExit() {
        AlertDialog.Builder exitAlertDialogBuilder = new AlertDialog.Builder(this);

        exitAlertDialogBuilder.setTitle("Exit");
        exitAlertDialogBuilder.setMessage("Are you sure?");

        exitAlertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
                System.exit(0);

                dialog.dismiss();
            }

        });

        exitAlertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Don't do anything when clicked NO

                dialog.dismiss();
            }

        });

        AlertDialog exitAlert = exitAlertDialogBuilder.create();
        exitAlert.show();

    }

    public void showAToast(String toastMessage){
        Context context = getApplicationContext();
        CharSequence text = toastMessage;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
