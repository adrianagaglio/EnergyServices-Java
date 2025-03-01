package epicode.it.energyservices.entities.invoice_status;

import epicode.it.energyservices.entities.invoice_status.dto.InvoiceStatusRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceStatusSvc {
    private final InvoiceStatusRepo invoiceStatusRepo;

    public List<InvoiceStatus> getAll() {
        return invoiceStatusRepo.findAll();
    }

    public Page<InvoiceStatus> getAllPageable(Pageable pageable) {
        return invoiceStatusRepo.findAll(pageable);
    }

    public InvoiceStatus getById(Long id) {
        return invoiceStatusRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Invoice status not found"));
    }

    public int count() {
        return (int) invoiceStatusRepo.count();
    }

    public String delete(Long id) {
        InvoiceStatus e = getById(id);
        invoiceStatusRepo.delete(e);
        return "Invoice status deleted successfully";
    }

    public String delete(InvoiceStatus e) {
        InvoiceStatus foundInvoiceStatus = getById(e.getId());
        invoiceStatusRepo.delete(foundInvoiceStatus);
        return "Invoice status deleted successfully";
    }

    public InvoiceStatus findByName(String name) {
        return invoiceStatusRepo.findByNameOrderByNameAsc(name.toUpperCase()).orElseThrow(() -> new EntityNotFoundException("Status " + name + " not exists"));
    }

    public InvoiceStatus create(@Valid InvoiceStatusRequest request) {
        InvoiceStatus status = new InvoiceStatus();
        status.setName(request.getName());
        status.setDescription(request.getDescription());
        return invoiceStatusRepo.save(status);
    }
}
