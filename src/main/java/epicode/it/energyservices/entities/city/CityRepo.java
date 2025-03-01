package epicode.it.energyservices.entities.city;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepo extends JpaRepository <City,Long> {
    List<City> findAllByDistrictId(Long id);
}
