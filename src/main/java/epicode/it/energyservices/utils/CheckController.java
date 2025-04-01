package epicode.it.energyservices.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/api/healthcheck/db")
    public ResponseEntity<String> checkDatabase() {
        try {
            // Esegui una query semplice
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return ResponseEntity.ok("Database connesso");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore connessione DB: " + e.getMessage());
        }
    }
}