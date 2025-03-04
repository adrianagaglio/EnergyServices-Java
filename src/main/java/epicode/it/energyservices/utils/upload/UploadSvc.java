package epicode.it.energyservices.utils.upload;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import epicode.it.energyservices.exceptions.MaxSizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class UploadSvc {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file)  {
        Map uploadResult = null;
        try {
            uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "energy-services",
                    "resource_type", "auto",
                    "transformation", new Transformation().quality("auto:low") // Riduce la qualità per minimizzare la dimensione
                            .width(64) // Riduce la larghezza a 64px
                            .height(64) // Riduce l'altezza a 64px
                            .crop("fill"),
                    "filename", file.getOriginalFilename()));
        } catch (IOException e) {
            throw new MaxSizeException("File too big");
        }
        return uploadResult.get("url").toString(); // Restituisce l'URL del file caricato
    }


}
