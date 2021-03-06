package com.vmware.vchs.converter.iamrole;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.vmware.vchs.model.portal.iamRole.GetUserResponse;
import com.vmware.vchs.model.portal.iamRole.UserItemResponse;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by fanz on 5/26/15.
 */

public class GetUserResponseConverter extends AbstractHttpMessageConverter<GetUserResponse> {

    private ObjectMapper objectMapper = new ObjectMapper();


    public GetUserResponseConverter() {
        super(new MediaType("application", "json"));
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return clazz.getCanonicalName().equalsIgnoreCase(((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName());
    }

    @Override
    protected GetUserResponse readInternal(Class<? extends GetUserResponse> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        GetUserResponse getUserResponse = new GetUserResponse();
        String body = CharStreams.toString(new InputStreamReader(inputMessage.getBody(), Charsets.UTF_8));
        JsonNode rootNode = checkNotNull(this.objectMapper.readTree(body), "The list response should not be null");
        UserItemResponse[] userItemResponseArray = this.objectMapper.readValue(rootNode.toString(), UserItemResponse[].class);
        List<UserItemResponse> userItemResponses = Arrays.asList(userItemResponseArray);
        getUserResponse.setUserItemResponses(userItemResponses);
        logger.debug(this.objectMapper.writeValueAsString(userItemResponses));
        return getUserResponse;
    }

    @Override
    protected void writeInternal(GetUserResponse userItemResponses, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        this.objectMapper.writeValue(outputMessage.getBody(), userItemResponses);
    }

}
