package uexcel.com.ltts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uexcel.com.ltts.dto.AuthenticationResponseDto;
import uexcel.com.ltts.dto.EmailPasswordChangeDto;
import uexcel.com.ltts.dto.SignupDto;
import uexcel.com.ltts.entity.Client;
import uexcel.com.ltts.entity.VerificationToken;
import uexcel.com.ltts.entity.Wallet;
import uexcel.com.ltts.event.SignupCompleteEvent;
import uexcel.com.ltts.exception.CustomException;
import uexcel.com.ltts.util.Validation;

import java.util.Date;
import java.util.Map;

@Service
public class AuthenticationServiceImp implements AuthenticationService  {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImp.class);
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

        client.setPassword(passwordEncoder.encode(validation.validatePassword(request.getPassword(),
                request.getConfirmPassword())));
        client.setGender(request.getGender());
        client.setStatus("active");
        client.setNFullName(validation.validateName(request.getNFullName()));
        client.setNPhone(validation.validatePhone(request.getNPhone()));
        client.setVerified(false);

        Wallet wallet = new Wallet();
        wallet.setClient(client);
//        repositoryService.getWalletRepository().save(wallet);
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
    @Transactional
    public String verifyEmail(String token) {
       VerificationToken vT = repositoryService.getVerificationTokenRepository()
               .findByToken(token);
       if(vT == null){
           throw  new CustomException("Token is invalid.","400");
       }

       if(isValidToke(vT.getDate())){
           vT.getClient().setVerified(true);
           repositoryService.getVerificationTokenRepository().save(vT);
           repositoryService.getVerificationTokenRepository().delete(vT);
           return "Verification successful.";
       }
       repositoryService.getVerificationTokenRepository().delete(vT);
        return "Session has expired.";
    }

    @Override
    @Transactional
    public String emailPasswordReset(String token, String requestUri,
                                     String servletContext, EmailPasswordChangeDto request){
        boolean isEmailChange = false;
        VerificationToken vT = repositoryService.getVerificationTokenRepository()
                .findByToken(token);
        if(vT == null){
            throw  new CustomException("Invalid token.","400");
        }

        if(isValidToke(vT.getDate())){

            if("/api/v1/chg-pwd".equals(requestUri)) {

                vT.getClient().setPassword(passwordEncoder.encode(
                        validation.validatePassword(request.getNewPassword(),
                                request.getConfirmPassword())));
            }

            if("/api/v1/chg-email".equals(requestUri)) {
                vT.getClient().setEmail(validation.validateEmail(request.getEmail()));
                vT.getClient().setVerified(false);
                isEmailChange = true;
            }
            repositoryService.getVerificationTokenRepository().save(vT);
            repositoryService.getVerificationTokenRepository().delete(vT);
            if(isEmailChange) {
                EmailPasswordChangeDto emailPwdChgDto = new EmailPasswordChangeDto();
                emailPwdChgDto.setEmail(vT.getClient().getEmail());
                return freshTokenVfyEmail(servletContext, (emailPwdChgDto));
            }
            return "Success";
        }
        repositoryService.getVerificationTokenRepository().delete(vT);
        return "Session has expired.";

    }


    @Override
    @Transactional
    public String freshTokenVfyEmail(String servletContext,EmailPasswordChangeDto request) {
        String url = servletContext+"/verify-email?token=";
        return getTokenAndFullUrlPath(url,request.getEmail());

    }

    @Override
    @Transactional
    public String freshTokenChgPwd(String requestUrl,EmailPasswordChangeDto request) {
        String url = requestUrl+"/chg-pwd?token=";
        return getTokenAndFullUrlPath(url,request.getEmail());
    }

    @Override
    @Transactional
    public String freshTokenChgEmail(String requestUrl,EmailPasswordChangeDto request) {
        String url = requestUrl+"/chg-email?token=";
        return getTokenAndFullUrlPath(url,request.getEmail());
    }

    public String changePasswordWithin(EmailPasswordChangeDto request){

        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            throw  new CustomException("Client not found.","404");
        }
        Client principal = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = repositoryService.getClientRepository().findByEmail(principal.getEmail());
        if(client==null){
            throw new CustomException("Something went wrong","404");
        }
        if(!passwordEncoder.matches(request.getOldPassword(), client.getPassword())){
            throw  new CustomException("Invalid password.","401");
        }

        client.setPassword(passwordEncoder.encode(
                validation.validatePassword(request.getNewPassword(),
                        request.getConfirmPassword())));
                repositoryService.getClientRepository().save(client);


        return "Success";
    }


    private Boolean isValidToke(Date date){
        return date.after(new Date());
    }

    private VerificationToken getFreshToken(String clientId){
        Client client = repositoryService.getClientRepository().findById(clientId)
                .orElseThrow(() -> new CustomException("Client not found.","404"));
        VerificationToken vt = new VerificationToken();
        vt.setClient(client);
        return vt;
    }

    private  String getTokenAndFullUrlPath(String urlPath, String email){

        Client principal = repositoryService.getClientRepository().findByEmail(email);
            if(principal == null){
                throw  new CustomException("No account is associated with this email.","404");
            }
            if(!"active".equals(principal.getStatus())){
                throw  new CustomException("Account is not found","404");
            }


        VerificationToken existingToken = repositoryService.getVerificationTokenRepository()
                .findByClientId(principal.getId());

        VerificationToken freshToken = getFreshToken(principal.getId());

        if(existingToken != null){

            existingToken.setToken(freshToken.getToken());
            existingToken.setDate(new Date());
            repositoryService.getVerificationTokenRepository().save(existingToken);
            String url = urlPath+existingToken.getToken();
            return "Click on the link to verify your account: " +url;
        }
        repositoryService.getVerificationTokenRepository().save(freshToken);
        String url = urlPath+freshToken.getToken();
        return "Click on the link to verify your account: " +url;

    }

}
