package com.umut.project.database.mywebservice.user;

import com.umut.project.database.mywebservice.common.PageResponse;
import com.umut.project.database.mywebservice.email.EmailService;
import com.umut.project.database.mywebservice.exception.ActivateTokenNotFoundException;
import com.umut.project.database.mywebservice.exception.FieldValidationException;
import com.umut.project.database.mywebservice.exception.MailDeliveryException;
import com.umut.project.database.mywebservice.exception.UserNotFoundException;
import com.umut.project.database.mywebservice.file.FileService;
import com.umut.project.database.mywebservice.token.RefreshTokenService;
import com.umut.project.database.mywebservice.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final EmailService emailService;
    private final FileService fileService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public UserDto.MessageResponse register(UserDto.UserRegisterRequest request)
    {
        User user = toEntity(request);

        User savedUser = userRepository.save(user);
        try {
            emailService.sendMail(savedUser.getEmail(), savedUser.getActivationToken());
        }
        catch (Exception exception)
        {
            log.error("Aktivasyon maili gonderilemedi: {}", savedUser.getEmail(), exception);
            throw new MailDeliveryException();
        }

        return toMessageResponse("user.register.success");
    }

    @Transactional
    public UserDto.MessageResponse activate(String token)
    {
        User user = userRepository.findByActivationToken(token)
                .orElseThrow(ActivateTokenNotFoundException::new);
        user.setActivate(true);
        user.setActivationToken(null);
        return toMessageResponse("user.activation.success");
    }

    @Transactional(readOnly = true)
    public PageResponse<UserDto.UserDetailsResponse> listUsers(Pageable pageable, Long currentUserId)
    {
        Page<User> users = currentUserId == null
                ? userRepository.findAll(pageable)
                : userRepository.findByIdNot(currentUserId, pageable);
        return PageResponse.from(users.map(this::toUserDetailsResponse));
    }

    @Transactional(readOnly = true)
    public UserDto.UserDetailsResponse getUser(Long userId)
    {
        return toUserDetailsResponse(findById(userId));
    }

    @Transactional
    public UserDto.UserDetailsResponse updateUser(Long id, UserDto.UserUpdateRequest request)
    {
        User user = findById(id);
        String newUsername = request.username().trim();

        if(userRepository.existsByUsernameAndIdNot(newUsername, id))
        {
            throw new FieldValidationException("username", "user.username.in.use");
        }

        user.setUsername(newUsername);
        return toUserDetailsResponse(user);
    }

    @Transactional
    public UserDto.UserDetailsResponse updateImage(Long id, MultipartFile image)
    {
        User user = findById(id);
        String oldImage = user.getImage();

        String newImage = fileService.saveProfileImage(image);
        user.setImage(newImage);
        userRepository.saveAndFlush(user);

        fileService.deleteProfileImage(oldImage);
        return toUserDetailsResponse(user);
    }

    @Transactional
    public UserDto.UserDetailsResponse deleteImage(Long id)
    {
        User user = findById(id);
        String oldImage = user.getImage();
        user.setImage(null);
        userRepository.saveAndFlush(user);

        fileService.deleteProfileImage(oldImage);
        return toUserDetailsResponse(user);
    }

    @Transactional
    public UserDto.MessageResponse updatePassword(Long id, UserDto.PasswordUpdateRequest request)
    {
        User user = findById(id);

        if(!passwordEncoder.matches(request.currentPassword(), user.getPassword()))
        {
            throw new FieldValidationException("currentPassword", "user.password.current.invalid");
        }
        if(passwordEncoder.matches(request.newPassword(), user.getPassword()))
        {
            throw new FieldValidationException("newPassword", "user.password.same");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        refreshTokenService.deleteByUserId(user.getId());

        return toMessageResponse("user.password.update.success");
    }

    private User findById(Long id)
    {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    private User toEntity(UserDto.UserRegisterRequest request)
    {
        User user = new User();
        user.setUsername(request.username().trim());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email().trim());
        user.setRole(Role.USER);
        return user;
    }

    private UserDto.UserDetailsResponse toUserDetailsResponse(User user)
    {
        return new UserDto.UserDetailsResponse(user.getId(), user.getUsername(), user.getEmail(), user.getImage());
    }

    private UserDto.MessageResponse toMessageResponse(String messageKey)
    {
        String message = messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
        return new UserDto.MessageResponse(message);
    }
}
