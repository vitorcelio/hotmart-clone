package com.hotmart.auth.services.user;

import com.hotmart.auth.dto.request.ChangePasswordRequestDTO;
import com.hotmart.auth.dto.request.RecoveryPasswordRequestDTO;
import com.hotmart.auth.dto.request.UserRequestDTO;
import lombok.NonNull;

public interface UserService {

    void save(@NonNull final UserRequestDTO request);

    void recovery(@NonNull final RecoveryPasswordRequestDTO request);

    void updatePassword(@NonNull final ChangePasswordRequestDTO request, @NonNull String pswdrst, @NonNull String uuid);

    void validationTokenUser(@NonNull String pswdrst, @NonNull String uuid);

}
