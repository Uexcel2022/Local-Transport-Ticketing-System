package uexcel.com.ltts.event;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import uexcel.com.ltts.entity.Client;
@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private final Client client;
    private final String applicationUrl;

    public RegistrationCompleteEvent(Client client, String applicationUrl) {
        super(client);
        this.client = client;
        this.applicationUrl = applicationUrl;
    }
}
