package matcha.integration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.converter.Converter;
import matcha.db.EntityActions;
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

import java.util.UUID;

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
                .requestPayloadType(String.class))
                .log(Gateways.REGISTRATION.getUri())
                .transform(o -> validateBySchema(Schemas.REGISTRY_SCHEMA.getName(), o.toString()))
                .filter(o -> o instanceof User,
                        f -> f.discardChannel(Channels.SCHEME_VALIDATION_ERROR.getChannelName()))
                .transform(o -> {((User) o).setActivationCode(UUID.randomUUID().toString()); return o;})
                .transform(entityActions::userRegistry)
                .get();
    }

    @Bean
    public IntegrationFlow schemeValidationErrorFlow() {
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

    private Object validateBySchema(String schemaName, String json) {
        try {
            validatorFactory.getValidatorMap().get(schemaName).validate(json);
            return Converter.convertToUser(json);
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

}
