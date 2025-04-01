package epicode.it.energyservices.entities.address.dto;

import epicode.it.energyservices.entities.city.City;
import lombok.Data;

@Data
public class AddressResponseForCustomer {
    private Long id;
    private String street;
    private String addressNumber;
    private int cap;
    private String city;
    private String district;
    private String districtCode;

}
