package epicode.it.energyservices.entities.invoice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InvoiceUpdateRequest {
    @NotBlank(message = "Invoice status is required")
    private String status;

    private String notes;
}
