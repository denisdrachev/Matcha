package matcha.validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.response.*;
import org.json.JSONObject;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ValidationMessageService {

//    private ValidatorFactory validatorFactory;

    public ResponseError prepareValidateMessage(BindingResult bindingResult) {
        String validatorMessage = bindingResult.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
        log.info("Validator message: {}", validatorMessage);
        return new ResponseError("error", validatorMessage);
    }

    public ResponseError prepareMessageNullObject() {
        return new ResponseError("error", "Request body is empty");
    }

    public ResponseError prepareErrorMessage(String message) {
        return new ResponseError("error", message);
    }

    public ResponseError prepareErrorMessage(StringBuilder message) {
        return prepareErrorMessage(message.toString());
    }

    public ResponseOnlyType prepareMessageOkOnlyType() {
        return new ResponseOnlyType("success");
    }

    public ResponseOkData prepareMessageOkData(JSONObject o) {
        return new ResponseOkData("ok", o);
    }

    public ResponseOkDataObject prepareMessageOkData(Object o) {
        return new ResponseOkDataObject("ok", o);
    }

    public ResponseDataList prepareMessageOkDataList(List list) {
        return new ResponseDataList("success", list);
    }
//    public boolean validateJsonBySchema(String schemaName, String json) {
////        try {
//        validatorFactory.getValidatorMap().get(schemaName).validate(json);
//        return true;
////        } catch (ValidationException e) {
////            String clientMessage;
////            if (e.getCausingExceptions().size() != 0) {
////                clientMessage = Utils.clearValidateMessage(e.getCausingExceptions());
////            } else {
////                if (e.getMessage().matches(StringConstants.validationDelimiter))
////                    clientMessage = e.getMessage().split(StringConstants.validationDelimiter)[1];
////                else
////                    clientMessage = e.getMessage();
////            }
////            StringBuilder sb = new StringBuilder()
////                    .append("Failed schema validate. Schema: ")
////                    .append(schemaName)
////                    .append(" json: ")
////                    .append(json)
////                    .append(" Message: ")
////                    .append(clientMessage);
////            String result = sb.toString();
////            log.warn(result);
////            return new ResponseError("error", clientMessage);
////        }
//    }
}
