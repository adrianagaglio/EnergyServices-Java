package epicode.it.energyservices.entities.invoice;

import epicode.it.energyservices.entities.invoice_status.InvoiceStatus;
import epicode.it.energyservices.entities.sys_user.customer.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepo extends JpaRepository<Invoice, Long> {

    @Query("SELECT i FROM Invoice i WHERE i.status.id = :statusId ORDER BY i.date ASC")
    public List<Invoice> findAllByStatusOrderByDateAsc(@Param("statusId") Long statusId);

    @Query("SELECT i FROM Invoice i WHERE i.status.id = :statusId ORDER BY i.date DESC")
    public List<Invoice> findAllByStatusOrderByDateDesc(@Param("statusId") Long statusId);

    @Query("SELECT i FROM Invoice i WHERE i.customer.id = :customerId OR i.customer.vatCode = :vatCode OR i.customer.pec = :pec ORDER BY i.date ASC")
    public List<Invoice> findAllByCustomerOrderByDateAsc(@Param("customerId") Long customerId, @Param(("vatCode")) String vatCode, @Param("pec") String pec);

    @Query("SELECT i FROM Invoice i WHERE i.customer.id = :customerId OR i.customer.vatCode = :vatCode OR i.customer.pec = :pec ORDER BY i.date DESC")
    public List<Invoice> findAllByCustomerOrderByDateDesc(@Param("customerId") Long customerId, @Param(("vatCode")) String vatCode, @Param("pec") String pec);

    public List<Invoice> findAllByDate(LocalDate date);

    @Query("SELECT i FROM Invoice i WHERE EXTRACT(YEAR FROM i.date) = :year")
    public List<Invoice> findAllByYear(@Param("year") int year);

    public List<Invoice> findAllByAmountBetweenOrderByAmountAsc(double min, double max);

    public List<Invoice> findAllByCustomerIdOrderByNumberDesc(Long customerId);

    @Query("SELECT MAX(i.number) FROM Invoice i")
    Optional<Integer> findMaxNumber();

    Optional<Invoice> findFirstByNumber(int number);

    @Query("SELECT i FROM Invoice i WHERE i.customer.vatCode = :vatCode")
    public List<Invoice> findAllByVatCode(@Param("vatCode") String vatCode);

    @Query("SELECT SUM(i.amount) FROM Invoice i WHERE i.customer.id = :customerId AND EXTRACT(YEAR FROM i.date) = :year")
    public Optional<Double> findTotalAllByCustomerAndYear(@Param("customerId") Long customerId, @Param("year") int year);

    @Query("SELECT SUM(i.amount) FROM Invoice i WHERE EXTRACT(YEAR FROM i.date) = :year AND (i.status.name = 'PAID' OR i.status.name = 'PARTIALLY PAID' OR i.status.name = 'ARCHIVED')")
    public Optional<Double> getTotal(@Param("year") int year);

    @Query("SELECT SUM(i.amount) FROM Invoice i WHERE EXTRACT(YEAR FROM i.date) = :year AND i.customer.id = :customerId AND (i.status.name = 'PAID' OR i.status.name = 'PARTIALLY PAID' OR i.status.name = 'ARCHIVED')")
    public Optional<Double> getTotalByCustomer(@Param("customerId") Long customerId, @Param("year") int year);

    @Query("SELECT i FROM Invoice i WHERE i.status.name IN ('SENT', 'PARTIALLY PAID', 'OVERDUE')")
    public List<Invoice> findAllWaitingPayment();

    @Query("SELECT i FROM Invoice i WHERE EXTRACT(YEAR FROM i.date) = :year ORDER BY i.number DESC")
    Page<Invoice> findLatest(@Param("year") int year, Pageable pageable);
}


