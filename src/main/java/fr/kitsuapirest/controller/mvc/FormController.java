package fr.kitsuapirest.controller.mvc;

import fr.kitsuapirest.dto.UserChangePwdForm;
import fr.kitsuapirest.dto.UserLoginForm;
import fr.kitsuapirest.dto.UserRegistrationForm;
import fr.kitsuapirest.model.User;
import fr.kitsuapirest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * The FormController class handles the MVC endpoints for login, signup, and change password forms.
 */
@Controller
public class FormController {

    private final RestTemplate restTemplate;
    private final UserService userService;

    public FormController(RestTemplate restTemplate, UserService userService) {
        this.restTemplate = restTemplate;
        this.userService = userService;
    }

    /**
     * Displays the login form.
     *
     * @return the name of the view template for the login form.
     */
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    /**
     * Processes the login form submission.
     *
     * @param userForm the UserLoginForm object containing the login details.
     * @param model    the Model object to populate data for the view.
     * @return a redirect URL to the dashboard page on successful login, or the login page with an error message on failed login.
     */
    @PostMapping("/login")
    public String login(@ModelAttribute("user") UserLoginForm userForm, Model model) {
        String apiUrl = "http://localhost:8080/api/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> data = new HashMap<>();
        data.put("username", userForm.getUsername());
        data.put("password", userForm.getPassword());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(data, headers);

        try {
            ResponseEntity<User> response = restTemplate.postForEntity(apiUrl, request, User.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                User authenticatedUser = response.getBody();
                model.addAttribute("user", authenticatedUser);
                return "redirect:/dashboard";
            } else {
                model.addAttribute("error", "Authentication error");
                return "login";
            }
        } catch (RestClientException e) {
            model.addAttribute("error", "An error occurred during authentication");
            return "login";
        }
    }

    /**
     * Displays the change password form.
     *
     * @return the name of the view template for the change password form.
     */
    @GetMapping("/change-password")
    public String showChangePasswordForm() {
        return "change-password";
    }

    /**
     * Processes the change password form submission.
     *
     * @param changePwdForm the UserChangePwdForm object containing the change password details.
     * @param model         the Model object to populate data for the view.
     * @param principal     the authenticated principal representing the user.
     * @return a redirect URL to the dashboard page on successful password change, or the change password page with an error message on failure.
     */
    @PostMapping("/change-password")
    public String changePassword(@Valid UserChangePwdForm changePwdForm, Model model, Principal principal) {
        String username = principal.getName();
        Integer userId = userService.getUserIdByUsername(username);

        String apiUrl = "http://localhost:8080/api/users/" + userId + "/change-password";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> data = new HashMap<>();
        data.put("password", changePwdForm.getPassword());
        data.put("newPassword", changePwdForm.getNewPassword());
        data.put("confirmPassword", changePwdForm.getConfirmPassword());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(data, headers);

        try {
            restTemplate.postForEntity(apiUrl, request, Void.class);
            return "redirect:/login";
        } catch (RestClientException e) {
            model.addAttribute("error", "An error occurred during password change");
            return "change-password";
        }
    }

    /**
     * Displays the signup form.
     *
     * @param model the Model object to populate data for the view.
     * @return the name of the view template for the signup form.
     */
    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("userForm", new UserRegistrationForm());

        return "signup";
    }

    /**
     * Processes the signup form submission.
     *
     * @param userForm the UserRegistrationForm object containing the user registration details.
     * @return a redirect URL to the login page on successful signup.
     */
    @PostMapping("/signup")
    public String signUpUser(@ModelAttribute("user") @Valid UserRegistrationForm userForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // S'il y a des erreurs de validation, retourner le modèle avec les erreurs
            return "signup"; // Ou le nom de votre modèle Thymeleaf pour la page d'inscription
        }


        String apiUrl = "http://localhost:8080/api/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        User newUser = new User();
        newUser.setFirstname(userForm.getFirstname());
        newUser.setLastname(userForm.getLastname());
        newUser.setUsername(userForm.getUsername());
        newUser.setEmail(userForm.getEmail());
        newUser.setPassword(userForm.getPassword());

        HttpEntity<User> request = new HttpEntity<>(newUser, headers);

        try {
            restTemplate.postForEntity(apiUrl, request, Void.class);
            return "redirect:/login";
        } catch (RestClientException e) {
            model.addAttribute("error", "An error occurred during account creation");
            return "signup"; // Ou le nom de votre modèle Thymeleaf pour la page d'inscription
        }
    }

}
