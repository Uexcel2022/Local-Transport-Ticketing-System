package uexcel.com.ltts.service;

import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import uexcel.com.ltts.dto.AuthenticationResponseDto;
import uexcel.com.ltts.dto.ClientInfoDto;
import uexcel.com.ltts.dto.SignupDto;
import uexcel.com.ltts.entity.Client;
import uexcel.com.ltts.entity.Wallet;
import uexcel.com.ltts.exception.CustomException;
import uexcel.com.ltts.util.Repos;
import uexcel.com.ltts.util.Validation;

import java.security.Principal;
import java.util.List;


@Service
public class ClientServiceImp implements ClientService {

    private final Validation validation;
    private final Repos repos;
    AuthenticationResponseDto authenticationResponseDto;

    public ClientServiceImp(Repos repos, Validation validation) {
        this.validation = validation;
        this.repos = repos;
    }

    @Override
    @Transactional
    public String updateClient(SignupDto request) {

        Client toUpdateClient = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        if(toUpdateClient.getEmail()!=null && !toUpdateClient.getEmail().equals(request.getEmail())) {
            validation.ifExist(request.getEmail(), request.getPhone());
        }

        if(toUpdateClient.getPhone()!=null && !toUpdateClient.getPhone().equals(request.getPhone())) {
            validation.ifExist(request.getEmail(), request.getPhone());
        }

        toUpdateClient.setFullName(validation.validateName(request.getFullName()));
        toUpdateClient.setPhone(validation.validatePhone(request.getPhone()));
        toUpdateClient.setEmail(validation.validateEmail(request.getEmail()));
        toUpdateClient.setDateOfBirth(validation.validateAge(request.getDateOfBirth()));
        toUpdateClient.setGender(request.getGender());
        toUpdateClient.setNFullName(validation.validateName(request.getNFullName()));
        toUpdateClient.setNPhone(validation.validatePhone(request.getNPhone()));
        toUpdateClient.setDateOfBirth(validation.validateAge(request.getDateOfBirth()));
        repos.getClientRepository().save(toUpdateClient);


        return "Client updated successfully";
    }

    @Override
    public List<Client> getAllClient() {
        return (List<Client>) repos.getClientRepository().findAll();
    }

    @Override
    public ClientInfoDto getClient(String id) {
       Client client =  repos.getClientRepository().findById(id).orElseThrow(()->
               new CustomException("Client not found","404"));
       Wallet wallet = repos.getWalletRepository().findByWalletNumber(client.getPhone());

       if(wallet==null){
            throw new CustomException("Wallet not found", "404");
        }
       ClientInfoDto clientInfoDto = new ClientInfoDto();
        clientInfoDto.setId(client.getId());
       clientInfoDto.setFullName(client.getFullName());
       clientInfoDto.setPhone(client.getPhone());
       clientInfoDto.setEmail(client.getEmail());
       clientInfoDto.setDateOfBirth(client.getDateOfBirth());
       clientInfoDto.setBalance(wallet.getBalance());
       clientInfoDto.setNFullName(client.getNFullName());
       clientInfoDto.setNPhone(client.getNPhone());
       return clientInfoDto;

    }


}
