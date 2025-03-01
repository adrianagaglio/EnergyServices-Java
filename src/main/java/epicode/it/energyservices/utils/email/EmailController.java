package epicode.it.energyservices.utils.email;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailSvc emailSvc;


    @PostMapping("/{isHtml}")
    public ResponseEntity<String> sendHtmlEmail(@RequestBody EmailRequest request, @RequestParam boolean isHtml) {
        if (isHtml) {
            return new ResponseEntity<>(emailSvc.sendEmail(request), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(emailSvc.sendEmail(request), HttpStatus.CREATED);
        }
    }
}
