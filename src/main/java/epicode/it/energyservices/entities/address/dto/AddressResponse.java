package epicode.it.energyservices.entities.address.dto;

import epicode.it.energyservices.entities.city.City;
import epicode.it.energyservices.entities.sys_user.customer.dto.CustomerResponse;
import epicode.it.energyservices.entities.sys_user.customer.dto.CustomerResponseForAddress;
import lombok.Data;

@Data
public class AddressResponse {
    private Long id;
    private String street;
    private String addressNumber;
    private int cap;
    private String city;
    private CustomerResponseForAddress customer;
}
