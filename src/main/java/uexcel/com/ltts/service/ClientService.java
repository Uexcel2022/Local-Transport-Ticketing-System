package uexcel.com.ltts.service;

import jakarta.websocket.server.PathParam;
import uexcel.com.ltts.dto.ClientInfoDto;
import uexcel.com.ltts.dto.SignupDto;
import uexcel.com.ltts.entity.Client;

import java.util.List;

public interface ClientService {
    String updateClient(SignupDto client);
    List<Client> getAllClient();
    ClientInfoDto getClient(String id);
}
