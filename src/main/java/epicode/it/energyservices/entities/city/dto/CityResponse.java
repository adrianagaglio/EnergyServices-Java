package epicode.it.energyservices.entities.city.dto;

import lombok.Data;

@Data
public class CityResponse {
    private Long id;
    private String name;
    private String districtName;
}
