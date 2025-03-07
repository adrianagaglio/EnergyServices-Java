package epicode.it.energyservices.entities.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressCreateRequest {
    @NotBlank (message = "street can't be empty")
    private String street;
    @NotBlank (message = "address can't be empty")
    private String addressNumber;
    @NotNull (message = "cap can't be empty")
    private int cap;
    @NotNull (message = "idCity can't be empty")
    private String city;
}
