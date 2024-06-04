package uexcel.com.ltts.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<AuthenticationResponseDto> signup(@RequestBody SignupDto signupDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(signupDto));
    }

    @PostMapping("client-login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody Map<String, String> login){
        return ResponseEntity.ok().body(authenticationService.authenticate(login));
    }

}
