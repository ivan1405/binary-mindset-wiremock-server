package com.binary.mindset.wiremock.runner;

import com.binary.mindset.wiremock.register.StubMappingRegister;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.core.WireMockApp;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import lombok.extern.slf4j.Slf4j;

import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

@Slf4j
public class WiremockRunner {

    private static final String SYSPROP_WIREMOCK_PORT = "wiremock.port";
    private static final String ENV_WIREMOCK_PORT = "WIREMOCK_PORT";

    public static final String DEFAULT_STUB_MAPPINGS_PATTERN = "**/META-INF/**/" + WireMockApp.MAPPINGS_ROOT + "/**/*.json";

    private final String stubMappingPattern;

    private WireMockServer server;

    public WiremockRunner(String stubMappingPattern) {
        this.stubMappingPattern = stubMappingPattern;
    }

    public void run(String... args) {
        WireMockConfiguration wireMockConfig = new WireMockConfiguration();

        // Do not record received requests to avoid JVM heap exhaustion.
        wireMockConfig.disableRequestJournal();

        // Resolve port
        int port = wiremockPort(args);
        if (port > 0) {
            wireMockConfig.port(port);
        } else {
            wireMockConfig.dynamicPort();
        }

        server = new WireMockServer(wireMockConfig);
        server.start();

        if (stubMappingPattern != null) {
            StubMappingRegister registry = new StubMappingRegister(server.port());
            registry.registerStubMappings(stubMappingPattern);
        }

        log.info("running Wiremock on port {}", server.port());
    }

    public void stop() {
        if (server != null) {
            server.stop();
        }
    }

    public boolean isRunning() {
        return server != null && server.isRunning();
    }

    public int port() {
        return server != null ? server.port() : -1;
    }

    public List<StubMapping> stubMappings() {
        return server != null ? server.getStubMappings() : Collections.emptyList();
    }

    public String stubMappingsReport(boolean pretty) {
        StringWriter sb = new StringWriter();
        stubMappings().forEach(mapping -> {
            String json = Json.write(mapping);
            sb.append('\n').append(pretty ? json : json.replaceAll("\\s", ""));
        });
        return sb.toString();
    }

    private int wiremockPort(String... args) {
        String wiremockPort;
        if (args.length > 0) {
            wiremockPort = args[0];
        } else {
            wiremockPort = System.getProperty(SYSPROP_WIREMOCK_PORT);
            if (wiremockPort == null) {
                wiremockPort = System.getenv(ENV_WIREMOCK_PORT);
            }
        }
        if (wiremockPort != null) {
            try {
                return Integer.parseInt(wiremockPort);
            } catch (NumberFormatException e) {
                log.error("unable to parse wiremock port {}", wiremockPort, e);
            }
        }
        return 0;
    }

}
