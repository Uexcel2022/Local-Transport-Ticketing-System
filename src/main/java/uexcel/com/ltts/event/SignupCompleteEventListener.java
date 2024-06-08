package uexcel.com.ltts.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import uexcel.com.ltts.entity.Client;
import uexcel.com.ltts.entity.VerificationToken;
import uexcel.com.ltts.util.RepositoryService;

@Component
public class SignupCompleteEventListener implements ApplicationListener<SignupCompleteEvent> {
    private static final Logger log = LoggerFactory.getLogger(SignupCompleteEventListener.class);
    private final RepositoryService repositoryService;

    public SignupCompleteEventListener(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Override
    public void onApplicationEvent(SignupCompleteEvent event) {
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
