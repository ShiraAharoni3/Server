package com.ashcollege.responses;

import com.ashcollege.entities.Post;

import java.util.List;

public class PostsResponse extends BasicResponse
{
    private List<Post> posts ;

    public PostsResponse(List<Post> posts) {
        this.posts = posts;
    }

    public PostsResponse(boolean success, Integer errorCode, List<Post> posts) {
        super(success, errorCode);
        this.posts = posts;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
