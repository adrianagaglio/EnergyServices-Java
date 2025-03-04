package epicode.it.energyservices.entities.invoice.dto;

import epicode.it.energyservices.entities.invoice.Invoice;
import epicode.it.energyservices.entities.sys_user.customer.dto.CustomerMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvoiceResponseMapper {
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private CustomerMapper customerMapper;

    public InvoiceResponse toInvoiceResponse(Invoice e) {
        InvoiceResponse invoiceResponse = modelMapper.map(e, InvoiceResponse.class);
        invoiceResponse.setCustomer(customerMapper.toCustomerResponseForInvoice(e.getCustomer()));
        invoiceResponse.setStatus(e.getStatus().getName());
        return invoiceResponse;
    }

    public List<InvoiceResponse> toInvoiceResponseList(List<Invoice> invoices) {
        return invoices.stream().map(this::toInvoiceResponse).toList();
    }

    public InvoiceResponseForCustomer toInvoiceResponseForCustomer(Invoice e) {
        InvoiceResponseForCustomer invoiceResponse = modelMapper.map(e, InvoiceResponseForCustomer.class);
        invoiceResponse.setStatus(e.getStatus().getName());
        return invoiceResponse;
    }

    public List<InvoiceResponseForCustomer> toInvoiceResponseForCustomerList(List<Invoice> invoices) {
        return invoices.stream().map(this::toInvoiceResponseForCustomer).toList();
    }
}
