package com.example.demo.context;

public class UserContext {
    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();

    private Long userId;
    private String username;

    public static UserContext getCurrentUser() {
        return userContext.get();
    }

    public static void setCurrentUser(UserContext context) {
        userContext.set(context);
    }

    public static void clear() {
        userContext.remove();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
} 