package uexcel.com.ltts.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String busName;
    private String busCode;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "busRoute", joinColumns = @JoinColumn(name="busId" ),
    inverseJoinColumns = @JoinColumn(name="routeId"))
    private List<Route> route;

}
