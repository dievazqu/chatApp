package com.example.diego.chatapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.diego.chatapp.R;
import com.example.diego.chatapp.manager.DatabaseManager;
import com.example.diego.chatapp.manager.RandomManager;
import com.example.diego.chatapp.model.Consumer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.diego.chatapp.model.Constants.*;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.chat_list)
    ListView chatList;

    @BindView(R.id.message_edit)
    EditText messageEdit;

    private String chatUserKey;
    private String localKey;
    private String localName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        final String chatUserName = intent.getStringExtra(CHAT_ACTIVITY_NAME);
        chatUserKey = intent.getStringExtra(CHAT_ACTIVITY_KEY);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        localKey = pref.getString(SHARED_PREFERENCES_KEY, null);
        localName = pref.getString(SHARED_PREFERENCES_NAME, null);

        getSupportActionBar().setTitle(chatUserName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final ArrayAdapter adapter = new ChatAdapter(this);
        chatList.setAdapter(adapter);
        DatabaseManager.getInstance().listenMessages(localKey, chatUserKey, new Consumer<String>() {
            @Override
            public void accept(String msg) {
                adapter.add(msg);
                chatList.setSelection(adapter.getCount() - 1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseManager.getInstance().stopListeningMessages(localKey, chatUserKey);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.roll_dice_4:
                rollDice(4);
                return true;
            case R.id.roll_dice_6:
                rollDice(6);
                return true;
            case R.id.roll_dice_8:
                rollDice(8);
                return true;
            case R.id.roll_dice_10:
                rollDice(10);
                return true;
            case R.id.roll_dice_12:
                rollDice(12);
                return true;
            case R.id.roll_dice_20:
                rollDice(20);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.send_button)
    public void sendMessage() {
        String message = messageEdit.getText().toString();
        if(message.length() < 1) {
            return;
        }
        messageEdit.setText("");
        DatabaseManager.getInstance().sendMessage(localKey, chatUserKey, localName + ": " + message);
    }

    private void rollDice(int faces){
        int dice = RandomManager.getInt(1, faces);
        DatabaseManager.getInstance().sendMessage(localKey, chatUserKey, "D"+faces+" de "+localName+": "+dice);
    }



    private class ChatAdapter extends ArrayAdapter<String> {

        ChatAdapter(Context context) {
            super(context, R.layout.chat_adapter);
        }

        public ChatAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.chat_adapter, null);
            }

            final String message = getItem(position);
            TextView messageView = v.findViewById(R.id.message_view);
            messageView.setText(message);

            return v;
        }
    }
}
