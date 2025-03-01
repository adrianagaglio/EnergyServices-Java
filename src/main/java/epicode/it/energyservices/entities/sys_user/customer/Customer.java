package epicode.it.energyservices.entities.sys_user.customer;

import epicode.it.energyservices.entities.address.Address;
import epicode.it.energyservices.entities.invoice.Invoice;
import epicode.it.energyservices.entities.sys_user.SysUser;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
public class Customer extends SysUser {
    private String denomination;

    @Column(name="vat_code")
    private String vatCode;

    @Column(name="creation_date")
    private LocalDate creationDate = LocalDate.now();

    @Column(name="last_contact")
    private LocalDate lastContact;

    @Column(name="yearly_turnover")
    private double yearlyTurnover;

    private String pec;

    private String phone;

    @Column(name="contact_phone")
    private String contactPhone;


    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<String, Address> addresses = new HashMap<>();

    @OneToMany
    private List<Invoice> invoices = new ArrayList<>();



}