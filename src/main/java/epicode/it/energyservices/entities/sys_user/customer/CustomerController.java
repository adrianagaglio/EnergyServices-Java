package epicode.it.energyservices.entities.sys_user.customer;

import epicode.it.energyservices.entities.sys_user.customer.dto.CustomerMapper;
import epicode.it.energyservices.entities.sys_user.customer.dto.CustomerResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    private CustomerSvc customerSvc;

    @Autowired
    private CustomerMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<CustomerResponse>> getAll() {
        return ResponseEntity.ok(mapper.toCustomerResponseList(customerSvc.getAll()));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<CustomerResponse>> getAll(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(customerSvc.getAllPageable(pageable));
    }

    @GetMapping("/byYearlyTurnoverBetween")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    //    Accessibile solo ad ADMIN/USER
    public ResponseEntity<List<CustomerResponse>> getByYearlyTurnoverBetween(@Param("min") double min,@Param("max") double max) {
        return ResponseEntity.ok(customerSvc.getByYearlyTurnoverBetween(min, max));
    }

    @GetMapping("/byDenominationContaining")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    //    Accessibile solo ad ADMIN/USER
    public ResponseEntity<List<CustomerResponse>> getByDenominationContaining(@Param("searchTerm") String searchTerm) {
        return ResponseEntity.ok(customerSvc.getByDenominationContaining(searchTerm.toLowerCase()));
    }

    @GetMapping("/byCreationDateBetween")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    //    Accessibile solo ad ADMIN/USER
    public ResponseEntity<List<CustomerResponse>> getByCreationDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate) {
        return ResponseEntity.ok(customerSvc.getByCreationDateBetween(startDate, endDate));
    }

    @GetMapping("/byLastContactBetween")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    //    Accessibile solo ad ADMIN/USER
    public ResponseEntity<List<CustomerResponse>> getByLastContactBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate) {
        return ResponseEntity.ok(customerSvc.getByLastContactBetween(startDate, endDate));
    }

    @GetMapping("/by-username")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CustomerResponse> getByUsername(@RequestParam String username) {
        return ResponseEntity.ok(mapper.toCustomerResponse(customerSvc.getByUsername(username)));
    }
    @GetMapping("/by-customer-username")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerResponse> getByCustomerUsername(@AuthenticationPrincipal User userDetails) {
        return ResponseEntity.ok(mapper.toCustomerResponse(customerSvc.getByUsername(userDetails.getUsername())));
    }


    @GetMapping("/by-vatCode")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CustomerResponse> getByVatCode(@RequestParam String vatCode) {
        return ResponseEntity.ok(customerSvc.findByVatCode(vatCode));
    }

    @GetMapping("/total")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Map<String, Integer>> count() {
        Map<String, Integer> response = new HashMap<>();
        response.put("customers", customerSvc.count());
        return ResponseEntity.ok(response);
    }
}
