package com.example.diego.chatapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.diego.chatapp.R;
import com.example.diego.chatapp.manager.DatabaseManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.diego.chatapp.model.Constants.*;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.player_name_edit)
    EditText playerNameEdit;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String key = sharedPreferences.getString(SHARED_PREFERENCES_KEY, null);
        if(key != null){
            goToContactActivity();
        }
    }

    @OnClick(R.id.login_button)
    public void login(){
        final String name = playerNameEdit.getText().toString();
        if(name.length() >= 2){

            final String key = DatabaseManager.getInstance().newUser(name);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SHARED_PREFERENCES_NAME, name);
            editor.putString(SHARED_PREFERENCES_KEY, key);
            editor.apply();

            goToContactActivity();
        }
    }

    public void goToContactActivity(){
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
        finish();
    }
}
