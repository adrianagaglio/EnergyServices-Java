package epicode.it.energyservices.entities.district;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DistrictRepo extends JpaRepository<District,Long> {
    Optional<District> findByName(String name);

    List<District> findByRegionIgnoreCase(String region);
}
