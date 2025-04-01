package epicode.it.energyservices.entities.city.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import epicode.it.energyservices.entities.district.dto.ProvinceHttpResponse;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityHttpResponse {
    String codice;
    String nome;
    ProvinceHttpResponse provincia;
    String cap;
}
