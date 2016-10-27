package rasppi.api.requests;

import java.util.Map;

public class GetVariableRequest extends GenericRequest{

    public GetVariableRequest(Map<String, String> params){
        super("http//:192.168.0.2/db_retrieve.php", "post", params);
    }
}
