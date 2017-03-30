package com.example.bruker.edubackpromo.data.models;

/**
 * Created by Anne on 23.02.2017.
 */

public class User {
    public String fullName;
    public boolean isStudent;
    public User(){

    }

    public User(boolean isStudent, String fullName) {
        this.isStudent = isStudent;
        this.fullName = fullName;
    }
}
