package de.griesser.outfit.weatherservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "weatherservice")
@Data
public class ServiceProperties {

    private String endpoint;
    private String units;
    private String appid;
    private String cityFilename;

}
