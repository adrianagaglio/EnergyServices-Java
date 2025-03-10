package epicode.it.energyservices.entities.sys_user.customer;

import com.github.javafaker.Faker;
import epicode.it.energyservices.auth.AppUserSvc;
import epicode.it.energyservices.auth.dto.RegisterRequest;
import epicode.it.energyservices.entities.address.dto.AddressCreateRequest;
import epicode.it.energyservices.entities.city.City;
import epicode.it.energyservices.entities.city.CitySvc;
import epicode.it.energyservices.entities.district.District;
import epicode.it.energyservices.entities.district.DistrictSvc;
import epicode.it.energyservices.entities.sys_user.customer.dto.CustomerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(3)
public class CustomerRunner implements ApplicationRunner {
    private final Faker faker;
    private final AppUserSvc appUserSvc;
    private final CustomerSvc customerSvc;
    private final CitySvc citySvc;
    private final DistrictSvc districtSvc;

    @Override
    public void run(ApplicationArguments args) throws Exception {


        if(customerSvc.count() == 0) {
            for (int i = 0; i < 50; i++) {

                CustomerRequest customer = new CustomerRequest();
                customer.setDenomination(faker.company().name());
                customer.setVatCode(faker.number().digits(11));
                customer.setPec(faker.internet().emailAddress());
                customer.setPhone(faker.phoneNumber().cellPhone());
                customer.setContactPhone(faker.phoneNumber().cellPhone());
                int random = faker.random().nextInt(0, 3);
                customer.setType(Type.values()[random]);

                List<District> districts = districtSvc.getDistricts();
                District d = districts.get(faker.random().nextInt(districts.size()));

                List<City> cities = citySvc.getCitiesByDistrict(d.getName());
                City c = cities.get(faker.random().nextInt(cities.size()));

                AddressCreateRequest addressRequest = new AddressCreateRequest();
                addressRequest.setAddressNumber(String.valueOf(faker.number().numberBetween(1, 1000)));
                addressRequest.setCap(faker.number().numberBetween(10000, 99999));
                addressRequest.setStreet(faker.address().streetAddress());
                addressRequest.setCity(c.getName());
                addressRequest.setDistrict(c.getDistrictName());
                addressRequest.setDistrictCode(d.getCode());

                City c2 = cities.get(faker.random().nextInt(cities.size()));
                AddressCreateRequest addressRequest1 = new AddressCreateRequest();
                addressRequest1.setAddressNumber(String.valueOf(faker.number().numberBetween(1, 1000)));
                addressRequest1.setCap(faker.number().numberBetween(10000, 99999));
                addressRequest1.setStreet(faker.address().streetAddress());
                addressRequest1.setCity(c2.getName());
                addressRequest1.setDistrict(c2.getDistrictName());
                addressRequest1.setDistrictCode(d.getCode());
                customer.setOperationalHeadquartersAddress(addressRequest);
                customer.setRegisteredOfficeAddress(addressRequest1);

                RegisterRequest request = new RegisterRequest();
                request.setName(faker.name().firstName());
                request.setSurname(faker.name().lastName());

                String surname = request.getSurname().toLowerCase();
                if (surname.contains(" ") || surname.contains("'")) {
                    surname = surname.replace(" ", "");
                    surname = surname.replace("'", "");
                }

                request.setUsername(request.getName().toLowerCase().charAt(0) + surname);
                request.setEmail(request.getName().toLowerCase() + "." + surname + "@mail.com");
                request.setPassword("password");

                request.setCustomer(customer);

                try {
                    appUserSvc.registerUser(request);
                } catch (RuntimeException e) {
                    System.out.println("in errore " + request);
                    System.out.println(e.getMessage());
                }
            }
        }

    }
}
