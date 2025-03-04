package epicode.it.energyservices.entities.invoice_status;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import epicode.it.energyservices.entities.invoice_status.dto.InvoiceStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Component
@Order(1)
@RequiredArgsConstructor
public class InvoiceStatusRunner implements ApplicationRunner {
    private final InvoiceStatusSvc invoiceStatusSvc;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String csvFile = getClass().getClassLoader().getResource("invoice_statuses.csv").getPath();


        if (invoiceStatusSvc.count() == 0) {

            try (CSVReader csvReader = new CSVReader(new FileReader(csvFile))) {
                List<String[]> rows = csvReader.readAll();

                // salta l'intestazione partendo da indice 1
                for (int i = 1; i < rows.size(); i++) {
                    String[] row = rows.get(i);

                    InvoiceStatusRequest status = new InvoiceStatusRequest();

                    status.setName(row[0].toUpperCase()); // colonna 1
                    status.setDescription(row[1]); // colonna 2
                    invoiceStatusSvc.create(status);
                }

                System.out.println("===> Import completed!");

            } catch (IOException | CsvException e) {

                e.printStackTrace();
            }
        }
    }
}

