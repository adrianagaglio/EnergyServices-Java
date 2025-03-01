package epicode.it.energyservices.entities.district;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import epicode.it.energyservices.entities.city.City;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class DistrictImportSvc {
    private final DistrictRepo districtRepo;

    @Transactional
    public void importCsvDistrict(String filePath) {
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            List<String[]> rows = csvReader.readAll(); // Legge tutte le righe

            for (int i = 1; i < rows.size(); i++) { // Salta l'intestazione
                String[] input = rows.get(i);
                String field = input[0];
                String[] fields = field.split(";");


                if (fields.length < 3) { // Controllo righe
                    continue;
                }

                String code = fields[0]; //code
                String districtName = fields[1]; // Nome della provincia
                String region = fields[2]; // Nome della regione

                District newDistrict = new District();
                newDistrict.setName(districtName);
                newDistrict.setCode(code);
                newDistrict.setRegion(region);

                districtRepo.save(newDistrict);

            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }
}
