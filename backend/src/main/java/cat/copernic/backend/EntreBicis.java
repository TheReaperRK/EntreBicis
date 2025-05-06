/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package cat.copernic.backend;

import cat.copernic.backend.entity.User;
import cat.copernic.backend.entity.enums.Role;
import cat.copernic.backend.repository.UserRepo;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author carlo
 */
@SpringBootApplication
@ComponentScan
public class EntreBicis {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(EntreBicis.class, args);

    }

}
