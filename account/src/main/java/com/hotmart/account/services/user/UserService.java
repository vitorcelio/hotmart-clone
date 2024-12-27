package com.hotmart.account.services.user;

import com.hotmart.account.dto.request.UserRequestDTO;
import com.hotmart.account.dto.request.UserUpdatePasswordRequestDTO;
import com.hotmart.account.dto.request.UserUpdateRequestDTO;
import com.hotmart.account.dto.response.UserResponseDTO;
import lombok.NonNull;

import java.util.List;

public interface UserService {

    UserResponseDTO save(@NonNull final UserRequestDTO request);

    UserResponseDTO update(@NonNull Long id, @NonNull final UserUpdateRequestDTO request);

    void updatePassword(@NonNull Long id, @NonNull final UserUpdatePasswordRequestDTO request);

    void closeAccount(@NonNull Long id);

    void dataAccessRequestByEmail(@NonNull Long id);

    UserResponseDTO findByEmail(@NonNull String email);

    UserResponseDTO findById(@NonNull Long id);

    List<UserResponseDTO> findAll();

}
