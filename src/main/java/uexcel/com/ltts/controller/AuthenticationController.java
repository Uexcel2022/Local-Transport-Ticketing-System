package uexcel.com.ltts.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uexcel.com.ltts.dto.AuthenticationResponseDto;
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
    @GetMapping("verify-token-email")
    public ResponseEntity<String>  emailVerification(@PathParam("token") String token){
       return ResponseEntity.ok().body(authenticationService.verifyEmail(token));
    }


    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":"
                + request.getServerPort()
                + request.getContextPath();
    }

}
