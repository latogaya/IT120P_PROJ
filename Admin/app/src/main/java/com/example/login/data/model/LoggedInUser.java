package com.example.login.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String name;
    private String email;
    private String password;
    private byte[] image;

    public LoggedInUser(String name, String email, String password, byte[] image) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.image = image;
    }


    public String getName() {
        return name;
    }
    public void setname() {this.name = name;}
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {this.email = email;}
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {this.password = password;}
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {this.image = image;}
}