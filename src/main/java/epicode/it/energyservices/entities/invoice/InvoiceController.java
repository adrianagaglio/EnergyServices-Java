package epicode.it.energyservices.entities.invoice;

import epicode.it.energyservices.auth.Role;
import epicode.it.energyservices.entities.invoice.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invoice")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceSvc invoiceSvc;
    private final InvoiceResponseMapper mapper;

    @GetMapping
//    Accessibile solo ad ADMIN e USER
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<InvoiceResponse>> getAll() {

        return ResponseEntity.ok(invoiceSvc.getAll());
    }

    @GetMapping("/byCustomer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<InvoiceResponse>> getAllByCustomer(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("ciao");
        return ResponseEntity.ok(invoiceSvc.getAllByCustomer(userDetails.getUsername()));
    }

    @GetMapping("/paged")
//    Accessibile solo ad ADMIN e USER
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<InvoiceResponse>> getAllPaged(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(invoiceSvc.getAllPageable(pageable));
    }

    @GetMapping("/{id}")
//    Accessibile solo ad ADMIN/USER o CUSTOMER legato alla fattura
    @PreAuthorize("hasAnyRole('ADMIN', 'USER','CUSTOMER')")
    public ResponseEntity<?> getById(@PathVariable Long id, @AuthenticationPrincipal User userDetails) {
        Invoice invoice = invoiceSvc.getById(id);

        if (userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_CUSTOMER"))
        ) {
            if (!invoice.getCustomer().getAppUser().getUsername().equals(userDetails.getUsername())) {
                throw new InvalidParameterException("You are not the owner of this invoice");
            } else {
                return ResponseEntity.ok(mapper.toInvoiceResponseForCustomer(invoice));
            }

        }
        return ResponseEntity.ok(mapper.toInvoiceResponse(invoice));
    }

    @GetMapping("/by-number")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER','CUSTOMER')")
    public ResponseEntity<?> getByNumber(@RequestParam int number, @AuthenticationPrincipal User userDetails) {
        Invoice invoice = invoiceSvc.getByNumber(number);

        //Adriana dovresti vederlo
        if (userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_CUSTOMER"))
        ) {
            if (!invoice.getCustomer().getAppUser().getUsername().equals(userDetails.getUsername())) {
                throw new InvalidParameterException("You are not the owner of this invoice");
            } else {
                return ResponseEntity.ok(mapper.toInvoiceResponseForCustomer(invoice));
            }

        }
        return ResponseEntity.ok(mapper.toInvoiceResponse(invoice));
    }

    @PutMapping("/{number}")
//    Accessibile solo a USER
    @PreAuthorize("hasAnyRole('USER', 'CUSTOMER')")
    public ResponseEntity<?> updateStatus(@PathVariable int number, @RequestBody InvoiceUpdateRequest request, @AuthenticationPrincipal User userDetails) {
        Invoice invoice = invoiceSvc.updateStatus(number, request);

        if (userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_CUSTOMER"))
        ) {
            if (!invoice.getCustomer().getAppUser().getUsername().equals(userDetails.getUsername())) {
                throw new InvalidParameterException("You are not the owner of this invoice");
            } else {
                return ResponseEntity.ok(mapper.toInvoiceResponseForCustomer(invoice));
            }

        }

        return ResponseEntity.ok(mapper.toInvoiceResponse(invoice));
    }

    @PostMapping
