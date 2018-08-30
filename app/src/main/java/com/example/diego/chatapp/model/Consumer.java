package com.example.diego.chatapp.model;

public interface Consumer<T> {
    void accept(T t);
}
