package rasppi.api.requests;

import java.util.Map;

public class GetVariableRequest extends GenericRequest{

    public GetVariableRequest(Map<String, Object> params){
        super("http://192.168.0.2/load.php", "post", params);
    }
}
