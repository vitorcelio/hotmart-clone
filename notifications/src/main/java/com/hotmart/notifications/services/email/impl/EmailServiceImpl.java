package com.hotmart.notifications.services.email.impl;

import com.hotmart.notifications.config.exception.EmailSentException;
import com.hotmart.notifications.dto.event.AccessDataEventDTO;
import com.hotmart.notifications.dto.event.EventDTO;
import com.hotmart.notifications.dto.event.RecoverPasswordEventDTO;
import com.hotmart.notifications.dto.response.NotificationsResponseDTO;
import com.hotmart.notifications.enums.SentStatus;
import com.hotmart.notifications.enums.TemplateType;
import com.hotmart.notifications.services.email.EmailService;
import com.hotmart.notifications.services.notifications.NotificationsService;
import com.hotmart.notifications.utils.EmailUtils;
import com.hotmart.notifications.utils.JsonUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service("myEmailService")
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final JsonUtil jsonUtil;
    private final EmailUtils emailUtils;
    private final NotificationsService service;

    @Override
    public void execute(EventDTO event, String payload) {
        NotificationsResponseDTO notification = service.save(event);

        try {
            TemplateType template = event.getTemplate();

            switch (template) {
                case CHANGE_PASSWORD -> sendEmailRecoverPassword(payload);
                case DATA_ACCESS -> sendEmailDataAccess(payload);
                default -> throw new EmailSentException("Invalid template");
            }

            service.viewedOrSent(notification.getId(), SentStatus.SENT);

        } catch (EmailSentException e) {
            log.error("Falha ao enviar notificação via e-mail (ID: {}): ", notification.getId(), e);
            service.viewedOrSent(notification.getId(), SentStatus.FAILED);

        } catch (RuntimeException e) {
            log.error("Erro técnico ao enviar notificação via e-mail (ID: {}): ", notification.getId(), e);
            service.viewedOrSent(notification.getId(), SentStatus.ERROR);

        }
    }

    private void sendEmailRecoverPassword(String payload) {
        RecoverPasswordEventDTO event = jsonUtil.toRecoverPasswordEvent(payload);

        try {
            final MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper email = emailUtils.createConfigEmail(mimeMessage, event.getEmail(), event.getTemplate().getTitle());

            Context context = new Context(LocaleContextHolder.getLocale());
            context.setVariable("pswdrst", event.getPswdrst());
            context.setVariable("uuid", event.getUuid());

            final String html = templateEngine.process(event.getTemplate().getPage(), context);

            email.setText(html, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Erro ao enviar email de recuperação de senha: " + e.getMessage());
        } catch (Exception e) {
            throw new EmailSentException("Falha ao enviar email de recuperação de senha: " + e.getMessage());
        }
    }

    private void sendEmailDataAccess(String payload) {
        AccessDataEventDTO event = jsonUtil.toAccessDataEvent(payload);

        try {
            final MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper email = emailUtils.createConfigEmail(mimeMessage, event.getEmail(), event.getTemplate().getTitle());

            Context context = new Context(LocaleContextHolder.getLocale());
            context.setVariable("name", event.getName());
            context.setVariable("uuid", event.getUuidAccessData());

            final String html = templateEngine.process(event.getTemplate().getPage(), context);

            email.setText(html, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Erro ao enviar email de recuperação de senha: " + e.getMessage());
        } catch (Exception e) {
            throw new EmailSentException("Falha ao enviar email de recuperação de senha: " + e.getMessage());
        }
    }

}
