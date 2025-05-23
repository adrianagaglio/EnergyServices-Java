package epicode.it.energyservices.entities.sys_user.customer.dto;

import epicode.it.energyservices.entities.address.Address;
import epicode.it.energyservices.entities.address.dto.AddressResponseForCustomer;
import epicode.it.energyservices.entities.invoice.dto.InvoiceResponse;
import epicode.it.energyservices.entities.sys_user.customer.Type;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CustomerResponseForInvoice {
    private String denomination;
    private String vatCode;
    private LocalDate creationDate;
    private LocalDate lastContact;
    private double yearlyTurnover;
    private String pec;
    private String phone;
    private String contactPhone;
    private Type type;
    private Map<String, AddressResponseForCustomer> addresses = new HashMap<>();

}
