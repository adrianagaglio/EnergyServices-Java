package epicode.it.energyservices.entities.city;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import epicode.it.energyservices.entities.district.District;
import epicode.it.energyservices.entities.district.DistrictRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Service
public class CityImportSvc {
    private final DistrictRepo districtRepo;
    private final CityRepo cityRepo;

    public void importCsv(String filePath) {
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            List<String[]> rows = csvReader.readAll(); // Legge tutte le righe

            for (int i = 1; i < rows.size(); i++) { // Salta l'intestazione
                String[] input = rows.get(i);
                String field = input[0];
                String[] fields = field.split(";");


                if (fields.length < 3) { // Controllo righe
                    continue;
                }

                String cityName = fields[2]; // Nome della città
                String districtName = fields[3]; // Nome della provincia

                District district = districtRepo.findByName(districtName)
                        .orElseGet(() -> {
                            District newDistrict = new District();
                            newDistrict.setName(districtName);
                            return districtRepo.save(newDistrict);
                        });

                City city = new City();
                city.setName(cityName);
                city.setDistrict(district);
                cityRepo.save(city);


            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }
}