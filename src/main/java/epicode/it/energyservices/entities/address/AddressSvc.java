package epicode.it.energyservices.entities.address;

import epicode.it.energyservices.entities.address.dto.AddressCreateRequest;
import epicode.it.energyservices.entities.address.dto.AddressMapper;
import epicode.it.energyservices.entities.address.dto.AddressResponse;
import epicode.it.energyservices.entities.address.dto.AddressResponseForCustomer;
import epicode.it.energyservices.entities.city.City;
import epicode.it.energyservices.entities.city.CityRepo;
import epicode.it.energyservices.entities.invoice.dto.InvoiceResponseForCustomer;
import epicode.it.energyservices.entities.sys_user.customer.CustomerSvc;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.security.InvalidParameterException;
import java.util.List;

@Service
@AllArgsConstructor
@Validated
public class AddressSvc {
    private final AddressRepo addressRepo;
    private final CityRepo cityRepo;
    private final AddressMapper mapper;
    private final CustomerSvc customerSvc;

    // restituisco tutti gli adresses
    public List<AddressResponse> findAllAddress() {
        List<Address> address = addressRepo.findAll();

        return mapper.toAddressResponseList(address);
    }

    // restituisco un address cercando per id
    public Address findAddressById(Long id) {
        return addressRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("The district you are looking for does not exist"));
    }

    public List<AddressResponseForCustomer> getAllByCustomer(String username) {
        Long customerId = customerSvc.getByUsername(username).getId();
        return mapper.toAddressResponseForCustomer(addressRepo.findAllByCustomerId(customerId));
    }

    // cancella un address per id
    public void deleteAddress(Long id,  @AuthenticationPrincipal UserDetails userDetail) {
        if (!addressRepo.existsById(id)) {
            throw new EntityNotFoundException("The address indicated does not exist");
        }
        Address address = addressRepo.findById(id).get();

        if (userDetail.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_CUSTOMER")) && !address.getCustomer().getAppUser().getUsername().equals(userDetail.getUsername())){
            throw new InvalidParameterException("You are not the owner of this address");
        }
        addressRepo.deleteById(id);
    }

    // salva un nuovo Address
    public Address saveAddress(Address request) {
/*        City city = cityRepo.findById(request.getIdCity())
                .orElseThrow(() -> new EntityNotFoundException("The city you are looking for does not exist"));

        Address newAddress = new Address();
        newAddress.setStreet(request.getStreet());
        newAddress.setAddressNumber(request.getAddressNumber());
        newAddress.setLocation(request.getLocation());
        newAddress.setCap(request.getCap());
        newAddress.setCity(city);*/
        return addressRepo.save(request);
    }

    // modifica un address esistente
    public AddressResponse updateAddress(Long id, AddressCreateRequest updatedRequest, @AuthenticationPrincipal UserDetails userDetails ) {

        Address existingAddress = addressRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("The address you are looking for does not exist"));

        City city = cityRepo.findById(updatedRequest.getIdCity())
                .orElseThrow(() -> new EntityNotFoundException("The city you are looking for does not exist"));

        if (userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_CUSTOMER")) && !existingAddress.getCustomer().getAppUser().getUsername().equals(userDetails.getUsername())){
            throw new InvalidParameterException("You are not the owner of this address");
        }

        existingAddress.setStreet(updatedRequest.getStreet());
        existingAddress.setAddressNumber(updatedRequest.getAddressNumber());
        existingAddress.setCap(updatedRequest.getCap());
        existingAddress.setCity(city);

        return mapper.toAddressResponse(addressRepo.save(existingAddress)) ;
    }


}
