package epicode.it.energyservices.entities.city;

import epicode.it.energyservices.entities.district.District;
import epicode.it.energyservices.entities.district.DistrictRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@AllArgsConstructor
@Validated
public class CitySvc {
    private final CityRepo cityRepo;

    // restituisco tutte le city
    public List<City> findAllCity() {
        return cityRepo.findAll();
    }

    // restituisco una city cercando per id
    public City findCityById(Long id) {
        return cityRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("The city you are looking for does not exist"));
    }

    public List<City> findAllByDistrictId(Long id) {
        return cityRepo.findAllByDistrictId(id);
    }

    public int count() {
        return (int) cityRepo.count();
    }
}
