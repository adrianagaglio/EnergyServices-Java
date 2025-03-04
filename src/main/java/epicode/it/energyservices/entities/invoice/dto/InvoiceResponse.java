package epicode.it.energyservices.entities.invoice.dto;

import epicode.it.energyservices.entities.sys_user.customer.dto.CustomerResponse;
import epicode.it.energyservices.entities.sys_user.customer.dto.CustomerResponseForInvoice;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InvoiceResponse {
    private Long id;
    private LocalDate date;
    private double amount;
    private int number;
    private String notes;
    private String status;
    private CustomerResponseForInvoice customer;
}
