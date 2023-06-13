package fr.kitsuapirest.service;

import fr.kitsuapirest.exception.NotFoundException;
import fr.kitsuapirest.model.Role;
import fr.kitsuapirest.model.User;
import fr.kitsuapirest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The UserService class handles operations related to users.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the ID of the user.
     * @return the user with the specified ID.
     * @throws NotFoundException if the user is not found.
     */
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
    }

    /**
     * Creates a new user.
     *
     * @param user the user to create.
     * @return the created user.
     */
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by username.
     *
     * @param username the username of the user.
     * @return the user with the specified username.
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Retrieves the ID of a user by username.
     *
     * @param username the username of the user.
     * @return the ID of the user.
     */
    public Integer getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user.getId();
    }

    /**
     * Updates a user.
     *
     * @param id   the ID of the user.
     * @param user the updated user.
     * @return the updated user.
     * @throws NotFoundException if the user is not found.
     */
    public User updateUser(Integer id, User user) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        // Set other user attributes if necessary
        return userRepository.save(existingUser);
    }

    /**
     * Authenticates a user with the specified username and password.
     *
     * @param username the username of the user.
     * @param password the password of the user.
     * @return an optional containing the authenticated user if authentication succeeds, or an empty optional if authentication fails.
     */
    public Optional<User> authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return Optional.of(user);
        }

        return Optional.empty();
    }

    /**
     * Changes the password of a user.
     *
     * @param id              the ID of the user.
     * @param currentPassword the current password of the user.
     * @param newPassword     the new password for the user.
     * @throws NotFoundException       if the user is not found.
     * @throws IllegalArgumentException if the current password is invalid.
     */
    public void changePassword(Integer id, String currentPassword, String newPassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        // Check if the current password matches
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid current password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Deletes a user.
     *
     * @param id the ID of the user.
     * @throws NotFoundException if the user is not found.
     */
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.delete(user);
    }

    /**
     * Loads a user by username.
     *
     * @param username the username of the user.
     * @return a UserDetails object representing the user.
     * @throws UsernameNotFoundException if the user is not found.
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Construct and return a UserDetails instance based on the user information
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                getAuthorities(user.getRole())
        );
    }

    /**
     * Utility method to retrieve user authorities as a collection of GrantedAuthority.
     *
     * @param role the role of the user.
     * @return a collection of GrantedAuthority representing the user authorities.
     */
    private Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }
}
