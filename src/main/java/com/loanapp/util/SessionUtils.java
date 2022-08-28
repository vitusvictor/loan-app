package com.loanapp.util;

import com.loanapp.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Component
public class SessionUtils {
    private final HttpSession httpSession;

    public Long getLoggedInUserId(){
        Long userId = (Long) httpSession.getAttribute("user_id");
        if (userId == null) {
            throw new UserNotFoundException("You are not yet logged in!");
        }
        return userId;
    }

}
