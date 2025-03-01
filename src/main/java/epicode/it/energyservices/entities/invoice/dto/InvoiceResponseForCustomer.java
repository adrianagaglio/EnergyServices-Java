package epicode.it.energyservices.entities.invoice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InvoiceResponseForCustomer {
    private Long id;
    private LocalDate date;
    private double amount;
    private int number;
    private String status;
    private String notes;
}
