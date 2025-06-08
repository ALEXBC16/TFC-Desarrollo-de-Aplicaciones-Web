package edu.lopezalejandro._aMarcha.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreoConfirmacion(String to, String nombreUsuario) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Bienvenido a 1ª Marcha 🚗");
        helper.setText(
            "<h2>Hola " + nombreUsuario + " 👋</h2>" +
            "<p>Tu cuenta ha sido creada correctamente.</p>" +
            "<p>Gracias por unirte a <b>1ª Marcha</b>.</p>",
            true
        );

        mailSender.send(mensaje);
    }
}
