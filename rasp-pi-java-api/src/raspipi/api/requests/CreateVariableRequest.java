package raspipi.api.requests;

import java.util.Map;

public class CreateVariableRequest extends GenericRequest{

    public CreateVariableRequest(Map<String, String> params){
        super("http//:192.168.0.2/db_create.php", "post", params);
    }
}
