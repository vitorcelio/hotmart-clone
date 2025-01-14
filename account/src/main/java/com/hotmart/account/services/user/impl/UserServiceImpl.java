package com.hotmart.account.services.user.impl;

import com.hotmart.account.config.exception.ValidationException;
import com.hotmart.account.dto.event.AccessDataEventDTO;
import com.hotmart.account.dto.event.HistoryDTO;
import com.hotmart.account.dto.event.OrderEventDTO;
import com.hotmart.account.dto.event.RecoverPasswordEventDTO;
import com.hotmart.account.dto.request.UserRequestDTO;
import com.hotmart.account.dto.request.UserUpdatePasswordRequestDTO;
import com.hotmart.account.dto.request.UserUpdateRequestDTO;
import com.hotmart.account.dto.response.UserResponseDTO;
import com.hotmart.account.enums.Roles;
import com.hotmart.account.models.DataAccess;
import com.hotmart.account.models.RecoverPassword;
import com.hotmart.account.models.Role;
import com.hotmart.account.models.User;
import com.hotmart.account.producer.KafkaProducer;
import com.hotmart.account.repositories.DataAccessRepository;
import com.hotmart.account.repositories.UserRepository;
import com.hotmart.account.services.user.UserService;
import com.hotmart.account.utils.AccountUtils;
import com.hotmart.account.utils.JsonUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.hotmart.account.enums.EventType.EMAIL;
import static com.hotmart.account.enums.SagaStatus.ROLLBACK;
import static com.hotmart.account.enums.SagaStatus.SUCCESS;
import static com.hotmart.account.enums.TemplateType.CHANGE_PASSWORD;
import static com.hotmart.account.enums.TemplateType.DATA_ACCESS;

@Slf4j
@RequiredArgsConstructor
@Service("myUserServiceImpl")
public class UserServiceImpl implements UserService {

    private static final String CURRENT_SOURCE = "USER_SERVICE";
    private static final String USER_ALREADY_EXISTS = "Usuário com :x :y já cadastrado!";
    private static final String USER_NOT_FOUND = "Usuário não encontrado!";

    private final UserRepository userRepository;
    private final DataAccessRepository dataAccessRepository;
    private final JsonUtil jsonUtil;
    private final KafkaProducer producer;

    @Value("${spring.kafka.topic.email}")
    private String emailTopic;

    @Value("${spring.kafka.topic.orchestrator}")
    private String orchestrator;

    @Override
    public void createUserSaga(@NonNull OrderEventDTO event) {
        try {
            validationUser(event);
            createUser(event);
            handleSuccess(event);
        } catch (Exception e) {
            log.error("Erro na validação do usuário: ", e);
            handleFail(event, e.getMessage());
        }

        sendOrchestratorSaga(event);
    }

    @Override
    public UserResponseDTO save(@NonNull UserRequestDTO request) {
        validationUserAlreadyExistsByEmail(request.getEmail());
        return createUser(request);
    }

    @Override
    public UserResponseDTO update(@NonNull Long id, @NonNull UserUpdateRequestDTO request) {
        validationUpdateUser(id, request);
        return updateUser(id, request);
    }

    @Override
    public void updatePassword(@NonNull Long id, @NonNull UserUpdatePasswordRequestDTO request) {
        validationUpdatePassword(id, request);
        updatePasswordUser(id, request);
    }

    @Override
    public void closeAccount(@NonNull Long id) {
        // Verificar cobranças ou comissões pendentes
        // Verificar saldo da conta
        // Produtos ativos
        closeUserAccount(id);
        // Enviar solicitação por email
    }

    @Override
    public void dataAccessRequestByEmail(@NonNull Long id) {
        DataAccess dataAccess = createDataAccess(id);
        sendNotificationAccessData(dataAccess);
    }

