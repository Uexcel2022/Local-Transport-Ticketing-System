package uexcel.com.ltts.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uexcel.com.ltts.dto.AuthenticationResponseDto;
import uexcel.com.ltts.dto.SignupDto;
import uexcel.com.ltts.entity.Client;
import uexcel.com.ltts.entity.VerificationToken;
import uexcel.com.ltts.entity.Wallet;
import uexcel.com.ltts.event.SignupCompleteEvent;
import uexcel.com.ltts.exception.CustomException;
import uexcel.com.ltts.util.RepositoryService;
import uexcel.com.ltts.util.Validation;

import java.util.Date;
import java.util.Map;

@Service
public class AuthenticationServiceImp implements AuthenticationService  {
    private final RepositoryService repositoryService;
    private final JwtService jwtService;
    private final Validation validation;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ApplicationEventPublisher eventPublisher;

    public AuthenticationServiceImp(RepositoryService repositoryService, JwtService jwtService, Validation validation,
                                    PasswordEncoder passwordEncoder, AuthenticationManager
                                            authenticationManager, ApplicationEventPublisher eventPublisher) {
        this.repositoryService = repositoryService;
        this.jwtService = jwtService;
        this.validation = validation;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.eventPublisher = eventPublisher;
    }

     public AuthenticationResponseDto register(SignupDto request, String url){

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
        repositoryService.getWalletRepository().save(wallet);
        eventPublisher.publishEvent(new SignupCompleteEvent(client,url));
        String token = jwtService.generateJwtToken(client);
        return new AuthenticationResponseDto(token);

    }

    @Override
    public AuthenticationResponseDto authenticate(Map<String, String> request) {

      authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.get("email"), request.get("password")));

       Client client = repositoryService.getClientRepository().findByEmail(request.get("email"));
       if(client == null){
           throw  new CustomException("Client not found.","404");
       }

        if(client.getStatus().equals("locked")){
            throw  new CustomException("You account has been locked.","401");
        }

        if(client.getStatus().equals("deactivated")){
            throw  new CustomException("Client not found.","404");
        }

        if(!client.isVerified()){
            throw  new CustomException("Please verify your email.","401");
        }

       String token = jwtService.generateJwtToken(client);

        return new AuthenticationResponseDto(token);
    }

    @Override
    public String verifyEmail(String token) {
       VerificationToken vt = repositoryService.getVerificationTokenRepository().findByToken(token);
       if(vt == null){
           throw  new CustomException("Token is invalid.","400");
       }
       if(isValidToke(vt.getDate())){
           vt.getClient().setVerified(true);
           repositoryService.getVerificationTokenRepository().save(vt);
           repositoryService.getVerificationTokenRepository().delete(vt);
           return "Verification successful.";
       }
       repositoryService.getVerificationTokenRepository().delete(vt);
        return "Session has expired.";
    }


    private Boolean isValidToke(Date date){
        return date.after(new Date());
        }
}
