package epicode.it.energyservices.utils.upload;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {
    private final UploadSvc uploadSvc;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file)  {
        String message = null;
            message = uploadSvc.uploadFile(file);

        Map<String, String> response = new HashMap<>();
        response.put("url", message);
        return ResponseEntity.ok(response);
    }

}
