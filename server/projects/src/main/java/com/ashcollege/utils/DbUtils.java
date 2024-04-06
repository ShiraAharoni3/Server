package com.ashcollege.utils;
import com.ashcollege.entities.Post;
import com.ashcollege.entities.User;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Component
public class DbUtils {
    private Connection connection = null;

    @PostConstruct
    public Connection createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "shira", "shira12345678");
            System.out.println("Connection success");

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;


    }

    public boolean registerUser(User user) {
        String defultImageUrl = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png";
        if (user.getImageUrl().length() == 0) {
            user.setImageUrl(defultImageUrl);
        }
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO users (username , password , imageUrl) VALUE (? , ? , ?)");
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getImageUrl());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public String SignIn(String username, String password) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT users.id FROM users WHERE username = ? AND password = ? ");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                String token = null;
                Random random = new Random();
                int rand = random.nextInt();
                token = String.valueOf(rand) + username + String.valueOf(rand / 100) + String.valueOf(rand % 10);
                preparedStatement =
                        connection.prepareStatement("UPDATE users SET token = ? WHERE id = ?");
                preparedStatement.setString(1, token);
                preparedStatement.setInt(2, id);
                preparedStatement.executeUpdate();
                return token;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean usernameAvailable(String username) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT users.username FROM users WHERE username = ? ");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return !resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> AllUsers = new ArrayList<>();
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT id, username , password FROM users");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                AllUsers.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return AllUsers;

    }

    public List<Post> getPosts(String token)
    {
        List<Post> posts = new ArrayList<>();
        try {
            int userId = getUserIdFromToken(token); // Retrieve the user id using the token
            if (userId != -1) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT id , content FROM posts WHERE user_id = ? ORDER BY date DESC LIMIT 20 ");
                preparedStatement.setInt(1 , userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) // כל עוד יש לו עוד פוסט
                {
                    int postId = resultSet.getInt(1);
                    String content = resultSet.getString(2);
                    Post post = new Post(postId , content);
                    posts.add(post);
                }
            }

        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return posts;

}

   /* public List<Post> getPosts (String token)
    {
        List<Post> posts = new ArrayList<>();
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT id FROM users WHERE token =? ") ;
            preparedStatement.setString(1 , token);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                int id = resultSet.getInt(1);
                preparedStatement = connection.prepareStatement("SELECT id , content FROM posts WHERE user_id = ? ORDER BY date DESC LIMIT 20 ");
                preparedStatement.setInt(1 , id);
                resultSet = preparedStatement.executeQuery() ;
                while (resultSet.next()) // כל עוד יש לו עוד פוסט
                {
                   int postId = resultSet.getInt(1);
                   String content = resultSet.getString(2);
                   Post post = new Post(postId , content);
                   posts.add(post);
                }
            }

        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return posts;
    } */

    public String getImageUrl(String token) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT imageUrl FROM users WHERE token = ?");
            preparedStatement.setString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    private int getUserIdFromToken(String token) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM users WHERE token = ?");
        preparedStatement.setString(1, token);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("id");
        }
        return -1;
    }

    // Method to add a new post
    public void addPost(String content, String token) {
        try {
            int userId = getUserIdFromToken(token);
            if (userId != -1) {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO posts (content, date, user_id) VALUE (?, NOW(), ?)");
                preparedStatement.setString(1, content);
                preparedStatement.setInt(2, userId);
                preparedStatement.executeUpdate();
            } else {
                // Handle the case where no user is found for the given token
                System.out.println("User not found for token: " + token);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void removePost( String token , int postId)
    {
        try {
            int userId = getUserIdFromToken(token); // Retrieve the user id using the token
            if (userId != -1)
            { // Check if userId is valid
                PreparedStatement preparedStatement =
                        connection.prepareStatement("DELETE FROM posts WHERE id = ? AND user_id = ?");
                preparedStatement.setInt(1, postId);
                preparedStatement.setInt(2, userId);
                preparedStatement.executeUpdate();

           }
            else {

                System.out.println("User not found for token: " + token);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /*public void addPost ( String content ,String token) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT id FROM users WHERE token =? ");
            preparedStatement.setString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                preparedStatement = connection.prepareStatement("INSERT INTO posts (content, date, user_id) VALUE (? , NOW() ,?)");
                preparedStatement.setString(1, content);
                preparedStatement.setInt(2, id);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    } */
        /*public void removePost( String token , int postId)
        {
            try {
                PreparedStatement preparedStatement =
                        connection.prepareStatement("SELECT id FROM users WHERE token =? ");
                preparedStatement.setString(1, token);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    int userId = resultSet.getInt(1);
                    preparedStatement =
                            connection.prepareStatement("DELETE FROM posts WHERE id = ? AND user_id = ?");
                    preparedStatement.setInt(1, postId);
                    preparedStatement.setInt(2, userId);
                    preparedStatement.executeUpdate();
                }
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }*/

    public void updateProfilePicture ( String newImageUrl , String token)
    {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE users SET imageUrl = ? WHERE token = ?");
            preparedStatement.setString(1, newImageUrl);
            preparedStatement.setString(2, token);
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
