package uexcel.com.ltts.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String  ticketNumber = UUID.randomUUID().toString();
    private String  status;
    private LocalDate date;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Route route;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Client client;
}
