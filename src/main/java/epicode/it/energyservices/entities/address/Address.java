package epicode.it.energyservices.entities.address;

import epicode.it.energyservices.entities.city.City;
import epicode.it.energyservices.entities.sys_user.customer.Customer;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String street;
    private String addressNumber;
    private int cap;
    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;


}