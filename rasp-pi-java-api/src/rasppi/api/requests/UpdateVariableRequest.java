package rasppi.api.requests;

import java.util.Map;

public class UpdateVariableRequest extends GenericRequest{

    /**
     * UpdateVariableRequest:
     *      A class that represents a request to update variable on the variable database with the attributes
     *      given as parameters.
     *
     * @param params
     */
    public UpdateVariableRequest(Map<String, String> params){
        super("http//:192.168.0.2/db_update.php", "post", params);
    }
}
