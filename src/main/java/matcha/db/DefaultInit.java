package matcha.db;

import lombok.AllArgsConstructor;
import matcha.converter.Utils;
import matcha.model.User;
import matcha.properties.ConfigProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.Date;

@AllArgsConstructor
@Configuration
public class DefaultInit {

    private EntityActions entityActions;
    private ConfigProperties properties;

    @Bean
    @DependsOn({"createAllTables"})
    @ConditionalOnProperty(value = "options.users.default.init.active", matchIfMissing = false, havingValue = "true")
    void initDefaultUsers() {
            User user = new User();
            user.setLogin(properties.getUsersDefaultInitLogin());
            Utils.initRegistryUser(user, properties.getUsersDefaultInitPassword());
            user.setActivationCode(properties.getUsersDefaultInitCode());
            user.setFname("test");
            user.setLname("test");
            user.setEmail("fermer@gmail.com");
            user.setActive(true);
            user.setBlocked(false);
            user.setTime(new Date());
            user.setProfileId(null);
            System.err.println(user);

            Object o = entityActions.userRegistry(user);
            System.err.println("Users created: " + o);
    }
}
