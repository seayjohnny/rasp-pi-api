package rasppi.api.objects;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dakota on 10/28/2016.
 */
public class ResponseBodyObject {
    private String Success;
    private String Message;
    private Map<String, Object> Content = new HashMap<>();

    public ResponseBodyObject(String success, String message) {
        Success = success;
        Message = message;
    }

    public ResponseBodyObject(String success, String message, Map<String, Object> content) {
        Success = success;
        Message = message;
        Content = content;
    }

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        Success = success;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Map<String, Object> getContent() {
        return Content;
    }

    public void setContent(Map<String, Object> content) {
        Content = content;
    }
}
