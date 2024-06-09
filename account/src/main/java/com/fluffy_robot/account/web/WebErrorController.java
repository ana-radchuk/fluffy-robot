package com.fluffy_robot.account.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class WebErrorController {

    @GetMapping
    public String getError(HttpServletRequest request, Object model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            String text = "";
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                text = "the page you are looking for cannot be found";
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                text = "you are not authorized to the page you are requesting";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                text = "you do not have permissions for this page";
            } else {
                text = "something weng wrong :(";
            }
//            model.addAttribute("errorText", text);
//            model.addAttribute("errorCode", statusCode);
        } else {
//            model.addAttribute("errorText", "An unknown error has occurred.");
//            model.addAttribute("errorCode", "UNKNOWN");
        }
        return "error";
    }

}
