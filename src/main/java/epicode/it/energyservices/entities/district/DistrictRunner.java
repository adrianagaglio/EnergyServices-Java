package epicode.it.energyservices.entities.district;

import epicode.it.energyservices.entities.city.CityImportSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class DistrictRunner implements ApplicationRunner {
    @Autowired
    private DistrictImportSvc districtImportSvc;

    @Autowired
    private DistrictSvc districtSvc;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (districtSvc.count() == 0) {
            String filePath = getClass().getClassLoader().getResource("province-italiane.csv").getPath();
            districtImportSvc.importCsvDistrict(filePath);
        }

    }
}
