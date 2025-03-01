package epicode.it.energyservices.auth;


import epicode.it.energyservices.auth.dto.LoginRequest;
import epicode.it.energyservices.auth.dto.RegisterRequest;
import epicode.it.energyservices.entities.sys_user.customer.dto.CustomerRequest;
import epicode.it.energyservices.entities.sys_user.customer.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(1)
public class AuthRunner implements ApplicationRunner {
    private final AppUserSvc appUserSvc;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if(appUserSvc.existByUsername("admin") || appUserSvc.existByUsername("user")) return;

        RegisterRequest admin = new RegisterRequest();
        admin.setName("Admin");
        admin.setSurname("Admin");
        admin.setUsername("admin");
        admin.setEmail("admin@admin.com");
        admin.setPassword("adminpwd");

        appUserSvc.registerAdmin(admin);

        RegisterRequest user = new RegisterRequest();
        user.setName("User");
        user.setSurname("User");
        user.setUsername("user");
        user.setEmail("user@user.com");
        user.setPassword("userpwd");
        appUserSvc.registerUser(user);

//        for (int i = 0; i < 1000; i++)
//        {
//            RegisterRequest customer = new RegisterRequest();
//            customer.setName("Customer + i");
//            customer.setSurname("Customer" + i);
//            customer.setUsername("customer" + i);
//            customer.setEmail("customer@customer.com" + i);
//            customer.setPassword("customerpwd");
//
//            CustomerRequest addInfoCustomer = new CustomerRequest();
//            addInfoCustomer.setType(Type.SPA);
//            addInfoCustomer.setPec("customer.pec@customer.com" + i);
//            addInfoCustomer.setPhone("1234567890" + i);
//            addInfoCustomer.setDenomination("EPICODE" + i);
//            addInfoCustomer.setVatCode("0987654321" + i);
//            addInfoCustomer.setContactPhone("1236805893" + i);
//            customer.setCustomer(addInfoCustomer);
//            appUserSvc.registerUser(customer);
//        }




    }
}