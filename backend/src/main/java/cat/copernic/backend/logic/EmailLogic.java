package cat.copernic.backend.logic;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Servei responsable de gestionar l'enviament de correus electrònics a l'usuari,
 * com per exemple: correus de recuperació de contrasenya o notificacions de caducitat del compte.
 * 
 * Utilitza una plantilla HTML estilitzada amb colors corporatius per a una millor experiència d'usuari.
 * També adjunta el logotip de l'empresa mitjançant CID per mostrar-lo en el cos del correu.
 * 
 * Els paràmetres de configuració del servidor SMTP es defineixen en el fitxer `application.properties`.
 * 
 * Exemple de dependència:
 * {@code spring.mail.username=example@entrebicis.cat}
 * 
 * @author carlo
 */
@Service
public class EmailLogic {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Adreça de correu electrònic configurada com a remitent.
     * Obtinguda des de `spring.mail.username` del fitxer de configuració.
     */
    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Envia un correu electrònic amb un codi de recuperació de contrasenya.
     * El missatge conté un disseny professional i el logotip de l'aplicació.
     *
     * @param to    Adreça de correu electrònic del destinatari.
     * @param token Codi únic de recuperació de contrasenya que l'usuari haurà d'introduir.
     */
    public void sendPasswordResetEmail(String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Recuperación de contraseña");

            String htmlContent = "<html>"
                    + "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 30px;'>"
                    + "<div style='max-width: 600px; margin: auto; background: #ffffff; padding: 40px; border-radius: 12px; text-align: center; box-shadow: 0 4px 12px rgba(0,0,0,0.1);'>"
                    + "<img src='cid:logoImage' alt='Logo EntreBicis' style='width: 100px; margin-bottom: 25px;' />"
                    + "<h2 style='color: #2e7d32; margin-bottom: 10px;'>Recuperació de Contrasenya</h2>"
                    + "<p style='color: #333; font-size: 16px; margin-bottom: 25px;'>Has sol·licitat restablir la teva contrasenya. Introdueix el següent codi per continuar:</p>"
                    + "<div style='font-size: 26px; color: #2e7d32; font-weight: bold; padding: 10px 20px; background-color: #e8f5e9; display: inline-block; border-radius: 8px; letter-spacing: 2px;'>"
                    + token + "</div>"
                    + "<p style='color: #555; font-size: 14px; margin-top: 30px;'>Si no has fet aquesta sol·licitud, pots ignorar aquest correu.</p>"
                    + "</div>"
                    + "</body></html>";

            helper.setText(htmlContent, true);
            ClassPathResource logo = new ClassPathResource("static/images/EntreBicisLogoWB.png");
            helper.addInline("logoImage", logo);

            mailSender.send(message);
            System.out.println("Correo enviado con éxito");

        } catch (MessagingException e) {
            System.out.println("Error al enviar correo: " + e.getMessage());
        }
    }

    /**
     * Envia un correu informant a l'usuari que el seu compte ha caducat.
     * Aquest missatge pot animar l'usuari a restablir la contrasenya o contactar amb suport.
     *
     * @param to Adreça de correu electrònic del destinatari.
     */
    public void sendExpirationMessage(String to) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Informació de compte");

            String htmlContent = "<html>"
                    + "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>"
                    + "<div style='max-width: 600px; margin: auto; background: white; padding: 20px; border-radius: 10px; text-align: center;'>"
                    + "<img src='cid:logoImage' alt='Logo de EntreBicis' style='width: 100px; margin-bottom: 20px;'>"
                    + "<h2 style='color: #2e7d32;'>Informació de l'estat del compte</h2>"
                    + "<p style='color: #333;'>T'informem que el teu compte ha caducat. Si vols continuar utilitzant l'aplicació, restableix la teva contrasenya.</p>"
                    + "</div>"
                    + "</body></html>";

            helper.setText(htmlContent, true);
            ClassPathResource logo = new ClassPathResource("static/images/EntreBicisLogoWB.png");
            helper.addInline("logoImage", logo);

            mailSender.send(message);
            System.out.println("Correo enviado con éxito");

        } catch (MessagingException e) {
            System.out.println("Error al enviar correo: " + e.getMessage());
        }
    }
}
