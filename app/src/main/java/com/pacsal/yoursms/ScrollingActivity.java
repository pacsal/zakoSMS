package com.pacsal.yoursms;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ScrollingActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText contactNumber;
    Button addNumber;
    SwitchCompat switchCompat;
    ToggleButton toggleButton;
    LinearLayout namba;
    LinearLayout settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myDb = new DatabaseHelper(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = myDb.getAllData();
                if(res.getCount()==0) {
                    //show msg
                    Snackbar.make(view, "Hakuna namba iliyowezeshwa", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                } else {
                    while (res.moveToNext()) {
                        Snackbar.make(view, res.getString(1), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        });

        contactNumber = (EditText)findViewById(R.id.editTextNumber);
        addNumber = (Button)findViewById(R.id.phoneNumber);
        switchCompat = (SwitchCompat)findViewById(R.id.compatSwitch);
        toggleButton = (ToggleButton)findViewById(R.id.ondoaButton);
        namba = (LinearLayout)findViewById(R.id.layoutNamba);
        settings = (LinearLayout)findViewById(R.id.layoutSetting);

        contactNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addNumber.setEnabled(!contactNumber.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        chekAppStatus();
        AddData();
        switchNamba();
        delNumber();
    }

    public void rate(){
                try {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id="
                                    + getPackageName())));
                }
    }

    //methode to check app status if there is phone number or note
    public void chekAppStatus(){
        Cursor resu = myDb.getAllData();
        if(resu.getCount()==0) {
            //show msg if no phone number in sqlite
            Toast.makeText(getApplicationContext(), "zakoSMS ipo tayari, weka namba", Toast.LENGTH_SHORT).show();
        } else {
            //set visible the settings layout and remove namba Layout
            namba.setVisibility(View.GONE);
            settings.setVisibility(View.VISIBLE);
            String p = "";
            while (resu.moveToNext()) {
                p = resu.getString(2);
            }
            int ps = Integer.parseInt(p);
            if(ps==0){
                switchCompat.setChecked(false);
            }else {
                switchCompat.setChecked(true);
            }
        }
    }

    //select id number
    public String idNum(){
        String ps="";
        Cursor resu = myDb.getAllData();
        if(resu.getCount()==0) { //do nothing
        } else {
            while (resu.moveToNext()) {
                ps = resu.getString(0);
            }
        }

        return ps;
    }

    //methode to switch the app to send number or note to the saved number
    public void switchNamba(){

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    boolean isUpdated = myDb.updateData(idNum(), 1);
                    if (isUpdated = true) {
                        Toast.makeText(getApplicationContext(), "Umeruhusu.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "zakoSMS imeshindwa kuruhusu.", Toast.LENGTH_SHORT).show();
                        switchCompat.setChecked(false);
                    }
                } else {
                    boolean isUpdated = myDb.updateData(idNum(), 0);
                    if (isUpdated = true) {
                        Toast.makeText(getApplicationContext(), "Umesitisha.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "zakoSMS imeshindwa kusitisha.", Toast.LENGTH_SHORT).show();
                        switchCompat.setChecked(false);
                    }
                }
            }
        });
    }

    public void delNumber(){
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScrollingActivity.this);
                //set title
                alertDialogBuilder.setTitle("Ondoa Namba kweli?");
                //set msg
                alertDialogBuilder.setMessage("Thibitisha chaguo lako").setCancelable(false)
                        .setPositiveButton("Ndio", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //if this is clicked
                                //start
                                Integer isDeleted = myDb.deleteData(idNum());
                                if (isDeleted > 0) {
                                    toggleButton.setChecked(false);
                                    namba.setVisibility(View.VISIBLE);
                                    settings.setVisibility(View.GONE);
                                    switchCompat.setChecked(false);
                                    Toast.makeText(getApplicationContext(), "Namba imeondolewa.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Kuna tatizo, namba haijaondolewa", Toast.LENGTH_SHORT).show();
                                }
                                //end
                            }
                        })
                        .setNegativeButton("Hapana", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                //create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                //show it
                alertDialog.show();

            }
        });
    }

    public void AddData(){
        addNumber.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String k = contactNumber.getText().toString();
                        if (contactNumber.getText().toString().matches("")) {
                            Toast.makeText(getApplicationContext(), "Weka namba ya simu", Toast.LENGTH_LONG).show();
                            return;
                        }
                        else if (!((k.substring(0,1).matches("0"))||(k.substring(0,4).matches("\\+255")))){
                            Toast.makeText(getApplicationContext(), "Namba ya simu ni batili", Toast.LENGTH_LONG).show();
                            return;
                        }
                        else if (!((k.length()>=10)&&(k.length()<=13))){
                            Toast.makeText(getApplicationContext(), "Namba ya simu haijatimia", Toast.LENGTH_LONG).show();
                            return;
                        }
                        else {
                            boolean isInserted = myDb.insertData(contactNumber.getText().toString(),1);
                            if (isInserted = true){
                                Toast.makeText(ScrollingActivity.this, "Namba uliyoiweka imewezeshwa", Toast.LENGTH_LONG).show();
                                contactNumber.setText("");
                                namba.setVisibility(View.GONE);
                                settings.setVisibility(View.VISIBLE);
                                switchCompat.setChecked(true);
                            }
                            else
                                Toast.makeText(ScrollingActivity.this, "Tatizo! App haijaiwezesha namba", Toast.LENGTH_LONG).show();
                        }
                    }

                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rate) {
            rate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
