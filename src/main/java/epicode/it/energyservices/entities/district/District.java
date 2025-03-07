package epicode.it.energyservices.entities.district;

import jakarta.persistence.*;
import lombok.Data;


@Data
public class  District {
    private String name;
    private String code;
    private String region;

}