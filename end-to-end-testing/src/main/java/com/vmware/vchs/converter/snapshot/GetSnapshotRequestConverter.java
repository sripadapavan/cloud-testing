package com.vmware.vchs.converter.snapshot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vmware.vchs.model.portal.snapshot.GetSnapshotRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by georgeliu on 14/12/17.
 */
public class GetSnapshotRequestConverter extends AbstractHttpMessageConverter<GetSnapshotRequest> {

    private ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(GetSnapshotRequestConverter.class);

    public GetSnapshotRequestConverter() {
        super(new MediaType("application", "json"));
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return clazz.getCanonicalName().equalsIgnoreCase(((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName());
    }

    @Override
    protected GetSnapshotRequest readInternal(Class<? extends GetSnapshotRequest> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        GetSnapshotRequest createSnapshotRequest = this.objectMapper.readValue(inputMessage.getBody(), clazz);
        return checkNotNull(createSnapshotRequest);
    }

    @Override
    protected void writeInternal(GetSnapshotRequest createSnapshotRequest, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        checkNotNull(createSnapshotRequest);
        this.objectMapper.writeValue(outputMessage.getBody(), createSnapshotRequest);
        logger.debug(this.objectMapper.writeValueAsString(createSnapshotRequest));
    }
}
