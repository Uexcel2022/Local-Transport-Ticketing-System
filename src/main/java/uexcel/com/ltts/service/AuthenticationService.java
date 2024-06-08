package uexcel.com.ltts.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import uexcel.com.ltts.dto.AuthenticationResponseDto;
import uexcel.com.ltts.dto.SignupDto;

import java.util.Map;
public interface AuthenticationService {
    AuthenticationResponseDto register(SignupDto request,String url);

    AuthenticationResponseDto authenticate(Map<String, String> login);

    String verifyEmail(String token);
}
