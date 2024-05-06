package com.example.wallpapers.exception;

import com.example.wallpapers.enums.AuthStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Getter
public class AuthException extends AuthenticationException {
    private AuthStatus authStatus;
    private HttpStatus httpStatus;

    /**
     * Put to the message a authStatus.toString()
     * @param authStatus custom authentication status
     * @param httpStatus http status
     */
    public AuthException(AuthStatus authStatus, HttpStatus httpStatus) {
        super(authStatus.name());
        this.authStatus = authStatus;
        this.httpStatus = httpStatus;
    }

    /**
     * Put to the message a string
     * @param authStatus custom authentication status
     * @param httpStatus http status
     */
    public AuthException(AuthStatus authStatus, HttpStatus httpStatus, String message) {
        super(message);
        this.authStatus = authStatus;
        this.httpStatus = httpStatus;
    }

    public AuthException(String error) {
        super(error);
    }
}
