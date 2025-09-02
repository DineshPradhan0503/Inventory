package com.inventory.management.services;

import com.inventory.management.payload.request.LoginRequest;
import com.inventory.management.payload.request.SignupRequest;
import com.inventory.management.payload.response.JwtResponse;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    void registerUser(SignupRequest signupRequest);
}
