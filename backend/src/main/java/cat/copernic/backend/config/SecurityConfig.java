package cat.copernic.backend.config;

import cat.copernic.backend.logic.auth.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import io.jsonwebtoken.security.Keys;

/**
 * Classe de configuració de seguretat per l'aplicació.
 * Aquí configurem dues cadenes de seguretat (una per a l'API mòbil amb JWT i una altra per a la web amb login de formulari).
 * També definim codificadors, autenticadors i el filtre JWT personalitzat.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Bean que retorna el filtre personalitzat per validar els tokens JWT a les peticions de l'API.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService);
    }

    /**
     * Cadena de seguretat per a les rutes de l'API mòbil (prefixed amb /api/**).
     * Utilitza autenticació JWT (sense sessió) i no permet accés si no es valida el token.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**") // Aquesta configuració només afecta a les rutes que comencen amb /api
            .csrf(csrf -> csrf.disable()) // Desactivem CSRF per l'API
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sense sessió, tot via JWT
            .formLogin(login -> login.disable()) // No fem servir login de formulari
            .logout(logout -> logout.disable()) // Tampoc cal logout de sessió
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/loginMobile", "/api/auth/recover", "/api/auth/reset").permitAll() // Permetem accés lliure a aquestes rutes
                .anyRequest().authenticated() // La resta requereix autenticació amb token
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    // Si no hi ha autenticació vàlida, tornem un error 401
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
                })
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // Afegim el filtre JWT abans del filtre per defecte

        return http.build();
    }

    /**
     * Cadena de seguretat per a la part web (formularis d'autenticació, sessió amb cookies).
     */
    @Bean
    @Order(2)
    public SecurityFilterChain webSecurity(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")) // Ignorem CSRF per les rutes de l'API
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/styles/**", "/scripts/**", "/images/**", "/css/**", "/api/auth/recover", "/api/auth/reset").permitAll() // Rutes públiques
                .requestMatchers("/admin/**").hasRole("ADMIN") // Només admins poden accedir a /admin/**
                .anyRequest().authenticated() // La resta de rutes requereixen login
            )
            .formLogin(form -> form
                .loginPage("/login") // Ruta personalitzada del login
                .defaultSuccessUrl("/", true) // Redirecció un cop logat
                .usernameParameter("mail") // Camp de l’email
                .passwordParameter("word") // Camp de la contrasenya
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // Permetem logout via GET (important per compatibilitat)
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }

    /**
     * Codificador de contrasenyes usant BCrypt. Es fa servir tant al login web com a l’API.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provider d'autenticació basat en el servei de detalls d'usuari i l'encodador.
     */
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Bean per obtenir el {@code AuthenticationManager}, necessari per a la gestió del login.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
