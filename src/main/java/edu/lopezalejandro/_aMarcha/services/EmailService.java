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

        helper.setFrom("no-reply@1amarcha.com");
        helper.setTo(to);
        helper.setSubject("Bienvenido a 1ª Marcha 🚗");

        String contenido = """
            <div style="font-family: Arial, sans-serif; background-color:#f5f5f5; padding:20px;">
                
                <div style="max-width:600px; margin:auto; background:white; padding:30px; border-radius:10px;">
                    
                    <h2 style="color:#2c3e50;">Hola %s 👋</h2>
                    
                    <p>Tu cuenta en <b>1ª Marcha</b> ha sido creada correctamente.</p>
                    
                    <p>
                        Ya puedes iniciar sesión en la plataforma y comenzar a realizar 
                        <b>tests de preparación para el carnet de conducir</b>.
                    </p>
                    
                    <div style="margin-top:20px; padding:15px; background:#ecf0f1; border-radius:5px;">
                        🚗 ¡Mucho éxito en tu preparación!
                    </div>
                    
                    <p style="margin-top:30px; font-size:14px; color:#777;">
                        Este es un correo automático, por favor no respondas a este mensaje.
                    </p>
                    
                    <hr>
                    
                    <p style="font-size:12px; color:#999;">
                        © 2026 1ª Marcha - Plataforma de tests de conducción
                    </p>
                    
                </div>
            
            </div>
            """.formatted(nombreUsuario);

        helper.setText(contenido, true);

        mailSender.send(mensaje);
    }
}