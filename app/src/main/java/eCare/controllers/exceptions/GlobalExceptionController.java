package eCare.controllers.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by echerkas on 12.05.2018.
 */

@ControllerAdvice
@Component("globalExceptionController")
public class GlobalExceptionController {

    public static final String DEFAULT_ERROR_VIEW = "error";

    static final Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);

    @ExceptionHandler(SQLException.class)
    public String handleSQLException(HttpServletRequest request, Exception ex, Model model){
        logger.info("SQLException Occured:: URL="+request.getRequestURL() + ex);
        model.addAttribute("message", "SQLException Occured:: URL="+request.getRequestURL());
        return "error";
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="IOException occured")
    @ExceptionHandler(IOException.class)
    public String handleIOException(HttpServletRequest request, Model model, Exception ex){
        logger.error("IOException handler executed" + ex);
        model.addAttribute("message", "IOException Occured:: URL="+ request.getRequestURL());
        return "404ErrorCode";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String error404(Exception ex, Model model) {
        model.addAttribute("loggedinuser", getPrincipal());
        return "404ErrorCode";
    }

    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleAllException(HttpServletRequest req, Exception e) throws Exception {
        if (AnnotationUtils.findAnnotation
                (e.getClass(), ResponseStatus.class) != null)
            throw e;

        ModelAndView mav = new ModelAndView();

        mav.addObject("message", e);
        mav.addObject("loggedinuser", getPrincipal());
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("handlerException");
        return mav;
    }

    @ExceptionHandler(value = ServletException.class)
    public ModelAndView handleJspExceptions(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException{
        ModelAndView mav = new ModelAndView();
        mav.addObject("loggedinuser", getPrincipal());
        mav.setViewName("jspError");
        servletRequest.setAttribute("loggedinuser", getPrincipal());
        return mav;
    }

    /**
     * This method returns the principal[user-name] of logged-in user.
     */
    private String getPrincipal(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }
}
