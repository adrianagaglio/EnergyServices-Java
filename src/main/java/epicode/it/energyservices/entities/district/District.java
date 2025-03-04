package epicode.it.energyservices.entities.district;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "districts")
public class  District {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String code;
    private String region;

}