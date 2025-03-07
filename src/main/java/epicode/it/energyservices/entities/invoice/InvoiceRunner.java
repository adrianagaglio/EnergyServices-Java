package epicode.it.energyservices.entities.invoice;

import com.github.javafaker.Faker;
import epicode.it.energyservices.entities.invoice.dto.InvoiceRequest;
import epicode.it.energyservices.entities.invoice_status.InvoiceStatus;
import epicode.it.energyservices.entities.invoice_status.InvoiceStatusSvc;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Order(5)
public class InvoiceRunner implements ApplicationRunner {
    private final InvoiceSvc invoiceSvc;
    private final InvoiceStatusSvc statusSvc;
    private final Faker faker;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("INVOICE RUNNER STARTED");

        if (invoiceSvc.count() == 0) {
            for (int i = 0; i < 100; i++) {
                InvoiceRequest request = new InvoiceRequest();
                LocalDate date = LocalDate.of(faker.random().nextInt(2022, 2024), faker.random().nextInt(1, 12), faker.random().nextInt(1, 28));
                request.setDate(date);
                request.setAmount(faker.number().randomDouble(2, 2, 1200));
                request.setStatus(statusSvc.getAll().get(faker.random().nextInt(0, statusSvc.getAll().size() - 1)).getName());
                request.setCustomerId(faker.random().nextInt(1, 140).longValue());

                try {
                    invoiceSvc.create(request);
                } catch (RuntimeException e) {
                    System.out.println("in errore " + request);
                    System.out.println(e.getMessage());
                }
            }

            for (int i = 0; i < 50; i++) {
                InvoiceRequest request = new InvoiceRequest();
                LocalDate date = LocalDate.of(2025, 1, faker.random().nextInt(1, 31));
                request.setDate(date);
                request.setAmount(faker.number().randomDouble(2, 2, 1200));
                request.setStatus(statusSvc.getAll().get(faker.random().nextInt(0, statusSvc.getAll().size() - 1)).getName());
                request.setCustomerId(faker.random().nextInt(1, 79).longValue());

                try {
                    invoiceSvc.create(request);
                } catch (RuntimeException e) {
                    System.out.println("in errore " + request);
                    System.out.println(e.getMessage());
                }
            }
        }


        System.out.println("Invoice Runner finished");

    }
}
