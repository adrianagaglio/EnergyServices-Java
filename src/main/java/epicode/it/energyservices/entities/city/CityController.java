package epicode.it.energyservices.entities.city;

import epicode.it.energyservices.entities.district.District;
import epicode.it.energyservices.entities.district.DistrictSvc;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cities")
public class CityController {
    private final CitySvc citySvcSvc;

    @GetMapping
    public ResponseEntity<List<City>> getAllCities(@RequestParam String district){
        return ResponseEntity.ok(citySvcSvc.getCitiesByDistrict(district));
    }
}
