package fr.kitsuapirest.controller;

import fr.kitsuapirest.dto.PasswordForm;
import fr.kitsuapirest.dto.UserForm;
import fr.kitsuapirest.entity.RoleEntity;
import fr.kitsuapirest.entity.UserEntity;
import fr.kitsuapirest.exception.UnauthorizedException;
import fr.kitsuapirest.service.RoleService;
import fr.kitsuapirest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    private UserEntity convertToUserEntity(UserForm userForm) {
        UserEntity user = new UserEntity();
        user.setUsername(userForm.getLogin());
        user.setPassword(userForm.getPassword());
        // Autres propriétés de l'utilisateur à définir si nécessaire
        return user;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserForm userForm) {
        UserEntity user = convertToUserEntity(userForm);
        Set<RoleEntity> roles = roleService.getRolesByName(userForm.getRoles());
        user.setRoles(roles);
        userService.createUser(user);
        return ResponseEntity.ok("User created successfully");
    }

    @PostMapping("/{userId}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long userId, @RequestBody @Valid PasswordForm passwordForm) {
        UserEntity user = userService.getUserById(userId);
        boolean isPasswordCorrect = userService.checkPassword(user, passwordForm.getCurrentPassword());
        if (isPasswordCorrect) {
            userService.changePassword(user, passwordForm.getNewPassword());
            return ResponseEntity.ok("Password changed successfully");
        } else {
            throw new UnauthorizedException("Incorrect password");
        }
    }
}

