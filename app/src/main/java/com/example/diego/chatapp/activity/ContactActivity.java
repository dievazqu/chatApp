package com.example.diego.chatapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.diego.chatapp.R;
import com.example.diego.chatapp.manager.DatabaseManager;
import com.example.diego.chatapp.model.BiConsumer;
import com.example.diego.chatapp.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.diego.chatapp.model.Constants.*;

public class ContactActivity extends AppCompatActivity {

    @BindView(R.id.contact_list)
    ListView contactListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);

        final String myKey = PreferenceManager.getDefaultSharedPreferences(this).getString(SHARED_PREFERENCES_KEY, null);
        final ArrayAdapter adapter = new ContactAdapter(this);
        contactListView.setAdapter(adapter);
        DatabaseManager.getInstance().listenUsers(new BiConsumer<String, String>() {
            @Override
            public void accept(String key, String name) {
                if(!myKey.equals(key)) {
                    adapter.add(new User(key, name));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseManager.getInstance().stopListeningUsers();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.delete_session:

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                String key = pref.getString(SHARED_PREFERENCES_KEY, null);
                pref.edit()
                        .remove(SHARED_PREFERENCES_KEY)
                        .remove(SHARED_PREFERENCES_NAME)
                        .apply();
                DatabaseManager.getInstance().removeUser(key);
                goToLoginActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToChatActivity(User user){
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(CHAT_ACTIVITY_KEY, user.getKey());
        intent.putExtra(CHAT_ACTIVITY_NAME, user.getName());
        startActivity(intent);
    }

    public void goToLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private class ContactAdapter extends ArrayAdapter<User> {

        public ContactAdapter(Context context) {
            super(context, R.layout.contact_adapter);
        }

        public ContactAdapter(Context context, int resource, List<User> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.contact_adapter, null);
            }

            final User user = getItem(position);
            TextView contactName = v.findViewById(R.id.contact_name);
            contactName.setText(user.getName());

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToChatActivity(user);
                }
            });
            return v;
        }
    }

}
