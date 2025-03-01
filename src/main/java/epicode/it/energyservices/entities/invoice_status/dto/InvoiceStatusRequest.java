package epicode.it.energyservices.entities.invoice_status.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InvoiceStatusRequest {

    @NotBlank(message = "Status name is required")
    private String name;

    @NotBlank(message = "Status description is required")
    private String description;
}
