package com.ashcollege.controllers;
import com.ashcollege.entities.Post;
import com.ashcollege.entities.User;
import com.ashcollege.responses.*;
import com.ashcollege.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ashcollege.utils.Errors.*;

@RestController
public class GeneralController
{
    @Autowired
    private DbUtils dbUtils ;



    @RequestMapping("/")
    public String test () {
        return "Hello From Server";
    }

    @RequestMapping("/sign-in")
    public LoginResponse checkUser (String username , String password )
    {
        boolean success = false ;
        Integer errorCode = null ;
        String token = dbUtils.SignIn(username , password);

        return new LoginResponse(token != null ,errorCode , token) ;
    }
    @RequestMapping("/get-posts")
    public PostsResponse getPosts (String token)
    {
        List<Post> userPost = dbUtils.getPosts(token);
        return new PostsResponse(userPost) ;
    }
    @RequestMapping("/get-imageUrl")
    public ImageResponse getImageUrl (String token)
    {
        String image = dbUtils.getImageUrl(token);
        return new ImageResponse(image) ;
    }


    @RequestMapping("/register")
    public RegisterResponse register ( String username , String password , String repeat , String imageUrl)
    {
        boolean success = false ;
        Integer errorCode = null ;
        Integer id = null;
        if( username != null)
        {
            if(password != null)
            {
              if(password.equals(repeat))
              {
                 if(usernameAvailable(username).isAvailable())
                 {
                     User user = new User();
                     user.setUsername(username);
                     user.setPassword(password);
                     user.setImageUrl(imageUrl);
                     dbUtils.registerUser(user);
                     id = user.getId();
                 }
                 else
                 {
                     errorCode = ERROR_USERNAME_NOT_AVAILABLE ;
                 }
              }
              else
              {
                  errorCode = ERROR_PASSWORDS_DONT_MATCH ;
              }
            }
            else
            {
                errorCode = ERROR_MISSING_PASSWORD ;
            }
        }
        else
        {
            errorCode = ERROR_MISSING_USERNAME ;
        }

        return new RegisterResponse(success , errorCode , id);

    }
    @RequestMapping("/username-available")
    public UsernameAvailableResponse usernameAvailable (String username)
    {
        boolean success = false ;
        Integer errorCode = null ;
        boolean available = false ;
        if(username != null) {
            available = dbUtils.usernameAvailable(username);
            success = true ;
        }
        else
        {
            errorCode = ERROR_MISSING_USERNAME ;
        }


        return new UsernameAvailableResponse( success , errorCode , available);


    }
    @RequestMapping("/get-all-users")

    public UsersResponse getAllUsers ()
    {
        List<User> allUsers = dbUtils.getAllUsers();
        return new UsersResponse(allUsers);
    }

    @RequestMapping("/add-new-post")

    public BasicResponse addNewPost (String content , String token)
    {
        dbUtils.addPost(content ,token);
        return new BasicResponse(true , null);
    }

    @RequestMapping("/remove-post")
    public BasicResponse removePost ( String token , int postId)
    {
        dbUtils.removePost(token , postId);
        return new BasicResponse(true , null);
    }
    @RequestMapping("/update-picture-profile")

    public BasicResponse updateProfilePicture ( String newImageUrl , String token)
    {
        dbUtils.updateProfilePicture(newImageUrl , token);
        return new BasicResponse(true , null);
    }

}
