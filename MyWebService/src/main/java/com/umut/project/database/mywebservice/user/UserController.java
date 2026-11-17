package com.umut.project.database.mywebservice.user;

import com.umut.project.database.mywebservice.common.PageResponse;
import com.umut.project.database.mywebservice.exception.ForbiddenException;
import com.umut.project.database.mywebservice.security.CurrentUser;
import com.umut.project.database.mywebservice.user.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto.MessageResponse> register(@Valid @RequestBody UserDto.UserRegisterRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request));
    }

    @PatchMapping("/{token}/activate")
    public ResponseEntity<UserDto.MessageResponse> activate(@PathVariable String token)
    {
        return ResponseEntity.ok(userService.activate(token));
    }

    @GetMapping
    public ResponseEntity<PageResponse<UserDto.UserDetailsResponse>> listUsers(
            @PageableDefault(size = 5) Pageable pageable,
            @AuthenticationPrincipal CurrentUser currentUser)
    {
        Long currentUserId = currentUser == null ? null : currentUser.getId();
        return ResponseEntity.ok(userService.listUsers(pageable, currentUserId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto.UserDetailsResponse> getUser(@PathVariable("id") Long userId)
    {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto.UserDetailsResponse> updateUser(@PathVariable("id") Long userId,
                                                                  @Valid @RequestBody UserDto.UserUpdateRequest request,
                                                                  @AuthenticationPrincipal CurrentUser currentUser)
    {
        checkOwnership(userId, currentUser);
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto.UserDetailsResponse> updateImage(@PathVariable("id") Long userId,
                                                                   @RequestPart("image") MultipartFile image,
                                                                   @AuthenticationPrincipal CurrentUser currentUser)
    {
        checkOwnership(userId, currentUser);
        return ResponseEntity.ok(userService.updateImage(userId, image));
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<UserDto.UserDetailsResponse> deleteImage(@PathVariable("id") Long userId,
                                                                   @AuthenticationPrincipal CurrentUser currentUser)
    {
        checkOwnership(userId, currentUser);
        return ResponseEntity.ok(userService.deleteImage(userId));
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<UserDto.MessageResponse> updatePassword(@PathVariable("id") Long userId,
                                                                  @Valid @RequestBody UserDto.PasswordUpdateRequest request,
                                                                  @AuthenticationPrincipal CurrentUser currentUser)
    {
        checkOwnership(userId, currentUser);
        return ResponseEntity.ok(userService.updatePassword(userId, request));
    }

    private void checkOwnership(Long userId, CurrentUser currentUser)
    {
        if(currentUser == null || !currentUser.getId().equals(userId))
        {
            throw new ForbiddenException();
        }
    }
}