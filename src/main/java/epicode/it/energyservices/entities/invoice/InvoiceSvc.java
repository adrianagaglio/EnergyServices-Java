package epicode.it.energyservices.entities.invoice;

import epicode.it.energyservices.entities.invoice.dto.*;
import epicode.it.energyservices.entities.invoice_status.InvoiceStatus;
import epicode.it.energyservices.entities.invoice_status.InvoiceStatusSvc;
import epicode.it.energyservices.entities.sys_user.customer.Customer;
import epicode.it.energyservices.entities.sys_user.customer.CustomerSvc;
import epicode.it.energyservices.entities.sys_user.customer.dto.CustomerRequest;
import epicode.it.energyservices.entities.sys_user.customer.dto.CustomerResponse;
import epicode.it.energyservices.utils.email.EmailMapper;
import epicode.it.energyservices.utils.email.EmailSvc;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class InvoiceSvc {
    private final InvoiceRepo invoiceRepo;
    private final InvoiceStatusSvc invoiceStatusSvc;
    private final CustomerSvc customerSvc;
    private final InvoiceResponseMapper mapper;
    private final EmailMapper emailMapper;
    private final EmailSvc emailSvc;

    public List<InvoiceResponse> getAll() {
        return mapper.toInvoiceResponseList(invoiceRepo.findAll());
    }

    public List<InvoiceResponseForCustomer> getAllByCustomer(String username) {
        Long customerId = customerSvc.getByUsername(username).getId();
        return mapper.toInvoiceResponseForCustomerList(invoiceRepo.findAllByCustomerIdOrderByNumberDesc(customerId));
    }

    public Page<InvoiceResponse> getAllPageable(Pageable pageable) {
        Page<Invoice> pagedInvoices = invoiceRepo.findAll(pageable);
        Page<InvoiceResponse> response = pagedInvoices.map(e -> {
            InvoiceResponse invoiceResponse = mapper.toInvoiceResponse(e);
            return invoiceResponse;
        });
        return response;
    }

    public Invoice getById(Long id) {
        return invoiceRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Invoice not found"));
    }

    public int count() {
        return (int) invoiceRepo.count();
    }

    @Transactional
    public InvoiceResponse create(@Valid InvoiceRequest request) {
        Invoice i = new Invoice();
        BeanUtils.copyProperties(request, i);
        int nextNumber = invoiceRepo.findMaxNumber().orElse(0) + 1;
        i.setNumber(nextNumber);
        InvoiceStatus status = request.getStatus() != null ? invoiceStatusSvc.findByName(request.getStatus()) : invoiceStatusSvc.findByName("DRAFT");
        Customer c = customerSvc.getById(request.getCustomerId());
        i.setCustomer(c);
        i.setStatus(status);
        InvoiceResponse response = mapper.toInvoiceResponse(invoiceRepo.save(i));
        int currentYear = LocalDate.now().getYear();
        c.setYearlyTurnover(getTotalByCustomerIdAndYear(request.getCustomerId(), currentYear));
        if (!i.getStatus().getName().equals("DRAFT"))
            emailSvc.sendEmailHtml(emailMapper.fromInvoicetoEmailRequest("New invoice", i));
        return response;
    }

    public Invoice getByNumber(int number) {
        return invoiceRepo.findFirstByNumber(number).orElseThrow(() -> new EntityNotFoundException("Invoice not found"));
    }

    public Invoice updateStatus(int number, @Valid InvoiceUpdateRequest request) {
        Invoice i = getByNumber(number);
        InvoiceStatus newStatus = invoiceStatusSvc.findByName(request.getStatus().toUpperCase());
        i.setStatus(newStatus);
        i.setNotes(request.getNotes());
        Invoice response = invoiceRepo.save(i);
        emailSvc.sendEmailHtml(emailMapper.fromInvoicetoEmailRequest("Invoice status changed", i));
        return response;
    }

    public List<Invoice> getAllByStatus(String status, String direction) {
        InvoiceStatus invoiceStatus = invoiceStatusSvc.findByName(status.toUpperCase());
        if (direction.equals("ASC")) {
            return invoiceRepo.findAllByStatusOrderByDateAsc(invoiceStatus.getId());
        } else {
            return invoiceRepo.findAllByStatusOrderByDateDesc(invoiceStatus.getId());
        }
    }

    public List<InvoiceResponse> getAllByCustomer(Long customerId, String vatCode, String pec, String direction) {
        if (direction.equals("ASC")) {
            return mapper.toInvoiceResponseList(invoiceRepo.findAllByCustomerOrderByDateAsc(customerId, vatCode, pec));
        } else {
            return mapper.toInvoiceResponseList(invoiceRepo.findAllByCustomerOrderByDateDesc(customerId, vatCode, pec));
        }
    }

    public List<Invoice> getAllByDate(LocalDate date) {
        return invoiceRepo.findAllByDate(date);
    }

    public List<Invoice> getAllByYear(int year) {
        return invoiceRepo.findAllByYear(year);
    }

    public List<Invoice> getAllByAmountBetween(double min, double max) {
        return invoiceRepo.findAllByAmountBetweenOrderByAmountAsc(min, max);
    }

    public double getTotalByCustomerIdAndYear(Long id, int year) {
        return invoiceRepo.findTotalAllByCustomerAndYear(id, year).orElse(0.0);
    }

    public double getTotal(int year) {
        return invoiceRepo.getTotal(year).orElse(0.0);
    }

    public double getTotalByCustomer (String username, int year) {
        Customer c = customerSvc.getByUsername(username);
        return invoiceRepo.getTotalByCustomer(c.getId(), year).orElse(0.0);
    }

    public List<InvoiceResponse> getAllWaitingPayment() {
        return mapper.toInvoiceResponseList(invoiceRepo.findAllWaitingPayment());
    }

    public Page<InvoiceResponse> getLatest(int size) {
        Pageable pageable = PageRequest.of(0, size); // Pagina 0 con 5 elementi
        int currentYear = LocalDate.now().getYear();
        Page<Invoice> pagedInvoices = invoiceRepo.findLatest(currentYear, pageable);
        Page<InvoiceResponse> response = pagedInvoices.map(e -> {
            InvoiceResponse invoiceResponse = mapper.toInvoiceResponse(e);
            return invoiceResponse;
        });
        return response;
    }
}
