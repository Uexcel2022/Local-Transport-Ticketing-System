package uexcel.com.ltts.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uexcel.com.ltts.dto.SignupDto;
import uexcel.com.ltts.service.ClientService;

@RestController
@RequestMapping("api/v1")
public class ClientController {

    private static final Logger log = LoggerFactory.getLogger(ClientController.class);
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("client-signup")
    public ResponseEntity<String> signup(@RequestBody SignupDto signupDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.processSignup(signupDto));
    }
}
