package epicode.it.energyservices.entities.district.dto;

import epicode.it.energyservices.entities.district.District;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistrictMapper {

    public District toDistrict(ProvinceHttpResponse request) {
        District d = new District();
        d.setName(request.getNome());
        d.setRegion(request.getRegione());
        d.setCode(request.getCodice());
        return d;
    }


    public List<District> toDistrictList(List<ProvinceHttpResponse> request) {
        return request.stream().map(this::toDistrict).toList();
    }
}
