package mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.web.domain.core.HTTPHandler;
import org.web.domain.core.HTTPMethod;
import org.web.domain.core.HTTPRequest;
import org.web.domain.core.HTTPResponse;
import org.web.infrastructure.FileUtil;

public class DomainController extends HTTPHandler {
    @Override
    public HTTPResponse handle(HTTPRequest httpRequest) {
        try {
            if (httpRequest.getHttpPath().comparePath("/api/users")) {
                if (httpRequest.getHttpMethod().equals(HTTPMethod.POST)) {
                    return post(httpRequest);
                } else if (httpRequest.getHttpMethod().equals(HTTPMethod.GET)) {
                    return get(httpRequest);
                }
                return getNotAllowedMethodResponse(httpRequest);
            } else if (httpRequest.getHttpPath().comparePath("/api/users/1")) {
                if (httpRequest.getHttpMethod().equals(HTTPMethod.PATCH)) {
                    return patch(httpRequest);
                }
                return getNotAllowedMethodResponse(httpRequest);
            }
            return getNotFindPathResponse(httpRequest);
        } catch (Exception exception) {
            return getNotExpectedResponse();
        }
    }

    private HTTPResponse getNotAllowedMethodResponse(HTTPRequest httpRequest) {
        HTTPResponse httpResponse = new HTTPResponse(405);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "plain/text");
        headers.put("content-encoding", "UTF-8");
        httpResponse.setHttpHeaders(headers);
        httpResponse.setBody(String.format("The method %s is not allowed on %s", httpRequest.getHttpMethod(),
                httpRequest.getHttpPath()));
        return httpResponse;
    }

    private HTTPResponse getNotFindPathResponse(HTTPRequest httpRequest) {
        HTTPResponse httpResponse = new HTTPResponse(404);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "plain/text");
        headers.put("content-encoding", "UTF-8");
        httpResponse.setHttpHeaders(headers);
        httpResponse.setBody(String.format("Cannot find the path %s", httpRequest.getHttpPath()));
        return httpResponse;
    }

    private HTTPResponse getNotExpectedResponse() {
        HTTPResponse httpResponse = new HTTPResponse(500);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "plain/text");
        headers.put("content-encoding", "UTF-8");
        httpResponse.setHttpHeaders(headers);
        httpResponse.setBody("The exception is not expected");
        return httpResponse;
    }

    public HTTPResponse post(HTTPRequest httpRequest) {
        HTTPPOSTRequest httpPostRequest = (HTTPPOSTRequest) FileUtil.readJsonValue(httpRequest.getBody(),
                HTTPPOSTRequest.class);
        try {
            validEmail(httpPostRequest.email);
            return new HTTPResponse(201);
        } catch (IllegalArgumentException ex) {
            HTTPResponse httpResponse = new HTTPResponse(400);
            Map<String, String> headers = new HashMap<>();
            headers.put("content-type", "plain/text");
            headers.put("content-encoding", "UTF-8");
            httpResponse.setHttpHeaders(headers);
            httpResponse.setBody("Registration's format incorrect.");
            return httpResponse;
        }
    }

    public HTTPResponse patch(HTTPRequest httpRequest) {
        HTTPResponse httpResponse = new HTTPResponse(200);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("content-encoding", "UTF-8");
        httpResponse.setHttpHeaders(headers);

        Map<String, Object> body = new HashMap<>();
        body.put("id", 1);
        body.put("email", "abc@gmail.com");
        body.put("name", "newAbc");
        body.put("password", "hello");
        httpResponse.setBody(body);
        return httpResponse;
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

    private void validEmail(String email) {
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Illegal Email");
        }
    }
}
