package uexcel.com.ltts.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uexcel.com.ltts.dto.AuthenticationResponseDto;
import uexcel.com.ltts.dto.EmailPasswordChangeDto;
import uexcel.com.ltts.dto.SignupDto;
import uexcel.com.ltts.service.AuthenticationService;

import java.util.Map;

@RestController
@RequestMapping("api/v1")
public class AuthenticationController {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;


    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;

    }

    @PostMapping("client-signup")
    public ResponseEntity<AuthenticationResponseDto> signup(@RequestBody SignupDto request,final HttpServletRequest servletRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(request,applicationUrl(servletRequest)));
    }

    @PostMapping("client-login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody Map<String, String> request){
        return ResponseEntity.ok().body(authenticationService.authenticate(request));
    }
    @GetMapping("verify-email")
    public ResponseEntity<String>  emailVerification(@PathParam("token") String token,
                                                     final HttpServletRequest servletRequest){
        log.info(servletRequest.getRequestURI());
       return ResponseEntity.ok().body(authenticationService.verifyEmail(token));
    }

    //process different
    @PutMapping(value = {"chg-email","chg-pwd"})
    public ResponseEntity<String> emailPasswordChange(@PathParam("token") String token,
                                                      @RequestBody EmailPasswordChangeDto request,
                                                      final HttpServletRequest servletRequest){
        String uri = servletRequest.getRequestURI();
        String contextPath = applicationUrl(servletRequest);

        return ResponseEntity.ok().body(authenticationService
                .emailPasswordReset(token,uri,contextPath,request));

    }
     //process different request
    @PostMapping(value = {"fresh-token-vfy-email","fresh-token-chg-pwd", "fresh-token-chg-email"})
    public ResponseEntity<String> getFreshToken(final HttpServletRequest servletRequest,
                                               @RequestBody EmailPasswordChangeDto emailPwdChgDto) {

        if("/api/v1/fresh-token-vfy-email".equals(servletRequest.getRequestURI())) {

            return ResponseEntity.ok().body(authenticationService
                    .freshTokenVfyEmail(applicationUrl(servletRequest),servletRequest.getRequestURI(),emailPwdChgDto));
        }
        if("/api/v1/fresh-token-chg-pwd".equals(servletRequest.getRequestURI())) {

            return ResponseEntity.ok().body(authenticationService
                    .freshTokenChgPwd(applicationUrl(servletRequest),servletRequest.getRequestURI(),emailPwdChgDto));
        }

        return ResponseEntity.ok().body(authenticationService
                .freshTokenChgEmail(applicationUrl(servletRequest),servletRequest.getRequestURI(),emailPwdChgDto));

    }
    @PutMapping("chg-pwd-within")
    public ResponseEntity<String> changePasswordWithin(@RequestBody EmailPasswordChangeDto request){
        return ResponseEntity.ok().body(authenticationService.changePasswordWithin(request));
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":"
                + request.getServerPort()
                + request.getContextPath();
    }
}
