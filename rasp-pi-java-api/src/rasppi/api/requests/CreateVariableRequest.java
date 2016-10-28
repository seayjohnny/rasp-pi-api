package rasppi.api.requests;

import java.util.Map;

public class CreateVariableRequest extends GenericRequest{

    /**
     * CreateVariableRequest:
     *      A class that represents a request to create a new variable on the variable database with the attributes
     *      given as parameters.
     *
     * @param params
     */
    public CreateVariableRequest(Map<String, Object> params){
        super("http://192.168.0.2/db_create.php", "post", params);
    }
}
