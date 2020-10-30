package kreishravi.cp470.androidassignments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "Toolbar Activity";
    String snackBarMsg = "";
    EditText customMessage = null;
    View customView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.FloatButtonMsg), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        snackBarMsg = getString(R.string.Option1Msg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.menu_main, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_one:
                Snackbar.make(findViewById(R.id.option_one), snackBarMsg, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            case R.id.option_two:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title)
                        .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                return true;
            case R.id.option_three:
                LayoutInflater inflater = TestToolbar.this.getLayoutInflater();
                customView = inflater.inflate(R.layout.snackbar_text_change, null);
                AlertDialog.Builder customBuilder = new AlertDialog.Builder(this);
                customBuilder.setView(customView)
                            .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    customMessage = customView.findViewById(R.id.custom_dialog_message);
                                    String newMsg = customMessage.getText().toString();
                                    if(!newMsg.matches("")) {
                                        snackBarMsg = newMsg;
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                        .create()
                        .show();
                return true;
            case R.id.option_four:
                Toast optionToast = Toast.makeText(this, "Version 1.0 by Kreishanth R.", Toast.LENGTH_LONG);
                optionToast.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}