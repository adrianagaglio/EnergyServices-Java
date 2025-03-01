package epicode.it.energyservices.entities.sys_user.customer;

import epicode.it.energyservices.entities.sys_user.customer.dto.CustomerRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Long> {

    Optional<Customer> findByAppUserUsername(String username);

    boolean existsByVatCode(String vatCode);

    boolean existsByDenomination(String denomination);

    boolean existsByPec(String pec);

    boolean existsByPhone(String phone);

    List<Customer> findAllByYearlyTurnoverBetween(double min, double max);

    List<Customer> findAllByCreationDateBetween(LocalDate startDate, LocalDate endDate);
    List<Customer> findAllByLastContactBetween(LocalDate startDate, LocalDate endDate);
    @Query("SELECT c FROM Customer c WHERE LOWER(c.denomination) LIKE CONCAT('%', :searchTerm, '%')")
    List<Customer> findAllByDenominationContaining(@Param("searchTerm") String searchTerm);

    @Query("SELECT c FROM Customer c WHERE c.appUser.username = :username")
    Optional<Customer> findByUsername(@Param("username") String username);

    Optional<Customer> findByVatCode(String vatCode);

}
