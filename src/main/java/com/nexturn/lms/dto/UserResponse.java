package com.nexturn.lms.dto;

import com.nexturn.lms.entity.User;
import com.nexturn.lms.utils.Role;

public class UserResponse {
    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;

    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

    public Integer getUserId() { return userId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
}