package epicode.it.energyservices.entities.address;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepo extends JpaRepository<Address, Long> {
    List<Address> findAllByCustomerId(Long id);
}
