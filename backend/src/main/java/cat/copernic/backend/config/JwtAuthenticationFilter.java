/*
 * Filtres de seguretat per a l'autenticació mitjançant JSON Web Token (JWT).
 * Aquest filtre s'executa un cop per cada petició i s'encarrega de validar el token
 * present a l'encapçalament "Authorization" de la petició.
 */
package cat.copernic.backend.config;

import cat.copernic.backend.logic.auth.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtre de seguretat personalitzat que s'encarrega de processar i validar els tokens JWT.
 * Aquest filtre només s'executa un cop per petició (gràcies a l'extensió {@link OncePerRequestFilter}).
 * Si el token és vàlid, assigna l'autenticació a l'usuari dins el context de seguretat de Spring.
 * 
 * @author carlo
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Constructor que rep les dependències necessàries: la classe utilitària JWT i el servei de detalls d'usuari.
     * @param jwtUtil classe que gestiona operacions sobre el token JWT
     * @param userDetailsService servei que carrega l'usuari a partir del correu (email)
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Mètode principal que intercepta totes les peticions HTTP.
     * Comprova si hi ha un token, el valida, i en cas afirmatiu autentica l'usuari associat.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Extracció del token de l'encapçalament Authorization
        String token = extractTokenFromHeader(request);

        if (StringUtils.hasText(token)) {
            if (jwtUtil.validateToken(token)) {
                // Si el token és vàlid, extraiem l'email (usuari) del token
                String username = jwtUtil.extractEmail(token);

                // Carreguem les dades de l'usuari des del servei personalitzat
                var userDetails = userDetailsService.loadUserByUsername(username);

                // Creem un objecte d'autenticació a partir de l'usuari
                var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Assignem detalls extres (com IP, sessió, etc.)
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Assignem l'autenticació al context de seguretat de Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else {
                // Si el token és invàlid, retornem error 401 i no continuem amb la cadena de filtres
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
                return;
            }
        }

        // Si no hi ha token, o si és una ruta pública, es continua amb la cadena de filtres
        filterChain.doFilter(request, response);
    }

    /**
     * Extreu el token JWT de l'encapçalament Authorization si està en el format correcte (Bearer ...).
     * @param request la petició HTTP rebuda
     * @return el token JWT com a cadena, o null si no és present o vàlid
     */
    private String extractTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Eliminem "Bearer "
        }
        return null;
    }

}
