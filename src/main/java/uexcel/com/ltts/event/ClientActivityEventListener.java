package uexcel.com.ltts.event;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import uexcel.com.ltts.entity.Client;
import uexcel.com.ltts.entity.VerificationToken;
import uexcel.com.ltts.exception.CustomException;
import uexcel.com.ltts.service.RepositoryService;

import java.util.Date;
@Component
public class ClientActivityEventListener implements ApplicationListener<ClientActivityEvent> {
    private static final Logger log = LoggerFactory.getLogger(ClientActivityEventListener.class);
    private final RepositoryService repositoryService;

    public ClientActivityEventListener(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ClientActivityEvent event) {
         String respUrl = event.getAppContext();

        if(event.getUri().equals("/api/v1/fresh-token-chg-email")){
            respUrl = event.getAppContext()+"/chg-email?token=";
        }

        if(event.getUri().equals("/api/v1/fresh-token-chg-pwd")){
            respUrl = event.getAppContext()+"/chg-pwd?token=";
        }

        if(event.getUri().equals("/api/v1/fresh-token-vfy-email")){
            respUrl = event.getAppContext()+"/verify-email?token=";
        }


        Client client = repositoryService.getClientRepository().existClientByEmail(event.getEmail());
        if(client == null){
            throw  new CustomException("No account is associated with this email.","404");
        }
        if(!"active".equals(client.getStatus())){
            throw  new CustomException("Account is not found","404");
        }


        VerificationToken existingToken = repositoryService.getVerificationTokenRepository()
                .findByClientId(client.getId());

        VerificationToken freshToken = getFreshToken(client.getId());

        if(existingToken != null){
            existingToken.setToken(freshToken.getToken());
            existingToken.setDate(new Date());
            repositoryService.getVerificationTokenRepository().save(existingToken);
            respUrl += existingToken.getToken();
        } else {
            repositoryService.getVerificationTokenRepository().save(freshToken);
            respUrl += freshToken.getToken();

        }
         //send email
        log.info("Click on the link to verify your account: {}", respUrl);


    }

    private VerificationToken getFreshToken(String clientId){
        Client client = repositoryService.getClientRepository().findById(clientId)
                .orElseThrow(() -> new CustomException("Client not found.","404"));
        VerificationToken vt = new VerificationToken();
        vt.setClient(client);
        return vt;
    }
}
