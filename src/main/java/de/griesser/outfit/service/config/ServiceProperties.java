package de.griesser.outfit.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "service")
@Data
public class ServiceProperties {

    private String endpoint;
    private String units;
    private String appid;
    private String cityFilename;

}
