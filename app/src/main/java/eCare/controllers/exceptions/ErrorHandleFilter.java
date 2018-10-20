package eCare.controllers.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * Created by echerkas on 27.06.2018.
 */
public class ErrorHandleFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(ErrorHandleFilter.class);

    GlobalExceptionController globalExceptionController;

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        globalExceptionController = (GlobalExceptionController) WebApplicationContextUtils
                .getRequiredWebApplicationContext(filterConfig.getServletContext())
                .getBean("globalExceptionController");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        try {
            globalExceptionController.handleJspExceptions(servletRequest, servletResponse);
            filterChain.doFilter(servletRequest, servletResponse);

        } catch (Exception ex) {
            logger.error("Error : {}", ex);
            servletRequest.setAttribute("message", ex);
            servletRequest.setAttribute("loggedinuser", getPrincipal());
            servletRequest.getRequestDispatcher("/WEB-INF/views/jspError.jsp")
                    .forward(servletRequest, servletResponse);

        }

    }


    /**
     * This method returns the principal[user-name] of logged-in user.
     */
    private String getPrincipal() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

}
