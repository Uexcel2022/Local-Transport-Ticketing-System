package uexcel.com.ltts.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
;

@Entity
@Data
public class Checkin {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private LocalDate date;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Bus bus;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(unique = true)
    private Booking booking;
}
