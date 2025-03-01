package epicode.it.energyservices.entities.invoice.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InvoiceRequest {

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Amount is required")
    @Min(message = "Amount must be greater than 1", value = 1)
    private double amount;

    private String status;

    @NotNull(message = "Customer ID is required")
    private Long customerId;
}
