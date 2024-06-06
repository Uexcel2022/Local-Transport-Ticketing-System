package uexcel.com.ltts.dto;

import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class ChangeBusRouteDto {
    @NonNull
    private String busCode;
    @NonNull
    private String origin;
    @NonNull
    private String destination;

}
