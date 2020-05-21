package matcha.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gateways {

    REGISTRATION("register"),
    PROFILE_UPDATE("profile-update");

    private final String uri;
}
