package epicode.it.energyservices.auth.jwt;

import epicode.it.energyservices.auth.AppUser;
import epicode.it.energyservices.auth.AppUserRepo;
import epicode.it.energyservices.auth.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {
    @Autowired
    private AppUserRepo appUserRepo;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        AppUser appUser = appUserRepo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        List<String> roles = appUser.getRoles()
                .stream()
                .map(Enum::name) // Converte gli enum in stringhe
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", roles) // Aggiunge i ruoli come claim
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String generateTokenResetPassword(AppUser appUser) {
        return Jwts.builder()
                .setSubject(appUser.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 *60*15))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }



    public Set<Role> getRolesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        List<String> rolesAsString = ((List<?>) claims.get("roles"))
                .stream()
                .map(Object::toString)
                .toList();

        return rolesAsString.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        final Set<Role> roles = getRolesFromToken(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}
