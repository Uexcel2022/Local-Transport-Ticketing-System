package uexcel.com.ltts.service;

import uexcel.com.ltts.dto.AuthenticationResponseDto;
import uexcel.com.ltts.dto.SignupDto;

import java.util.Map;

public interface AuthenticationService {
    AuthenticationResponseDto register(SignupDto signupDto);

    AuthenticationResponseDto authenticate(Map<String, String> login);
}
