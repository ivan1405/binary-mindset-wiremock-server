package com.binary.mindset.wiremock;

import com.binary.mindset.wiremock.runner.WiremockRunner;
import lombok.extern.slf4j.Slf4j;

import static com.binary.mindset.wiremock.runner.WiremockRunner.DEFAULT_STUB_MAPPINGS_PATTERN;

@Slf4j
public class WiremockApp {

    public static void main(String[] args) {
        WiremockRunner wiremockRunner = new WiremockRunner(DEFAULT_STUB_MAPPINGS_PATTERN);
        wiremockRunner.run(args);

        log.info("wiremock is running on port {}\n {} stub mappings are available:\n{}",
                wiremockRunner.port(),
                wiremockRunner.stubMappings().size(),
                wiremockRunner.stubMappingsReport(true)
        );
    }
}
