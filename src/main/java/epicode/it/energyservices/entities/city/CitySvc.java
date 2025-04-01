package epicode.it.energyservices.entities.city;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import epicode.it.energyservices.entities.city.dto.CityHttpResponse;
import epicode.it.energyservices.entities.city.dto.CityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@AllArgsConstructor
@Validated
public class CitySvc {

    private final CityMapper mapper;

    public List<City> getCitiesByDistrict(String district) {
        String url = "https://axqvoqvbfjpaamphztgd.functions.supabase.co/comuni/provincia/" + district.replace(" ", "%20");

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = null;

        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return mapper.toCityList(objectMapper.readValue(response.body(), new TypeReference<List<CityHttpResponse>>() {
            }));
        } catch(IOException e) {
            throw new RuntimeException("Errore durante il parsing del JSON", e);
        }
    }
}
