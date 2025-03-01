package epicode.it.energyservices.entities.sys_user.customer;

import epicode.it.energyservices.auth.AppUser;
import epicode.it.energyservices.entities.address.Address;
import epicode.it.energyservices.entities.address.dto.AddressCreateRequest;
import epicode.it.energyservices.entities.city.CitySvc;
import epicode.it.energyservices.entities.sys_user.customer.dto.CustomerMapper;
import epicode.it.energyservices.entities.sys_user.customer.dto.CustomerRequest;
import epicode.it.energyservices.entities.sys_user.customer.dto.CustomerResponse;
import epicode.it.energyservices.entities.sys_user.customer.dto.CustomerResponseForInvoice;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class CustomerSvc {
    private final CustomerRepo customerRepo;
    private final CustomerMapper mapper;
    private final CitySvc citySvc;

    public List<Customer> getAll() {

        return customerRepo.findAll();
    }

    public Customer getByUsername(String username) {
        return customerRepo.findByAppUserUsername(username).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }

    public Page<CustomerResponse> getAllPageable(Pageable pageable) {
        Page<Customer> pagedCustomer = customerRepo.findAll(pageable);
        Page<CustomerResponse> response = pagedCustomer.map(e -> {
            CustomerResponse customerResponse = mapper.toCustomerResponse(e);
            return customerResponse;
        });
        return response;

    }
    public Customer update(Customer customer) {
        return customerRepo.save(customer);
    }

    public List<CustomerResponse> getByYearlyTurnoverBetween(double min, double max) {
        List<Customer> customers = customerRepo.findAllByYearlyTurnoverBetween(min, max);
        return mapper.toCustomerResponseList(customers);
    }
    public List<CustomerResponse> getByCreationDateBetween(LocalDate startDate, LocalDate endDate) {
        List<Customer> customers = customerRepo.findAllByCreationDateBetween(startDate, endDate);
        return mapper.toCustomerResponseList(customers);
    }

    public List<CustomerResponse> getByLastContactBetween(LocalDate startDate, LocalDate endDate) {
        List<Customer> customers = customerRepo.findAllByLastContactBetween(startDate, endDate);
        return mapper.toCustomerResponseList(customers);
    }
    public List<CustomerResponse> getByDenominationContaining(String searchTerm) {
        List<Customer> customers = customerRepo.findAllByDenominationContaining(searchTerm);
        return mapper.toCustomerResponseList(customers);
    }


    public Customer getById(Long id) {
        return customerRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }

    public int count() {

        return (int) customerRepo.count();
    }

    public String delete(Long id) {
        Customer e = getById(id);
        customerRepo.delete(e);
        return "Customer deleted successfully";
    }

    public CustomerResponse findByVatCode(String vatCode) {
        return mapper.toCustomerResponse(customerRepo.findByVatCode(vatCode).orElseThrow(() -> new EntityNotFoundException("Customer not found")));
    }

    @Transactional
    public Customer create(AppUser appUser, @Valid CustomerRequest request) {
        if (customerRepo.existsByVatCode(request.getVatCode()))
            throw new EntityExistsException("Customer vatCode already exists");
        if (customerRepo.existsByDenomination(request.getDenomination()))
            throw new EntityExistsException("Customer denomination already exists");
        if (customerRepo.existsByPec(request.getPec()))
            throw new EntityExistsException("Customer pec already exists");
        if (customerRepo.existsByPhone(request.getPhone()))
            throw new EntityExistsException("Customer phone already exists");

        Customer customer = new Customer();
        BeanUtils.copyProperties(request, customer);
        customer.setType(request.getType());
        customer.setAppUser(appUser);

        HashMap<String, Address> addressMap = new HashMap<>();

        if (request.getOperationalHeadquartersAddress() != null) {
            AddressCreateRequest operationalAddress = request.getOperationalHeadquartersAddress();
            Address a = new Address();
            a.setCap(operationalAddress.getCap());
            a.setCity(citySvc.findCityById(operationalAddress.getIdCity()));
            a.setStreet(operationalAddress.getStreet());
            a.setAddressNumber(operationalAddress.getAddressNumber());
            a.setCustomer(customer);
            addressMap.put("OperationalHeadquartersAddress", a);
        }

        if (request.getRegisteredOfficeAddress() != null) {
            AddressCreateRequest registeredAddress = request.getRegisteredOfficeAddress();
            Address a = new Address();
            a.setCap(registeredAddress.getCap());
            a.setCity(citySvc.findCityById(registeredAddress.getIdCity()));
            a.setStreet(registeredAddress.getStreet());
            a.setAddressNumber(registeredAddress.getAddressNumber());
            a.setCustomer(customer);
            addressMap.put("RegisteredOfficeAddress", a);
        }

        customer.setAddresses(addressMap);

        return customerRepo.save(customer);
    }


}
