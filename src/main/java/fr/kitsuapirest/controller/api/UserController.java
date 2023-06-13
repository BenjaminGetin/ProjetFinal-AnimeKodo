package fr.kitsuapirest.controller.api;

import fr.kitsuapirest.dto.UserChangePwdForm;
import fr.kitsuapirest.dto.UserLoginForm;
import fr.kitsuapirest.dto.UserRegistrationForm;
import fr.kitsuapirest.model.Anime;
import fr.kitsuapirest.model.User;
import fr.kitsuapirest.service.RatingService;
import fr.kitsuapirest.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * The UserController class handles API endpoints related to users.
 */
@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RatingService ratingService;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder, RatingService ratingService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.ratingService = ratingService;
    }

    /**
     * Retrieves all users.
     *
     * @return a ResponseEntity containing a list of User objects if successful.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve.
     * @return a ResponseEntity containing the User object if found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Creates a new user.
     *
     * @param userForm the UserRegistrationForm object containing the user details.
     * @return a ResponseEntity containing the created User object if successful, along with HttpStatus.CREATED.
     */
    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody @Valid UserRegistrationForm userForm) {
        User newUser = new User();
        newUser.setFirstname(userForm.getFirstname());
        newUser.setLastname(userForm.getLastname());
        newUser.setUsername(userForm.getUsername());
        newUser.setEmail(userForm.getEmail());
        newUser.setPassword(userForm.getPassword());

        User createdUser = userService.createUser(newUser);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Authenticates a user using their username and password.
     *
     * @param userForm the UserLoginForm object containing the user's login credentials.
     * @param session  the HttpSession object for storing session information.
     * @return a ResponseEntity containing the authenticated User object if successful.
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody @Valid UserLoginForm userForm, HttpSession session) {
        String username = userForm.getUsername();
        String password = userForm.getPassword();

        Optional<User> authenticatedUser = userService.authenticate(username, password);

        if (authenticatedUser.isPresent()) {
            User user = authenticatedUser.get();
            session.setAttribute("userId", user.getId());
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Changes the password of a user.
     *
     * @param id           the ID of the user.
     * @param changePwdForm the UserChangePwdForm object containing the password change details.
     * @return a ResponseEntity indicating a successful password change.
     */
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Integer id, @RequestBody @Valid UserChangePwdForm changePwdForm) {
        String password = changePwdForm.getPassword();
        String newPassword = changePwdForm.getNewPassword();
        String confirmPassword = changePwdForm.getConfirmPassword();

        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        userService.changePassword(id, password, newPassword);

        return ResponseEntity.ok().build();
    }

    /**
     * Updates an existing user.
     *
     * @param id   the ID of the user to update.
     * @param user the updated User object.
     * @return a ResponseEntity containing the updated User object if successful.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody @Valid User user) {
        User updatedUser = userService.updateUser(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Deletes a user.
     *
     * @param id the ID of the user to delete.
     * @return a ResponseEntity indicating a successful deletion with HttpStatus.NO_CONTENT.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Retrieves the top rated animes.
     *
     * @param principal the authenticated principal representing the user.
     * @return a ResponseEntity containing a list of Anime objects representing the top rated animes.
     */
    @GetMapping("/top-rated")
    public ResponseEntity<List<Anime>> getTopRatedAnimes(Principal principal) {
        List<Anime> topRatedAnimes = ratingService.getTopRatedAnimes(principal);
        return ResponseEntity.ok(topRatedAnimes);
    }
}
