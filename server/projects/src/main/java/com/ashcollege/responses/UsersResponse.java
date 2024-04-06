package com.ashcollege.responses;

import com.ashcollege.entities.User;

import java.util.List;

public class UsersResponse extends BasicResponse
{
    private List<User> allUsers;

    public UsersResponse(List<User> allUsers) {
        this.allUsers = allUsers;
    }

    public UsersResponse(boolean success, Integer errorCode, List<User> allUsers) {
        super(success, errorCode);
        this.allUsers = allUsers;
    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<User> allUsers) {
        this.allUsers = allUsers;
    }
}
