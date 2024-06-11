package uexcel.com.ltts.service;

import uexcel.com.ltts.dto.AuthenticationResponseDto;
import uexcel.com.ltts.dto.EmailPasswordChangeDto;
import uexcel.com.ltts.dto.SignupDto;

import java.util.Map;
public interface AuthenticationService {

    AuthenticationResponseDto register(SignupDto request, String url);

    AuthenticationResponseDto authenticate(Map<String, String> login);

    String verifyEmail(String token);

    String freshTokenVfyEmail(String requestUrl,EmailPasswordChangeDto request);

    String freshTokenChgPwd(String requestUrl,EmailPasswordChangeDto request);

    String freshTokenChgEmail(String requestUrl,EmailPasswordChangeDto request);

   String emailPasswordReset(String token, String requestUri,
                             String contextPath, EmailPasswordChangeDto request);

    String changePasswordWithin(EmailPasswordChangeDto request);
}
