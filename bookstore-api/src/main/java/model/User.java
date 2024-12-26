package model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class User {
    @JsonProperty(value = "userName")
    private final String name;
    private final String password;

    public User(String userName, String password) {
        this.name = userName;
        this.password = password;
    }
}
