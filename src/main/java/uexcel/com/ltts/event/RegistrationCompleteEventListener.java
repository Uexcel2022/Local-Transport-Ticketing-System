package uexcel.com.ltts.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import uexcel.com.ltts.entity.Client;
import uexcel.com.ltts.entity.VerificationToken;
import uexcel.com.ltts.service.RepositoryService;

@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private static final Logger log = LoggerFactory.getLogger(RegistrationCompleteEventListener.class);
    private final RepositoryService repositoryService;

    public RegistrationCompleteEventListener(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        Client client = event.getClient();

        //creating token
        VerificationToken vt = new VerificationToken();
        vt.setClient(client);
        repositoryService.getVerificationTokenRepository().save(vt);
        String token = vt.getToken();

        //Send email
        String url = event.getApplicationUrl()+"/verify-token-email?token="+ token;

        //sending email

        log.info("Click on the link to verify your account: {}",url);

    }
}
