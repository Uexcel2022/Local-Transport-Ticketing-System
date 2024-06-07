package uexcel.com.ltts.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uexcel.com.ltts.dto.AuthenticationResponseDto;
import uexcel.com.ltts.dto.SignupDto;
import uexcel.com.ltts.entity.Client;
import uexcel.com.ltts.entity.Wallet;
import uexcel.com.ltts.exception.CustomException;
import uexcel.com.ltts.util.Repos;
import uexcel.com.ltts.util.Validation;

import java.util.Map;
@Service
public class AuthenticationServiceImp implements AuthenticationService  {
    private final Repos repos;
    private final JwtService jwtService;
    private final Validation validation;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImp(Repos repos, JwtService jwtService, Validation validation,
                                    PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.repos = repos;
        this.jwtService = jwtService;
        this.validation = validation;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

     public AuthenticationResponseDto register(SignupDto request){

        validation.ifExist(request.getEmail(),request.getPhone());

        Client client = new Client();
        client.setFullName(validation.validateName(request.getFullName()));
        client.setPhone(validation.validatePhone(request.getPhone()));
        client.setEmail(validation.validateEmail(request.getEmail()));
        client.setDateOfBirth(validation.validateAge(request.getDateOfBirth()));

        client.setPassword(passwordEncoder.encode(validation.password(request.getPassword(),
                request.getConfirmPassword())));
        client.setGender(request.getGender());
        client.setRole(request.getRole());
        client.setStatus("active");
        client.setNFullName(validation.validateName(request.getNFullName()));
        client.setNPhone(validation.validatePhone(request.getNPhone()));
        client.setVerified(false);

        Wallet wallet = new Wallet();
        wallet.setStatus("active");
        wallet.setClient(client);
        repos.getWalletRepository().save(wallet);
        String token = jwtService.generateJwtToken(client);
        return new AuthenticationResponseDto(token);

    }

    @Override
    public AuthenticationResponseDto authenticate(Map<String, String> request) {

      authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.get("email"), request.get("password")));

       Client client = repos.getClientRepository().findByEmail(request.get("email"));
       if(client == null){
           throw  new CustomException("Client not found.","404");
       }

       String token = jwtService.generateJwtToken(client);

        return new AuthenticationResponseDto(token);
    }
}
