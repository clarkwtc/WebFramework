package org.web.application;

import org.web.domain.User;
import org.web.domain.UserSystem;
import org.web.domain.exceptions.IncorrectFormatOfEmailException;

public class DomainService {
    private final UserSystem userSystem;
    public DomainService() {
        this.userSystem = new UserSystem();
    }

    public User registerUser(HTTPPOSTRequest httpPOSTRequest){
        validEmail(httpPOSTRequest.email, "Registration's format incorrect.");
        userSystem.register(httpPOSTRequest.email, httpPOSTRequest.name, httpPOSTRequest.password);
        return userSystem.query(httpPOSTRequest.name).get(0);
    }

    private void validEmail(String email, String exceptionMsg){
        if (!email.contains("@")){
            throw new IncorrectFormatOfEmailException(exceptionMsg);
        }
    }

    public void getUser(String name){
        userSystem.query();
    }

    public User login(HTTPLoginRequest httpLoginRequest) {
        validEmail(httpLoginRequest.email, "Login's format incorrect.");
       return userSystem.login(httpLoginRequest.email, httpLoginRequest.password);
    }
}
