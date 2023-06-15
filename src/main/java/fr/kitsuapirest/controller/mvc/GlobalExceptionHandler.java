package fr.kitsuapirest.controller.mvc;

import fr.kitsuapirest.service.KitsuApiService;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * The GlobalExceptionHandler class handles exceptions globally for the MVC controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles general exceptions and maps them to the error page.
     *
     * @param model     the Model object to populate data for the view.
     * @param exception the Exception object representing the exception.
     * @return the name of the view template for the error page.
     */

    /**
     * Handles the AnimeAlreadyExistsException and maps it to the error page.
     *
     * @param model     the Model object to populate data for the view.
     * @param exception the AnimeAlreadyExistsException object representing the exception.
     * @return the name of the view template for the error page.
     */
    @ExceptionHandler(KitsuApiService.AnimeAlreadyExistsException.class)
    public String handleAnimeAlreadyExistsException(Model model, Exception exception) {
        model.addAttribute("errorMessage", exception.getMessage());
        return "error";
    }

    /**
     * Handles the AnimeNotFoundException and maps it to the error page.
     *
     * @param model     the Model object to populate data for the view.
     * @param exception the AnimeNotFoundException object representing the exception.
     * @return the name of the view template for the error page.
     */
    @ExceptionHandler(AnimeDetailsController.AnimeNotFoundException.class)
    public String handleAnimeNotFoundException(Model model, AnimeDetailsController.AnimeNotFoundException exception) {
        model.addAttribute("errorMessage", "Anime not found");
        return "error";
    }
}
