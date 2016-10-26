package raspipi.api.requests;

import java.util.Map;

public class UpdateVariableRequest extends GenericRequest{

    public UpdateVariableRequest(Map<String, String> params){
        super("http//:192.168.0.2/db_update.php", "post", params);
    }
}
