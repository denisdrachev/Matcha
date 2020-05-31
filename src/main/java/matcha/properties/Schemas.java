package matcha.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Schemas {

    REGISTRY_SCHEMA("registry-schema"),
    LOGIN_SCHEMA("login-schema");

    private final String name;
}