    @Override
    public UserResponseDTO findByEmail(@NonNull String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ValidationException(USER_NOT_FOUND));
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .cpfCnpj(user.getCpfCnpj())
                .website(user.getWebsite())
                .description(user.getDescription())
                .birthDate(user.getBirthDate())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .roles(getRolesString(user.getRoles()))
                .build();
    }

    @Override
    public UserResponseDTO findById(@NonNull Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ValidationException(USER_NOT_FOUND));

        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .cpfCnpj(user.getCpfCnpj())
                .website(user.getWebsite())
                .description(user.getDescription())
                .birthDate(user.getBirthDate())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .roles(getRolesString(user.getRoles()))
                .build();
    }

    @Override
    public List<UserResponseDTO> findAll() {
        return UserResponseDTO.convert(userRepository.findAll());
    }

    private List<String> getRolesString(Set<Role> rolesUser) {
        List<String> rolesList = null;
        if (!ObjectUtils.isEmpty(rolesUser)) {
            rolesList = rolesUser.stream().map(Role::getName).toList();
        }

        return rolesList;
    }

    private UserResponseDTO createUser(@NonNull UserRequestDTO request) {

        Set<Role> roles = Set.of(Role.builder()
                .id(Roles.valueOf(request.getRole()).getId())
                .name(Roles.valueOf(request.getRole()).name())
                .build());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(roles)
                .cpfCnpj(request.getCpfCnpj())
                .phone(request.getPhone())
                .build();

        User userSave = userRepository.save(user);

        return UserResponseDTO.builder()
                .id(userSave.getId())
                .name(userSave.getName())
                .email(userSave.getEmail())
                .createdAt(userSave.getCreatedAt())
                .updatedAt(userSave.getUpdatedAt())
                .roles(getRolesString(user.getRoles()))
                .build();
    }

    private UserResponseDTO updateUser(@NonNull Long id, @NonNull UserUpdateRequestDTO request) {
        User user = userRepository.findById(id).orElseThrow(() -> new ValidationException(USER_NOT_FOUND));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setCpfCnpj(request.getCpfCnpj());
        user.setWebsite(request.getWebsite());
        user.setDescription(request.getDescription());
        user.setBirthDate(request.getBirthDate());

        User userUpdate = userRepository.save(user);

        return UserResponseDTO.builder()
                .id(userUpdate.getId())
                .name(userUpdate.getName())
                .email(userUpdate.getEmail())
                .phone(userUpdate.getPhone())
                .cpfCnpj(userUpdate.getCpfCnpj())
                .website(userUpdate.getWebsite())
                .description(userUpdate.getDescription())
                .birthDate(userUpdate.getBirthDate())
                .createdAt(userUpdate.getCreatedAt())
                .updatedAt(userUpdate.getUpdatedAt())
                .roles(getRolesString(user.getRoles()))
                .build();
    }

    private void updatePasswordUser(@NonNull Long id, @NonNull UserUpdatePasswordRequestDTO request) {
        User user = userRepository.findById(id).orElseThrow(() -> new ValidationException(USER_NOT_FOUND));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }

    private void closeUserAccount(@NonNull Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ValidationException(USER_NOT_FOUND));

        user.setDeletedAt(LocalDateTime.now());
        user.setEnabled(false);
        user.setAccountNonExpired(false);
        user.setAccountNonLocked(false);
        user.setCredentialsNonExpired(false);
        userRepository.save(user);
    }

    private DataAccess createDataAccess(@NonNull Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ValidationException(USER_NOT_FOUND));

        DataAccess dataAccess = DataAccess.builder()
                .user(user)
                .build();

        dataAccessRepository.save(dataAccess);

        return dataAccess;
    }

    private void validationUserAlreadyExistsByEmail(@NonNull String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            log.error("User with email {} already exists", email);
            throw new ValidationException(USER_ALREADY_EXISTS.replaceAll(":x", "email").replaceAll(":y", email));
        }
    }

    private void validationUserAlreadyExistsByCpfCnpj(@NonNull String cpfCnpj) {
        if (userRepository.findByCpfCnpj(cpfCnpj).isPresent()) {
            log.error("User with Cpf/Cnpj {} already exists", cpfCnpj);
            throw new ValidationException(USER_ALREADY_EXISTS.replaceAll(":x", "Cpf/Cnpj").replaceAll(":y", cpfCnpj));
        }
    }

    private void validationUpdateUser(@NonNull Long id, @NonNull UserUpdateRequestDTO request) {
        log.info("Validating update user: {}", request);
        User user = userRepository.findById(id).orElseThrow(() -> new ValidationException(USER_NOT_FOUND));

        if (!user.getEmail().equals(request.getEmail())) {
            log.error("User with email {} does not match with email {} in request", user.getEmail(),
                    request.getEmail());

            validationUserAlreadyExistsByEmail(request.getEmail());
        }

        if (request.getCpfCnpj() != null && !user.getCpfCnpj().equals(request.getCpfCnpj())) {
            validationUserAlreadyExistsByCpfCnpj(request.getCpfCnpj());
        }
    }

    private void validationUpdatePassword(@NonNull Long id, @NonNull UserUpdatePasswordRequestDTO request) {
        User user = userRepository.findById(id).orElseThrow(() -> new ValidationException(USER_NOT_FOUND));
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            log.info("Password does not match with password {}", request.getPassword());
            throw new ValidationException("A senha está incorreta!");
        }
    }

    private void validationUser(@NonNull OrderEventDTO event) {
        if (ObjectUtils.isEmpty(event.getOrder().getBuyer().getEmail())) {
            throw new ValidationException("Email não foi informado");
        }

        if (ObjectUtils.isEmpty(event.getOrder().getBuyer().getCpfCnpj())) {}
    }

    private void createUser(@NonNull OrderEventDTO event) {
        Optional<User> user = userRepository.findByEmail(event.getOrder().getBuyer().getEmail());

        if (user.isPresent()) {
            User userEmail = user.get();
            event.getOrder().getBuyer().setUserId(userEmail.getId());
        } else {
            log.info("Usuário não existe, será cadastrado com email {}", event.getOrder().getBuyer().getEmail());

            var nameSeparate = event.getOrder().getBuyer().getName().split(" ");
            String password = AccountUtils.accentRemover(
                    nameSeparate.length > 1 ?
                            String.format("@A1b2c3_%s_%s", nameSeparate[0], nameSeparate[1]) :
                            String.format("@A1b2c3_%s", nameSeparate[0])
            );

            var request = UserRequestDTO.builder()
                    .email(event.getOrder().getBuyer().getEmail())
                    .name(event.getOrder().getBuyer().getName())
                    .password(new BCryptPasswordEncoder().encode(password))
                    .role(Roles.BUYER.name())
                    .cpfCnpj(event.getOrder().getBuyer().getCpfCnpj())
                    .phone(event.getOrder().getBuyer().getPhone())
                    .build();

            UserResponseDTO userEmail = save(request);
            event.getOrder().getBuyer().setUserId(userEmail.getId());
        }

    }

    private void sendNotification(@NonNull String event, @NonNull String topic) {
        producer.sendEvent(event, topic);
    }

    private void sendNotificationAccessData(@NonNull DataAccess dataAccess) {
        User user = dataAccess.getUser();

        AccessDataEventDTO event = AccessDataEventDTO.builder()
                .eventId(Instant.now().toEpochMilli() + "_" + UUID.randomUUID())
                .type(EMAIL)
                .template(DATA_ACCESS)
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .uuidAccessData(dataAccess.getId().toString())
                .build();

        sendNotification(jsonUtil.toJson(event), emailTopic);
    }

    private void sendNotificationRecoverPassword(@NonNull RecoverPassword recoverPassword) {
        RecoverPasswordEventDTO event = RecoverPasswordEventDTO.builder()
                .eventId(Instant.now().toEpochMilli() + "_" + UUID.randomUUID())
                .type(EMAIL)
                .template(CHANGE_PASSWORD)
                .userId(recoverPassword.getUser().getId())
                .name(recoverPassword.getUser().getName())
                .email(recoverPassword.getUser().getEmail())
                .uuid(recoverPassword.getUser().getUuid())
                .pswdrst(recoverPassword.getPswdrst())
                .build();

        sendNotification(jsonUtil.toJson(event), emailTopic);
    }

    private void addHistory(@NonNull OrderEventDTO event, @NonNull String message) {
        var history = HistoryDTO.builder()
                .source(event.getSource())
                .status(event.getStatus())
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();

        event.addHistory(history);
    }

    private void handleFail(@NonNull OrderEventDTO event, @NonNull String message) {
        event.setStatus(ROLLBACK);
        event.setSource(CURRENT_SOURCE);
        addHistory(event, "Falha ao validar usuário: ".concat(message));
    }

    private void handleSuccess(@NonNull OrderEventDTO event) {
        event.setStatus(SUCCESS);
        event.setSource(CURRENT_SOURCE);
        addHistory(event, "Usuário validado com sucesso");
    }

    private void sendOrchestratorSaga(@NonNull OrderEventDTO event) {
        String payload = jsonUtil.toJson(event);
        producer.sendEvent(payload, orchestrator);
    }

}
