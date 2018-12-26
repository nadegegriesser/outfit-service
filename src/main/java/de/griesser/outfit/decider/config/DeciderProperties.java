package de.griesser.outfit.decider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "decider")
@Data
public class DeciderProperties {

    private String decisionKey;
    private String decisionFilename;

}
