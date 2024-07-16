package org.example;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptPasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "your_password";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        System.out.println("Encoded password: " + encodedPassword);
    }
}