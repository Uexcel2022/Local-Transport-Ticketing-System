package uexcel.com.ltts.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
@Setter
@Getter
public class ClientActivityEvent extends ApplicationEvent {
    private final String appContext;
    private  final String uri;
    private final String email;

    public ClientActivityEvent(String appContext, String uri, String email) {
        super(appContext);
        this.appContext = appContext;
        this.uri = uri;
        this.email = email;
    }
}
