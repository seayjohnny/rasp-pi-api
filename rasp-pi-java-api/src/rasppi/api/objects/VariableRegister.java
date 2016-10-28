package rasppi.api.objects;

import rasppi.api.exceptions.DuplicateVariableId;
import rasppi.api.exceptions.InvalidVariableId;
import rasppi.api.requests.CreateVariableRequest;
import rasppi.api.requests.GetVariableRequest;
import rasppi.api.requests.RemoveVariableRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  VariableRegister:
 *      A singleton class that handles the creation, retrieval, and removal of variables on the variable database. The
 *      purpose of this class is to delegate the ids of variables so that the VariableObjects in code and the variables
 *      on the variable database are loosely synced.
 */
public class VariableRegister {
    private static VariableRegister instance = null;
    private static List<Integer> IdStack= new ArrayList<>();

    private VariableRegister(){}

    public static VariableRegister open(){
        if(instance == null){
            instance = new VariableRegister();
        }

        return instance;
    }

    public static List<Integer> getIdStack() {
        return IdStack;
    }

    /**
     * addVariableId:
     *      Adds the provided id to the IdStack.
     *
     * @param id
     */
    private void addVariableId(Integer id){
        try{
            if(!this.IdStack.contains(id)) {
                this.IdStack.add(id);
            } else {
                throw new DuplicateVariableId();
            }
        } catch(DuplicateVariableId e) {
            System.err.println(e.getMessage().toString());
        }
    }

    /**
     * removeVariableId:
     *      Removes the provided id from the IdStack.
     *
     * @param id
     */
    private void removeVariableId(Integer id){
        try{
            if(this.IdStack.contains(id)) {
                this.IdStack.remove(id);
            } else {
                throw new InvalidVariableId();
            }
        } catch(InvalidVariableId e){
            System.err.println(e.getMessage().toString());
        }
    }

    /**
     * reserveVariable:
     *      Opens a CreateVariableRequest to reserve a valid variable id on the variable database and returns the
     *      reserved id.
     *
     * @return Integer
     */
    public Integer reserveVariable(){
        Integer id;

        Map<String, Object> emptyParams = new HashMap<>();
        emptyParams.put("name", "TEMP");
        emptyParams.put("type", "TEMP");
        emptyParams.put("value", "TEMP");

        CreateVariableRequest request = new CreateVariableRequest(emptyParams);
        request.execute();
        id = Integer.parseInt(request.getResponse().getBody().get("id"));
        this.addVariableId(id);

        return id;
    }

    /**
     * retrievVariable:
     *      Opens a GetVariableRequest to retrieve a variable from the database and returns its attributes.
     *
     * @param id
     * @return Map<String, String>
     */
    public Map<String, String> retrieveVariable(Integer id){
        GetVariableRequest request = new GetVariableRequest(this.idToParams(id));
        request.execute();
        this.addVariableId(id);
        return request.getResponse().getBody();
    }

    /**
     * removeVariable:
     *      Opens a RemoveVariableRequest to remove a variable from the database, and then removes the variable's id
     *      from the IdStack.
     *
     * @param id
     */
    public void removeVariable(Integer id){
        RemoveVariableRequest request = new RemoveVariableRequest(this.idToParams(id));
        request.execute();

        this.removeVariableId(id);
    }

    private Map<String, Object> idToParams(Integer id){
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        return params;
    }



}
