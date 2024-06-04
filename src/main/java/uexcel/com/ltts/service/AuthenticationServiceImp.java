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

     public AuthenticationResponseDto register(SignupDto signupDto){

        validation.ifExist(signupDto.getEmail(),signupDto.getPhone());

        Client client = new Client();
        client.setFullName(validation.validateName(signupDto.getFullName()));
        client.setPhone(validation.validatePhone(signupDto.getPhone()));
        client.setEmail(validation.validateEmail(signupDto.getEmail()));
        client.setDateOfBirth(validation.validateAge(signupDto.getDateOfBirth()));
        client.setPassword(passwordEncoder.encode(validation.password(signupDto.getPassword(),
                signupDto.getConfirmPassword())));
        client.setGender(signupDto.getGender());
        client.setRole(signupDto.getRole());
        client.setStatus("active");
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
           throw  new CustomException("User not found","404");
       }

       String token = jwtService.generateJwtToken(client);

        return new AuthenticationResponseDto(token);
    }
}
