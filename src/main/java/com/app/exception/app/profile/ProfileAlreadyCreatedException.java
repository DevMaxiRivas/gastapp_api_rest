package com.app.exception.app.profile;

import com.app.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ProfileAlreadyCreatedException extends BaseException {

    public ProfileAlreadyCreatedException() {
        super(
                "Profile Already Exists",
                "CONFLICT",
                HttpStatus.CONFLICT,
                "Profile could not be created",
                "body"
        );
    }
}
