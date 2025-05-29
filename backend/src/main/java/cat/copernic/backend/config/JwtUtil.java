package cat.copernic.backend.config;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

/**
 * Classe utilitària per gestionar operacions relacionades amb JSON Web Tokens (JWT).
 * Aquesta classe encapsula la lògica per generar, validar i extreure informació dels tokens.
 * 
 * L'algorisme utilitzat per signar el token és HS512 i es fa servir una clau secreta injectada.
 */
@Component
public class JwtUtil {

    // Clau secreta per signar i verificar els tokens
    private final SecretKey secretKey;

    // Temps d'expiració del token en mil·lisegons (1 dia)
    private final long EXPIRATION_TIME = 86400000;

    /**
     * Constructor que rep la clau secreta per signar i validar tokens.
     * @param secretKey clau secreta proporcionada pel bean corresponent
     */
    public JwtUtil(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * Genera un token JWT a partir del correu electrònic de l'usuari.
     * 
     * @param email l'email que identificarem com a subjecte del token
     * @return una cadena JWT vàlida
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email) // Assignem l'email com a subjecte del token
                .setIssuedAt(new Date()) // Data de creació
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Data d'expiració
                .signWith(secretKey, SignatureAlgorithm.HS512) // Signatura amb HS512
                .compact(); // Compilació del token
    }

    /**
     * Extreu l'email del subjecte a partir d'un token JWT.
     * 
     * @param token el token JWT a analitzar
     * @return l'email (subjecte) inclòs dins del token
     * @throws JwtException si el token no és vàlid o no es pot analitzar
     */
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // Assignem la clau per verificar la signatura
                .build()
                .parseClaimsJws(token) // Anàlisi del token
                .getBody()
                .getSubject(); // Retornem el subjecte (email)
    }

    /**
     * Comprova si un token és vàlid. El token ha de tenir una signatura correcta i no estar caducat.
     * 
     * @param token el token a validar
     * @return true si el token és vàlid, false si és invàlid o ha caducat
     */
    public boolean validateToken(String token) {
        try {
            extractEmail(token); // Si es pot extreure l'email sense excepcions, és vàlid
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
