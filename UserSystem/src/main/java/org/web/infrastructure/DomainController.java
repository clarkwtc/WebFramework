package org.web.infrastructure;


import org.web.application.*;
import org.web.domain.User;
import org.web.domain.core.HTTPRequest;
import org.web.domain.core.HTTPResponse;
import org.web.domain.exceptions.ForbiddenException;
import org.web.domain.exceptions.IllegalAuthenticationException;
import org.web.domain.exceptions.InvalidNameFormatException;

import java.util.*;

public class DomainController {
    public DomainService domainService;
    public Map<String, User> tokens;

    public DomainController(DomainService domainService) {
        this.domainService = domainService;
        this.tokens = new HashMap<>();
    }

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }

    public DomainService getDomainService() {
        return domainService;
    }

    public Map<String, User> getTokens() {
        return tokens;
    }

    public void setTokens(Map<String, User> tokens) {
        this.tokens = tokens;
    }

    public HTTPResponse registerUser(HTTPRequest httpRequest) {
        HTTPPOSTRequest httpPOSTRequest = httpRequest.readBodyAsObject(HTTPPOSTRequest.class);

        User user = domainService.registerUser(httpPOSTRequest);

        HTTPResponse httpResponse = new HTTPResponse(201);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("content-encoding", "UTF-8");
        httpResponse.setHttpHeaders(headers);
        httpResponse.setBody(new HTTPPOSTResponse(user.getId(), user.getEmail(), user.getName()));
        return httpResponse;
    }

    public HTTPResponse login(HTTPRequest httpRequest) {
        HTTPLoginRequest httpLoginRequest = httpRequest.readBodyAsObject(HTTPLoginRequest.class);

        User user = domainService.login(httpLoginRequest);
        String token = String.valueOf(UUID.randomUUID());
        tokens.put(token, user);

        HTTPResponse httpResponse = new HTTPResponse(200);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("content-encoding", "UTF-8");
        httpResponse.setHttpHeaders(headers);
        httpResponse.setBody(new HTTPLoginResponse(user.getId(), user.getEmail(), user.getName(), token));
        return httpResponse;
    }

    public static class HTTPRenameRequest{
        public int id;
        public String newName;
    }


    public HTTPResponse rename(HTTPRequest httpRequest) {
        String authorization = httpRequest.getHttpHeaders().get("Authorization");
        Map<String, Object> pathVariable = httpRequest.getHttpPath().getPathVariable();
        int userId = Integer.parseInt(pathVariable.get("userId").toString());
        HTTPRenameRequest httpRenameRequest = httpRequest.readBodyAsObject(HTTPRenameRequest.class);
        httpRenameRequest.id = userId;
        validToken(authorization);
        validPermission(authorization, userId);

        try {
            domainService.rename(httpRenameRequest);
        }catch (IllegalArgumentException exception){
            throw new InvalidNameFormatException("Name's format invalid.");
        }

        return new HTTPResponse(204);
    }

    private void validToken(String authorization){
        String token = parseToken(authorization);
        if (!tokens.containsKey(token)){
            throw new IllegalAuthenticationException("Can't authenticate who you are.");
        }
    }

    private void validPermission(String authorization, int userId) {
        User user = getUserByAuthorization(authorization);
        if (user.getId() != userId){
            throw new ForbiddenException("Forbidden");
        }
    }

    private User getUserByAuthorization(String authorization){
        return tokens.get(parseToken(authorization));
    }

    private String parseToken(String authorization){
        return authorization.replace("Bearer ", "").strip();
    }

    public HTTPResponse get(HTTPRequest httpRequest) {
        HTTPResponse httpResponse = new HTTPResponse(200);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("content-encoding", "UTF-8");
        httpResponse.setHttpHeaders(headers);

        List<Map<String, Object>> body = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("id", 1);
        item.put("email", "abc@gmail.com");
        item.put("name", "abc");
        body.add(item);
        httpResponse.setBody(body);
        return httpResponse;
    }
}
