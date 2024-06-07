package uexcel.com.ltts.controller;

import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uexcel.com.ltts.dto.ClientInfoDto;
import uexcel.com.ltts.dto.SignupDto;
import uexcel.com.ltts.entity.Client;
import uexcel.com.ltts.service.ClientService;

import java.util.List;
@RestController
@RequestMapping("api/v1")
public class ClientController {
    private static final Logger log = LoggerFactory.getLogger(ClientController.class);
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PutMapping("update-client")
    public ResponseEntity<String> updateClient(@RequestBody SignupDto request) {
        return ResponseEntity.ok().body(clientService.updateClient(request));
    }

    @GetMapping("get-all-client")
    public ResponseEntity<List<Client>> updateClient() {
        return ResponseEntity.ok().body(clientService.getAllClient());
    }
    @GetMapping("client")
    public ResponseEntity<ClientInfoDto> getClientInfo(@PathParam("id")String id) {
       return ResponseEntity.ok().body(clientService.getClient(id));
    }
}
