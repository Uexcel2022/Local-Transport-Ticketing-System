package uexcel.com.ltts.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
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
    @JoinTable(name = "bus_route", joinColumns = @JoinColumn(name="bus_id"),
    inverseJoinColumns = @JoinColumn(name="route_id"))
    private Set<Route> route;

}
