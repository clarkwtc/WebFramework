package org.web.domain.ext;

import org.web.domain.core.DeserializationBodyTypeHandler;
import org.web.infrastructure.FileUtil;

public class DeserializationBodyTypeToXMLHandler extends DeserializationBodyTypeHandler {
    public DeserializationBodyTypeToXMLHandler(DeserializationBodyTypeHandler next) {
        super(next);
    }

    @Override
    protected boolean matchContentType(String contentType) {
        return "application/xml".equals(contentType);
    }

    @Override
    protected String transform(Object responseBody) {
        return FileUtil.writeXMLValue(responseBody, responseBody.getClass());
    }
}