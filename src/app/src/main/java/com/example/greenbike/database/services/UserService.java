package com.example.greenbike.database.services;

import com.example.greenbike.database.models.user.User;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.security.NoSuchAlgorithmException;

public class UserService {

    public User create(User entity) {
        return null;
    }

    public User getById(String id) {
        return null;
    }

    public List<User> getAll() {
        return null;
    }

    public User update(User entity) {
        return null;
    }

    public void delete(String id) {

    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

        String hashPassword = this.bytesToHex(encodedHash);

        return hashPassword;
    }

    private String bytesToHex(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);

        StringBuilder hexString = new StringBuilder(number.toString(16));

        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }
}
