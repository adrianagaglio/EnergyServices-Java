package epicode.it.energyservices.utils.email;

import epicode.it.energyservices.auth.AppUser;
import epicode.it.energyservices.entities.invoice.Invoice;
import epicode.it.energyservices.entities.invoice.dto.InvoiceResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Component
public class EmailMapper {


    @Value("${spring.website}")
    private String website;

    public EmailRequest fromInvoicetoEmailRequest(String text, Invoice invoice) {
        EmailRequest request = new EmailRequest();
        request.setTo(invoice.getCustomer().getAppUser().getEmail());
        String number = invoice.getNumber() < 10 ? "00" + invoice.getNumber() : invoice.getNumber() < 100 ? "0" + invoice.getNumber() : invoice.getNumber() + "";
        request.setSubject("Energyservices - " + text + " #" + number);
        request.setBody(fromInvoiceToEmailBody(text, invoice));
        return request;
    }

    public EmailRequest fromAppUserToEmailRequest(String text, AppUser user) {
        EmailRequest request = new EmailRequest();
        request.setTo(user.getEmail());
        request.setSubject("Energyservices - " + text);
        request.setBody(fromAppUserToEmailBody(text, user));
        return request;
    }

    public String fromInvoiceToEmailBody(String text, Invoice invoice) {
        String template = loadTemplateFromClasspath("invoice.html");
        Map<String, String> values = new HashMap<>();
        values.put("text", text);
        values.put("number", invoice.getNumber() < 10 ? "00" + invoice.getNumber() : invoice.getNumber() < 100 ? "0" + invoice.getNumber() : invoice.getNumber() + "");
        values.put("amount", invoice.getAmount() + "");
        values.put("date", invoice.getDate() + "");
        values.put("status", invoice.getStatus().getName());
        values.put("website", website);
        return processTemplate(template, values);
    }

    public String fromAppUserToEmailBody(String text, AppUser user) {
        String template = loadTemplateFromClasspath("user.html");
        Map<String, String> values = new HashMap<>();
        values.put("text", text);
        values.put("username", user.getUsername());
        values.put("email", user.getEmail());
        values.put("website", website);
        return processTemplate(template, values);
    }

    public String forResetPasswordRequestBody(String link) {
        String template = loadTemplate("src/main/resources/templates/resetPassword.html");
        Map<String, String> values = new HashMap<>();
        values.put("link", link);
        values.put("website", website);
        return processTemplate(template, values);
    }

    public EmailRequest fromResetPasswordBodyToEmailRequest(String link, AppUser user) {

        EmailRequest request = new EmailRequest();
        request.setTo(user.getEmail());
        request.setSubject("Energyservices - Reset password");
        request.setBody(forResetPasswordRequestBody(link));
        return request;
    }

    public String forResetPasswordSuccess() {
        String template = loadTemplateFromClasspath("resetPasswordSuccess.html");
        Map<String, String> values = new HashMap<>();
        return processTemplate(template, values);
    }

    public EmailRequest fromResetPasswordSuccessBodyToEmailRequest(AppUser user) {
        EmailRequest request = new EmailRequest();
        request.setTo(user.getEmail());
        request.setSubject("Energyservices - Reset password");
        request.setBody(forResetPasswordSuccess());
        return request;
    }

    private String loadTemplate(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String loadTemplateFromClasspath(String templateName) {
        try {
            // nel classpath
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("templates/" + templateName)) {
                if (is != null) {
                    return new String(is.readAllBytes(), StandardCharsets.UTF_8);
                }
            }

            File fileInAppResources = new File("app/resources/templates/" + templateName);
            if (fileInAppResources.exists()) {
                return Files.readString(fileInAppResources.toPath(), StandardCharsets.UTF_8);
            }

            File fileInSrcResources = new File("src/main/resources/templates" + templateName);
            if (fileInSrcResources.exists()) {
                return Files.readString(fileInSrcResources.toPath(), StandardCharsets.UTF_8);
            }

            throw new IOException("Template non trovato: " + templateName +
                                  ". Cercato in: classhpath:templates/, app/resources/templates/, src/main/resources/templates");

        } catch (IOException e) {
            // Logga l'errore e rilancia
            System.err.println("Errore caricando il template " + templateName + ": " + e.getMessage());
            e.printStackTrace();

            return "<html><body></body></html>";
        }
    }


    private String processTemplate(String template, Map<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return template;
    }
}
