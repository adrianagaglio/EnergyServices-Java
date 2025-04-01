package epicode.it.energyservices.entities.city.dto;

import epicode.it.energyservices.entities.city.City;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CityMapper {

    public City toCity(CityHttpResponse request) {
        City c = new City();
        c.setName(request.getNome());
        c.setDistrictName(request.getProvincia().getNome());
        c.setCap(request.getCap());
        return c;
    }

    public List<City> toCityList(List<CityHttpResponse> request) {
        return request.stream().map(this::toCity).toList();
    }
}
