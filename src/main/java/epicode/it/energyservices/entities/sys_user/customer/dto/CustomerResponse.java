package epicode.it.energyservices.entities.sys_user.customer.dto;

import epicode.it.energyservices.entities.address.Address;
import epicode.it.energyservices.entities.address.dto.AddressResponseForCustomer;
import epicode.it.energyservices.entities.invoice.Invoice;
import epicode.it.energyservices.entities.invoice.dto.InvoiceResponse;
import epicode.it.energyservices.entities.invoice.dto.InvoiceResponseForCustomer;
import epicode.it.energyservices.entities.sys_user.customer.Type;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CustomerResponse {
    private Long id;
    private String denomination;
    private String vatCode;
    private LocalDate creationDate;
    private LocalDate lastContact;
    private double yearlyTurnover;
    private String pec;
    private String phone;
    private String contactPhone;
    private Type type;
    private Map<String, AddressResponseForCustomer> addresses = new HashMap<>(); // collegare a entità indirizzo
    private List<InvoiceResponseForCustomer> invoices = new ArrayList<>();
    private String image;

}
