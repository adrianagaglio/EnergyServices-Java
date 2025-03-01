package epicode.it.energyservices.entities.invoice_status;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceStatusRepo extends JpaRepository<InvoiceStatus, Long> {

    public Optional<InvoiceStatus> findByNameOrderByNameAsc(String name);
}
