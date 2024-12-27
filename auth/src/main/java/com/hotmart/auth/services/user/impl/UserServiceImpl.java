package com.hotmart.auth.services.user.impl;

import com.hotmart.auth.config.exception.ValidationException;
import com.hotmart.auth.dto.event.RecoverPasswordEventDTO;
import com.hotmart.auth.dto.request.ChangePasswordRequestDTO;
import com.hotmart.auth.dto.request.RecoveryPasswordRequestDTO;
import com.hotmart.auth.dto.request.UserRequestDTO;
import com.hotmart.auth.enums.Roles;
import com.hotmart.auth.models.RecoverPasswordEntity;
import com.hotmart.auth.models.RoleEntity;
import com.hotmart.auth.models.UserEntity;
import com.hotmart.auth.producer.KafkaProducer;
import com.hotmart.auth.repositories.RecoverPasswordRepository;
import com.hotmart.auth.repositories.UserRepository;
import com.hotmart.auth.services.user.UserService;
import com.hotmart.auth.utils.JsonUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.UUID;

import static com.hotmart.auth.enums.EventType.EMAIL;
import static com.hotmart.auth.enums.TemplateType.CHANGE_PASSWORD;

@RequiredArgsConstructor
@Slf4j
@Service("myUserServiceImpl")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RecoverPasswordRepository recoverPasswordRepository;
    private final KafkaProducer kafkaProducer;
    private final JsonUtil jsonUtil;

    @Value("${spring.kafka.topic.email}")
    private String emailTopic;

    @Override
    public void save(@NonNull UserRequestDTO request) {
        validationUserAlreadyExistsByEmail(request.getEmail());
        createUser(request);
    }

    @Override
    public void recovery(@NonNull RecoveryPasswordRequestDTO request) {
        RecoverPasswordEntity recoverPassword = createRecoverPassword(request);
        sendNotificationRecoverPassword(recoverPassword);
    }

    @Override
    public void updatePassword(@NonNull ChangePasswordRequestDTO request, @NonNull String pswdrst, @NonNull String uuid) {
        validationPassword(request);
        validationTokenUser(pswdrst, uuid);
        changePasswordUser(request, pswdrst);
        updateRecoverPassword(pswdrst);
    }

    @Override
    public void validationTokenUser(@NonNull String pswdrst, @NonNull String uuid) {
        try {
            var recoverPassword = recoverPasswordRepository.findByPswdrst(pswdrst).orElseThrow(() -> new ValidationException("Código de Recuperação inválido."));

            UserEntity user = recoverPassword.getUser();

            long hours = Duration.between(recoverPassword.getCreatedAt(), LocalDateTime.now()).toHours();
            if (hours > 10) {
                throw new ValidationException("Token expirado.");
            }

            if (!user.getUuid().equals(uuid)) {
                throw new ValidationException("Sem autorização para recuperar a senha.");
            }

            if (recoverPassword.isUpdated()) {
                throw new ValidationException("Este token já foi usado anteriormente.");
            }

        } catch (Exception e) {
            throw new ValidationException("Token inválido, não foi possível atualizar a senha.");
        }
    }

    private void validationUserAlreadyExistsByEmail(@NonNull String email) {
        UserEntity user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            log.error("User with email {} already exists", email);
            throw new ValidationException("Email existente. Identificamos que você já possui uma conta com este email na Hotmart. Recupere sua senha e/ou faça login.");
        }
    }

    private void createUser(@NonNull UserRequestDTO request) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Set<RoleEntity> roles = Set.of(RoleEntity.builder()
                .id(Roles.valueOf(request.getRole()).getId())
                .name(Roles.valueOf(request.getRole()).name())
                .build());

        UserEntity build = UserEntity.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(encoder.encode(request.getPassword()))
                    .roles(roles)
                    .build();

        userRepository.save(build);
    }

    private void validationPassword(@NonNull ChangePasswordRequestDTO request) {
        if (!request.getPassword().equals(request.getPasswordRepeat())) {
            throw new ValidationException("As senhas não coincidem.");
        }
    }

    private void changePasswordUser(@NonNull ChangePasswordRequestDTO request, @NonNull String pswdrst) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        var recoverPassword = recoverPasswordRepository.findByPswdrst(pswdrst).orElseThrow(() -> new ValidationException("Código de Recuperação inválido."));

        UserEntity user = recoverPassword.getUser();
        user.setPassword(encoder.encode(request.getPassword()));

        userRepository.save(user);
    }

    private RecoverPasswordEntity createRecoverPassword(@NonNull RecoveryPasswordRequestDTO request) {

        UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new ValidationException("Usuário não encontrado."));

        RecoverPasswordEntity recoverPassword = RecoverPasswordEntity.builder()
                .user(user)
                .build();


        return recoverPasswordRepository.save(recoverPassword);
    }

    private void updateRecoverPassword(@NonNull String pswdrst) {
        var recoverPassword = recoverPasswordRepository.findByPswdrst(pswdrst).orElseThrow(() -> new ValidationException("Código de Recuperação inválido."));
        recoverPassword.setUpdated(true);
        recoverPasswordRepository.save(recoverPassword);
    }

    private void sendNotification(@NonNull String event, @NonNull String topic) {
        kafkaProducer.sendEvent(event, topic);
    }

    private void sendNotificationRecoverPassword(@NonNull RecoverPasswordEntity recoverPassword) {
        RecoverPasswordEventDTO event = RecoverPasswordEventDTO.builder()
                .eventId(Instant.now().toEpochMilli() + "_" + UUID.randomUUID())
                .type(EMAIL)
                .template(CHANGE_PASSWORD)
                .name(recoverPassword.getUser().getName())
                .email(recoverPassword.getUser().getEmail())
                .uuid(recoverPassword.getUser().getUuid())
                .pswdrst(recoverPassword.getPswdrst())
                .build();

        sendNotification(jsonUtil.toJson(event), emailTopic);
    }

}
