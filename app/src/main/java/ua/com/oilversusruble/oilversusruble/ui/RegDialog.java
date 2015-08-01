package ua.com.oilversusruble.oilversusruble.ui;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import ua.com.oilversusruble.oilversusruble.MainActivity;
import ua.com.oilversusruble.oilversusruble.R;

/**
 * Created by User on 21.05.2015.
 */
public class RegDialog implements OnClickListener {
    private EditText etRegUsername, etRegPassword1, etRegPassword2, etRegEmail;
    private ProgressBar progressBar;
    private Button btnRegisterD, btnCancelReg;
    private Dialog dialog;
    private MainActivity activity;

    public RegDialog( MainActivity activity ){
        this.activity = activity;
        init();
    }

    private void init(){
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_register);
        dialog.setTitle("Register");

        etRegUsername = (EditText) dialog.findViewById(R.id.etRegUsername);
        etRegPassword1 = (EditText) dialog.findViewById(R.id.etRegPassword1);
        etRegPassword2 = (EditText) dialog.findViewById(R.id.etRegPassword2);
        etRegEmail = (EditText) dialog.findViewById(R.id.etRegEmail);
        progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);
        btnRegisterD = (Button) dialog.findViewById(R.id.btnRegisterD);
        btnCancelReg = (Button) dialog.findViewById(R.id.btnCancelReg);

        btnRegisterD.setOnClickListener(this);
        btnCancelReg.setOnClickListener(this);

    }

    public void show(){
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegister) {
            if (!(etRegUsername.getText().toString().equals("")
                    | etRegPassword1.getText().toString().equals("")
                    | etRegPassword2.getText().toString().equals("")) & etRegPassword1.getText().toString().equals(etRegPassword2.getText().toString())) {
                ParseUser user = new ParseUser();
                user.setUsername(etRegUsername.getText().toString());
                user.setPassword(etRegPassword1.getText().toString());
                if (isEmailValid(etRegEmail.getText().toString())) {
                    user.setEmail(etRegEmail.getText().toString());
                }

                progressBar.setIndeterminate(true);
                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            progressBar.setIndeterminate(false);
                            activity.updateUserParams();
                            dialog.dismiss();

                        } else {
                            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {
            activity.updateUserParams();
            dialog.dismiss();
        }
    }


    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}