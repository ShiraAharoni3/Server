package com.ashcollege.responses;

public class UsernameAvailableResponse extends BasicResponse
{
    private boolean available ;

    public UsernameAvailableResponse()
    {

    }

    public UsernameAvailableResponse(boolean available)
    {
        this.available = available;
    }

    public UsernameAvailableResponse(boolean success, Integer errorCode, boolean available) {
        super(success, errorCode);
        this.available = available;
    }



    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
