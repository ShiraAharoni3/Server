package com.ashcollege.responses;

import com.ashcollege.entities.User;

import java.util.List;

public class ImageResponse extends BasicResponse
{
    private String imageUrl ;

    public ImageResponse(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ImageResponse(boolean success, Integer errorCode, String imageUrl) {
        super(success, errorCode);
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
