package epicode.it.energyservices.entities.district;

import epicode.it.energyservices.entities.address.Address;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/districts")
public class DistrictController {

    private final DistrictSvc districtSvc;


//    @GetMapping("region/{region}")
//    public ResponseEntity<List<District>> getDistrictByRegion(@PathVariable String region){
//        return ResponseEntity.ok(districtSvc.findByRegion(region));
//    }

    @GetMapping
    public ResponseEntity<List<District>> getAllDistricts(){
        return ResponseEntity.ok(districtSvc.getDistricts());
    }
}
