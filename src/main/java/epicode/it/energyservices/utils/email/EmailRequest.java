package epicode.it.energyservices.utils.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmailRequest {

    @Email(message = "Invalid email address for REPLY TO field")
    private String replyTo;

    @Email(message = "Invalid email address for TO field")
    private String to;

    @Email(message = "Invalid email address for CC field")
    private String cc;

    @NotBlank(message = "SUBJECT is required")
    private String subject;

    @NotBlank(message = "BODY is required")
    private String body;

}
