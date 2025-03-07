package epicode.it.energyservices.entities.city;

import epicode.it.energyservices.entities.district.District;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class City {
    private String name;
    private String districtName;
    private String cap;
}