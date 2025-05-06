/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.config;

import javax.crypto.SecretKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author carlo
 */

@Configuration
public class JwtConfig {
    
    @Bean
    public JwtUtil jwtUtil(SecretKey secretKey) {
        return new JwtUtil(secretKey);
    }
}
