package com.example.diego.chatapp.manager;



import com.example.diego.chatapp.model.BiConsumer;
import com.example.diego.chatapp.model.Consumer;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManager {

    private static DatabaseManager instance = null;

    private final FirebaseDatabase firebaseDatabase;

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private DatabaseManager() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }


    private ChildEventListener listenUsers;

    public void listenUsers(final BiConsumer<String, String> userConsumer) {
        final DatabaseReference ref = firebaseDatabase.getReference("users");

        listenUsers = ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userConsumer.accept(dataSnapshot.getKey(), dataSnapshot.getValue(String.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void stopListeningUsers(){
        firebaseDatabase.getReference("users").removeEventListener(listenUsers);
    }

    public String newUser(String name) {
        final DatabaseReference ref = firebaseDatabase.getReference("users").push();
        ref.setValue(name);
        return ref.getKey();
    }


    private String getFinalKey(String localKey, String chatUserKey){
        if (localKey.compareTo(chatUserKey) < 0) {
            return localKey + chatUserKey;
        } else {
            return chatUserKey + localKey;
        }
    }

    private ChildEventListener listenMessages;

    public void listenMessages(String localKey, String chatUserKey, final Consumer<String> consumer) {
        final DatabaseReference ref = firebaseDatabase.getReference("chats/" + getFinalKey(localKey, chatUserKey));
        listenMessages = ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                consumer.accept(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void stopListeningMessages(String localKey, String chatUserKey){
        firebaseDatabase.getReference("chats/" + getFinalKey(localKey, chatUserKey)).removeEventListener(listenMessages);
    }

    public void sendMessage(String localKey, String chatUserKey, String message) {

        final DatabaseReference ref = firebaseDatabase.getReference("chats/" + getFinalKey(localKey, chatUserKey)).push();
        ref.setValue(message);
        return;
    }

    public void removeUser(String key) {
        final DatabaseReference ref = firebaseDatabase.getReference("users/" + key);
        ref.removeValue();
    }
}
