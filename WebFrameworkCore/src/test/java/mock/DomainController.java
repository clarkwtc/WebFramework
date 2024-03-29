package mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.web.domain.core.HTTPResponse;

public class DomainController {
    public DomainService domainService;

    public DomainController(DomainService domainService) {
        this.domainService = domainService;
    }

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }

    public DomainService getDomainService() {
        return domainService;
    }

    public HTTPResponse post(HTTPPOSTRequest httpRequest) {
        try {
            domainService.validEmail(httpRequest);
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

    public HTTPResponse patch() {
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

    public HTTPResponse get() {
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
