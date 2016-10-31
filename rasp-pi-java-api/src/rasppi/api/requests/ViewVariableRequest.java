package rasppi.api.requests;

import java.util.Map;

public class ViewVariableRequest extends GenericRequest{

    /**
     * ViewVariableResquest:
     *      A class that represents a request to view variables on the variable database. A set of parameters can be
     *      provided to filter out results.
     *
     * @param params
     *      May be left empty. If done so, the server will return all variables.
     *      Optional Parameters:
     *          id   -  If provided with the id of a variable, the server will return only that variable.
     *          name -  If provided with the name of a variable, the server will return all variables that have that
     *                  string as a substring in their name.
     *          type -  If provided with the type of a variable, the server will return all variables of that type.
     *          value - If provided with the value of a variable, the server will return all variables with that value.
     *
     *      Name, type, and value parameters can be used in conjunction to filter out specific variables.
     */
    public ViewVariableRequest(Map<String, Object> params){
        super("http://192.168.0.2/view.php", "post", params);
    }
}
