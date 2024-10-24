package com.albert.supportbackend.controller;

import com.albert.supportbackend.errors.ErrorsPresentation;
import com.albert.supportbackend.model.Role;
import com.albert.supportbackend.model.User;
import com.albert.supportbackend.repository.UserRepository;
import com.albert.supportbackend.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(path = "/api/users")
public class UserRestController {

    private final UserService userService;

    private final MessageSource messageSource;
    private final UserRepository userRepository;

    public UserRestController(UserService userService, MessageSource messageSource, UserRepository userRepository) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.findAll());
    }

    @GetMapping(params = "name")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> getAllUsersByName(@RequestParam("name") String name) {
        List<User> users = userService.findAllByName(name);
        if (users.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(users);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> grantOperatorRole(@PathVariable Long id, Locale locale) {
        User user = userService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (user.getRoles().contains(Role.OPERATOR)) {
            final String message = messageSource.getMessage("user.grant.errors.already_operator",
                    new Object[]{}, locale);
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorsPresentation(
                            Collections.singletonList(message)));
        } else {
            user.getRoles().add(Role.OPERATOR);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userRepository.save(user));
        }
    }
}
