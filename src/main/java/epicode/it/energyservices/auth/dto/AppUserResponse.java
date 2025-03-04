package epicode.it.energyservices.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AppUserResponse {
    private long id;
    private String username;

    private String email;

    private String name;

    private String surname;

    private String avatar;

}
