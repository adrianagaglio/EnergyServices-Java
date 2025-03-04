package epicode.it.energyservices.entities.sys_user.customer;

import com.github.javafaker.Faker;
import epicode.it.energyservices.entities.address.Address;
import epicode.it.energyservices.entities.address.AddressSvc;
import epicode.it.energyservices.entities.address.dto.AddressCreateRequest;
import epicode.it.energyservices.entities.city.City;
import epicode.it.energyservices.entities.city.CitySvc;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AddressesRunner implements ApplicationRunner {
    private final CustomerSvc customerSvc;
    private final CitySvc citySvc;
    private final Faker faker;
    private final AddressSvc addressSvc;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//
//        List<Customer> customers = customerSvc.getAll();
//        for (Customer customer : customers) {
//            City city = citySvc.findCityById((long) faker.number().numberBetween(1, 1000));
//            Address addressRequest = new Address();
//            addressRequest.setAddressNumber(String.valueOf(faker.number().numberBetween(1, 1000)));
//            addressRequest.setCap(faker.number().numberBetween(10000, 99999));
//            addressRequest.setStreet(faker.address().streetAddress());
//            addressRequest.setCity(city);
//            City city1 = citySvc.findCityById((long) faker.number().numberBetween(1, 1001));
//            Address addressRequest1 = new Address();
//            addressRequest1.setAddressNumber(String.valueOf(faker.number().numberBetween(1, 1000)));
//            addressRequest1.setCap(faker.number().numberBetween(10000, 99999));
//            addressRequest1.setStreet(faker.address().streetAddress());
//            addressRequest1.setCity(city1);
//            customer.getAddresses().put("HeadquartersAddress", addressRequest );
//            customer.getAddresses().put("OperationalHeadquartersAddress", addressRequest1 );
//        }
//
    }
}
