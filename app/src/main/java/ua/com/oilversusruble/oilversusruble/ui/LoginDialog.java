package ua.com.oilversusruble.oilversusruble.ui;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import ua.com.oilversusruble.oilversusruble.MainActivity;
import ua.com.oilversusruble.oilversusruble.R;

/**
 * Created by User on 21.05.2015.
 */
public class LoginDialog implements OnClickListener {
    private EditText etLogUsername, etLogPassword;
    private ProgressBar loginProgressBar;
    private Button btnLoginD, btnCancelLog;
    private Dialog dialog;
    private MainActivity activity;

    public LoginDialog(MainActivity activity){
        this.activity = activity;
        init();
    }

    private void init(){
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_login);
        dialog.setTitle("Login");

        etLogUsername = (EditText) dialog.findViewById(R.id.etLogUsername);
        etLogPassword = (EditText) dialog.findViewById(R.id.etLogPassword);
        loginProgressBar = (ProgressBar) dialog.findViewById(R.id.loginProgressBar);
        btnLoginD = (Button) dialog.findViewById(R.id.btnLoginD);
        btnCancelLog = (Button) dialog.findViewById(R.id.btnCancelLog);
        loginProgressBar.setIndeterminate(true);




        btnLoginD.setOnClickListener(this);
        btnCancelLog.setOnClickListener(this);

    }

    public void show(){
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLoginD){
            if (!(etLogUsername.getText().toString().equals("") | etLogPassword.getText().toString().equals(""))){
                ParseUser.logInInBackground(etLogUsername.getText().toString(), etLogPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (e == null) {
                            activity.updateUserParams();
                            loginProgressBar.setIndeterminate(false);
                            dialog.dismiss();


                        } else {
                            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }else{
            activity.updateUserParams();

            dialog.dismiss();
        }
    }

}