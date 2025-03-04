package epicode.it.energyservices.entities.sys_user.customer.dto;


import epicode.it.energyservices.entities.invoice.Invoice;
import epicode.it.energyservices.entities.invoice.InvoiceRepo;
import epicode.it.energyservices.entities.invoice.dto.InvoiceResponseForCustomer;
import epicode.it.energyservices.entities.sys_user.customer.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerMapper {
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private InvoiceRepo invoiceRepo;

    public CustomerResponse toCustomerResponse(Customer e) {
        CustomerResponse customerResponse = modelMapper.map(e, CustomerResponse.class);
        customerResponse.setId(e.getId());
        List<Invoice> invoices = invoiceRepo.findAllByVatCode(e.getVatCode());
        List<InvoiceResponseForCustomer> invoiceResponses = new ArrayList<>();
        if (invoices.size() > 0) {
            for (int i = 0; i < invoices.size(); i++) {
                Invoice invoice = invoices.get(i);
                InvoiceResponseForCustomer invoiceResponse = modelMapper.map(invoice, InvoiceResponseForCustomer.class);
                invoiceResponse.setStatus(invoice.getStatus().getName());
                invoiceResponses.add(invoiceResponse);
            }
            customerResponse.setInvoices(invoiceResponses);
        }
        customerResponse.setImage(e.getAppUser().getAvatar());
        return customerResponse;
    }

    public List<CustomerResponse> toCustomerResponseList(List<Customer> customers) {
        return customers.stream().map(this::toCustomerResponse).toList();
    }

    public CustomerResponseForInvoice toCustomerResponseForInvoice(Customer e) {
        CustomerResponseForInvoice customerResponseForInvoice = modelMapper.map(e, CustomerResponseForInvoice.class);
        return customerResponseForInvoice;
    }

}