//    Accessibile solo a USER
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<InvoiceResponse> create(@RequestBody InvoiceRequest request) {
        return new ResponseEntity<>(invoiceSvc.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/by-status")
//    Accessibile solo ad ADMIN/USER
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'CUSTOMER')")
    public ResponseEntity<?> getAllByStatus(@RequestParam String status, @RequestParam(required = false) String direction, @AuthenticationPrincipal User userDetails) {
        if (direction == null || direction.isEmpty()) direction = "ASC";

        List<Invoice> invoices = invoiceSvc.getAllByStatus(status, direction);

        if (userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_CUSTOMER"))
        ) {
            return ResponseEntity.ok(mapper.toInvoiceResponseForCustomerList(invoices.stream().filter(invoice -> invoice.getCustomer().getAppUser().getUsername().equals(userDetails.getUsername())).toList()));
        }
        return ResponseEntity.ok(mapper.toInvoiceResponseList(invoices));
    }

    @GetMapping("/by-customer")
    //    Accessibile solo ad ADMIN/USER
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<InvoiceResponse>> getAllByCustomer(@RequestParam(required = false) Long customerId,
                                                                  @RequestParam(required = false) String vatCode,
                                                                  @RequestParam(required = false) String pec,
                                                                  @RequestParam(required = false) String direction) {
        if (customerId == null && vatCode == null && pec == null) {
            throw new InvalidParameterException("Customer ID, VAT Code or PEC is required");
        }
        if (direction == null || direction.isEmpty()) direction = "ASC";
        return ResponseEntity.ok(invoiceSvc.getAllByCustomer(customerId, vatCode, pec, direction));
    }

    @GetMapping("/by-date")
    //    Accessibile solo ad ADMIN/USER
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'CUSTOMER')")
    public ResponseEntity<?> getAllByDate(@RequestParam LocalDate date, @AuthenticationPrincipal User userDetails) {
        List<Invoice> invoices = invoiceSvc.getAllByDate(date);

        if (userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_CUSTOMER"))
        ) {
            return ResponseEntity.ok(mapper.toInvoiceResponseForCustomerList(invoices.stream().filter(invoice -> invoice.getCustomer().getAppUser().getUsername().equals(userDetails.getUsername())).toList()));
        }

        return ResponseEntity.ok(mapper.toInvoiceResponseList(invoices));
    }

    @GetMapping("/by-year")
    //    Accessibile solo ad ADMIN/USER
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'CUSTOMER')")
    public ResponseEntity<?> getAllByYear(@RequestParam int year, @AuthenticationPrincipal User userDetails) {
        List<Invoice> invoices = invoiceSvc.getAllByYear(year);

        if (userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_CUSTOMER"))
        ) {
            return ResponseEntity.ok(mapper.toInvoiceResponseForCustomerList(invoices.stream().filter(invoice -> invoice.getCustomer().getAppUser().getUsername().equals(userDetails.getUsername())).toList()));
        }

        return ResponseEntity.ok(mapper.toInvoiceResponseList(invoices));
    }

    @GetMapping("/amount-range")
    //    Accessibile solo ad ADMIN/USER
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'CUSTOMER')")
    public ResponseEntity<?> getAllByAmountBetween(double min, double max, @AuthenticationPrincipal User userDetails) {
        List<Invoice> invoices = invoiceSvc.getAllByAmountBetween(min, max);

        if (userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_CUSTOMER"))
        ) {
            return ResponseEntity.ok(mapper.toInvoiceResponseForCustomerList(invoices.stream().filter(invoice -> invoice.getCustomer().getAppUser().getUsername().equals(userDetails.getUsername())).toList()));
        }

        return ResponseEntity.ok(mapper.toInvoiceResponseList(invoices));
    }

    @GetMapping("/total")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Map<String, Double>> getTotal(@RequestParam int year) {
        Map<String, Double> response = new HashMap<>();
        response.put("total", invoiceSvc.getTotal(year));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/waiting-payment")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<InvoiceResponse>> getWaitingPayment() {
        return ResponseEntity.ok(invoiceSvc.getAllWaitingPayment());
    }

    @GetMapping("/latest")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<InvoiceResponse>> getLatest(@RequestParam int limit) {
        return ResponseEntity.ok(invoiceSvc.getLatest(limit));
    }

    @GetMapping("/total-by-customer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Map<String, Double>> getTotalByCustomer(@RequestParam int year, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Map<String, Double> response = new HashMap<>();
        response.put("total", invoiceSvc.getTotalByCustomer(username, year));
        return ResponseEntity.ok(response);
    }
}
