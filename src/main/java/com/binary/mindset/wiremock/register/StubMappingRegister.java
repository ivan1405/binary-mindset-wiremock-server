package com.binary.mindset.wiremock.register;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;
import static org.springframework.core.io.support.ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX;

@Slf4j
public class StubMappingRegister {

    private final WireMock client;

    public StubMappingRegister(int port) {
        this( "localhost", port);
    }

    public StubMappingRegister(String host, int port) {
        this(new WireMock(host, port));
    }

    public StubMappingRegister(WireMock client) {
        this.client = client;
    }

    public void registerStubMappings(String resourcePattern) {
        Resource[] stubResources = this.resolveStubMappings(resourcePattern);

        for (Resource stubResource : stubResources) {
            registerStubMapping(stubResource);
        }
    }

    private Resource[] resolveStubMappings(String stubMappingPattern) {
        try {
            return new PathMatchingResourcePatternResolver().getResources(CLASSPATH_ALL_URL_PREFIX + stubMappingPattern);
        } catch (IOException e) {
            log.error("unable to resolve resources for pattern '{}'", stubMappingPattern, e);
        }
        return new Resource[0];
    }

    private void registerStubMapping(Resource stubResource) {
        StubMapping stubMapping = StubMapping.buildFrom(asString(stubResource));
        client.register(stubMapping);
    }

    private String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            return throwUnchecked(e, String.class);
        }
    }

}
