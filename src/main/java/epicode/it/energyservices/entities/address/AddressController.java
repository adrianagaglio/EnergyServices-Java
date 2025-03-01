package epicode.it.energyservices.entities.address;

import epicode.it.energyservices.entities.address.dto.AddressCreateRequest;
import epicode.it.energyservices.entities.address.dto.AddressMapper;
import epicode.it.energyservices.entities.address.dto.AddressResponse;
import epicode.it.energyservices.entities.address.dto.AddressResponseForCustomer;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController {
    private final AddressSvc addressService;
    private final AddressMapper mapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER') ")
    public ResponseEntity<List<AddressResponse>> getAllAddresses(){
        return ResponseEntity.ok(addressService.findAllAddress());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER','CUSTOMER')")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id,  @AuthenticationPrincipal UserDetails userDetails){
        addressService.deleteAddress(id, userDetails);
        return new ResponseEntity<>("address eliminato", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER') || hasRole('CUSTOMER')")
    public ResponseEntity<?> getAddressById(@PathVariable Long id,@AuthenticationPrincipal User userDetails) {
        Address address = addressService.findAddressById(id);
        if (userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_CUSTOMER"))
        ) {
            if (!address.getCustomer().getAppUser().getUsername().equals(userDetails.getUsername())){
                throw new InvalidParameterException("You are not the owner of this address");
            } else {
                return ResponseEntity.ok(mapper.toAddressResponseForCustomer(address));
            }

        }
        return ResponseEntity.ok(mapper.toAddressResponse(address));
    }

    @GetMapping("/byCustomer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<AddressResponseForCustomer>> getAllFromCustomer(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(addressService.getAllByCustomer(userDetails.getUsername()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER') || hasRole('CUSTOMER')")
    public ResponseEntity<AddressResponse> modifyAddress(@RequestBody AddressCreateRequest addressCreaRequest, @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(addressService.updateAddress(id,addressCreaRequest, userDetails));
    }

    // verificare se necessario (gestione indirizzi a partire dalla creazione utente)
/*    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody AddressCreateRequest addressCreateRequest, @AuthenticationPrincipal UserDetails userDetails) {
        Address savedAddress = addressService.saveAddress(addressCreateRequest, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAddress);
    }*/


}
