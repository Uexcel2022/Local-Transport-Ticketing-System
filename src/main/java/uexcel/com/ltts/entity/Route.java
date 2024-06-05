package uexcel.com.ltts.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String origin;
    private String destination;
    private double price;

    @ManyToMany(mappedBy = "route")
    private Set<Bus> bus;
}
