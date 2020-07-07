package matcha.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.converter.Converter;
import matcha.converter.Utils;
import matcha.db.EntityActions;
import matcha.db.EntityManipulator;
import matcha.model.User;
import matcha.properties.Channels;
import matcha.properties.Gateways;
import matcha.properties.Schemas;
import matcha.response.ResponseError;
import matcha.validator.ValidatorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;

@Configuration
@AllArgsConstructor
@EnableIntegration
@EnableIntegrationManagement
@Slf4j
public class FlowConfiguration {

    private ValidatorFactory validatorFactory;
    private EntityActions entityActions;

    @Bean
    public IntegrationFlow myFlow() {
        return IntegrationFlows.from(Http.inboundGateway(Gateways.REGISTRATION.getUri())
                .requestMapping(m -> m.methods(HttpMethod.POST))
                .crossOrigin(cors -> cors.origin("*"))
                .requestPayloadType(String.class))
                .log(Gateways.REGISTRATION.getUri())
                .transform(o -> validateRegistrationUserBySchema(Schemas.REGISTRY_SCHEMA.getName(), o.toString()))
                .filter(o -> !(o instanceof ResponseError),
                        f -> f.discardChannel(Channels.SCHEME_VALIDATION_ERROR.getChannelName()))
                .transform(entityActions::userRegistry)
                .get();
    }

    @Bean
    public IntegrationFlow loginFlow() {
        return IntegrationFlows.from(Http.inboundGateway(Gateways.LOGIN.getUri())
                .requestMapping(m -> m.methods(HttpMethod.POST))
                .crossOrigin(cors -> cors.origin("*"))
                .requestPayloadType(String.class))
                .log(Gateways.LOGIN.getUri())
                .transform(o -> loginPrepare(Schemas.LOGIN_SCHEMA.getName(), o.toString()))
                .filter(o -> !(o instanceof ResponseError),
                        f -> f.discardChannel(Channels.SCHEME_VALIDATION_ERROR.getChannelName()))
//                .transform(this::loginPrepare)
                .get();
    }

    @Bean
    public IntegrationFlow profileUpdateFlow() {
        return IntegrationFlows.from(Http.inboundGateway(Gateways.PROFILE_UPDATE.getUri())
                .requestMapping(m -> m.methods(HttpMethod.POST))
                .crossOrigin(cors -> cors.origin("*"))
                .requestPayloadType(String.class))
                .log(Gateways.PROFILE_UPDATE.getUri())
                .transform(o -> profilePrepare(Schemas.PROFILE_UPDATE_SCHEMA.getName(), o.toString()))
                .filter(o -> !(o instanceof ResponseError),
                        f -> f.discardChannel(Channels.SCHEME_VALIDATION_ERROR.getChannelName()))
//                .transform(this::loginPrepare)
                .get();
    }

    @Bean
    public IntegrationFlow schemeValidationErrorChannel() {
        return IntegrationFlows.from(Channels.SCHEME_VALIDATION_ERROR.getChannelName())
                .log(Channels.SCHEME_VALIDATION_ERROR.getChannelName())
                .transform(Object::toString)
                .get();
    }

//    @Bean
//    public IntegrationFlow outbound() {
//        return IntegrationFlows.from("httpOutRequest")
//                .handle(Http.outboundGateway("http://localhost:8080/foo")
//                        .httpMethod(HttpMethod.POST)
//                        .expectedResponseType(String.class))
//                .get();
//    }

    //                .route(p -> {
//                    OnlyAction onlyAction = ConverterToObject.convertToOnlyAction(p.toString());
//                    if (onlyAction != null)
//                        return onlyAction.getAction();
//                    else
//                        return Channels.NO_ACTION_CHANNEL.getChannelName();
//                })

    private Object validateRegistrationUserBySchema(String schemaName, String json) {
        Object o = validateOnlyBySchema(schemaName, json);
        if (o instanceof Boolean) {
            try {
                User user = Converter.convertToUser(json);
                final ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);
                Utils.initRegistryUser(user, node.get("password").asText());
                o = user;
            } catch (Exception e) {
                log.error("Error. Error mapping json: " + json);
            }
        }
        return o;
    }

//    private Object validateRegistrationUserBySchema(String schemaName, String json) {
//        Object o = validateOnlyBySchema(schemaName, json);
//        if (o instanceof Boolean) {
//            try {
//                User user = Converter.convertToUser(json);
////                entityActions.
//
//                final ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);
//                Utils.initRegistryUser(user, node.get("password").asText());
//                o = user;
//            } catch (Exception e) {
//                log.error("Error. Error mapping json: " + json);
//            }
//        }
//        return o;
//    }

    private Object validateOnlyBySchema(String schemaName, String json) {
        try {
            validatorFactory.getValidatorMap().get(schemaName).validate(json);
            return true;
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder()
                    .append("Failed schema validate. Schema: ")
                    .append(schemaName)
                    .append(" json: ")
                    .append(json)
                    .append(" Message: ")
                    .append(e.getMessage());
            String result = sb.toString();
            log.warn(result);
            return new ResponseError("error", result);
        }
    }

    private Object loginPrepare(String schemaName, String json) {
        Object o = validateOnlyBySchema(schemaName, json);
        if (o instanceof Boolean) {
            try {
                final ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);
                //проверка location. Если там ip, то найти его расположение, иначе - ничего не делать
                o = entityActions.userLogin(node.get("login").asText(), node.get("password").asText(), node.get("location"));
            } catch (Exception e) {
                log.error("Error. Error mapping json: " + json);
            }
        }
        return o;
    }

    private Object profilePrepare(String schemaName, String json) {
        Object o = validateOnlyBySchema(schemaName, json);
        if (o instanceof Boolean) {
            try {
                User user = Converter.convertToUser(json);
                o = entityActions.profileSave(json);
//                final ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);
//                Utils.initRegistryUser(user, node.get("password").asText());
//                o = user;
            } catch (Exception e) {
                log.error("Error. Error mapping json: " + json);
            }
        }
        return o;
    }
}
