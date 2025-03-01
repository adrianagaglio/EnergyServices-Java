package epicode.it.energyservices.entities.invoice;

import epicode.it.energyservices.entities.invoice_status.InvoiceStatus;
import epicode.it.energyservices.entities.sys_user.customer.Customer;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name="invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private  Long id;

    private LocalDate date;

    private double amount;

    private String notes = "";

    @Column(nullable = false, unique = true)
    private int number = 1;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private InvoiceStatus status;

}