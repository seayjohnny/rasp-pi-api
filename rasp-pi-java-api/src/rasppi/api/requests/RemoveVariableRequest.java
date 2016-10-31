package rasppi.api.requests;

import java.util.Map;

public class RemoveVariableRequest extends GenericRequest{

    /**
     * RemoveVariableRequest:
     *      A class that represents a request to remove a variable from the variable database.
     *
     * @param params
     *      Only valid parameter is "id".
     */
    public RemoveVariableRequest(Map<String, Object> params){
        super("http://192.168.0.2/remove.php", "post", params);
    }
}
