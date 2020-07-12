package matcha.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties("schema")
public class ConfigProperties {

    String folder;
    List<String> schemasList;

    @Value("${options.users.default.active}")
    boolean usersDefaultActive;

    @Value("${options.mail.send}")
    boolean mailSend;
}
