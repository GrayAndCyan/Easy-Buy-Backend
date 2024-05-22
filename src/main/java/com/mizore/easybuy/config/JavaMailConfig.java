package com.mizore.easybuy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mail")
public class JavaMailConfig {
    private String host = "smtp.126.com";
    private String username = "EasyBuyServer@126.com";
    private String password = "FWIDNNXOTKUEFOSC";
    private String from = "EasyBuyServer@126.com";
    private String to = "EasyBuyAdmin@126.com";
}
