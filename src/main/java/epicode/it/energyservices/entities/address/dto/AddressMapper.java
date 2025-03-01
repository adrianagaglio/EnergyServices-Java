package epicode.it.energyservices.entities.address.dto;


import epicode.it.energyservices.entities.address.Address;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddressMapper {
    private ModelMapper modelMapper = new ModelMapper();

    public AddressResponse toAddressResponse(Address e) {
        AddressResponse addressResponse = modelMapper.map(e, AddressResponse.class);
        return addressResponse;
    }

    public List<AddressResponse> toAddressResponseList(List<Address> address) {
        return address.stream().map(this::toAddressResponse).toList();
    }

    public AddressResponseForCustomer toAddressResponseForCustomer(Address e) {
        AddressResponseForCustomer addressResponseForCustomer = modelMapper.map(e, AddressResponseForCustomer.class);
        return addressResponseForCustomer;
    }

    public List<AddressResponseForCustomer> toAddressResponseForCustomer(List<Address> address) {
        return address.stream().map(this::toAddressResponseForCustomer).toList();
    }

}
