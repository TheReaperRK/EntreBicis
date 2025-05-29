/*
 * Configuració del servei de correu electrònic per a l'aplicació EntreBicis.
 * Aquesta classe defineix el bean necessari per poder enviar correus mitjançant SMTP.
 */
package cat.copernic.backend.config;

import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Classe de configuració per habilitar l'enviament de correus electrònics
 * des del backend de l'aplicació. Utilitzem el servidor SMTP de Gmail amb autenticació TLS.
 * 
 * Aquesta configuració és necessària per funcionalitats com la recuperació de contrasenya,
 * notificacions, etc.
 */
@Configuration
public class EmailConfig {

    /**
     * Defineix el bean {@code JavaMailSender} utilitzat per enviar correus electrònics.
     * 
     * @return una instància de {@code JavaMailSender} configurada amb el servidor de Gmail
     */
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Configuració bàsica del servidor SMTP de Gmail
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        // Credencials del compte Gmail utilitzat per enviar els correus
        mailSender.setUsername("AdMeProject3@gmail.com");
        mailSender.setPassword("oxza mtdt iewj hkxt");

        // Propietats addicionals per a la connexió SMTP
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");        // Protocol SMTP
        props.put("mail.smtp.auth", "true");                 // Autenticació habilitada
        props.put("mail.smtp.starttls.enable", "true");      // Encriptació TLS
        props.put("mail.debug", "true");                     // Mostra informació de debug per consola

        return mailSender;
    }

}
