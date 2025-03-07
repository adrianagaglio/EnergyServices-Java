package epicode.it.energyservices.entities.district;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import epicode.it.energyservices.entities.district.dto.DistrictMapper;
import epicode.it.energyservices.entities.district.dto.ProvinceHttpResponse;
import jakarta.persistence.EntityNotFoundException;
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
public class DistrictSvc {
    private final DistrictMapper mapper;

    public List<District> getDistricts() {
        String url = "https://axqvoqvbfjpaamphztgd.functions.supabase.co/province";
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

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
            return mapper.toDistrictList(objectMapper.readValue(response.body(), new TypeReference<List<ProvinceHttpResponse>>() {
            }));
        } catch(IOException e) {
            throw new RuntimeException("Errore durante il parsing del JSON");
        }
    }
}