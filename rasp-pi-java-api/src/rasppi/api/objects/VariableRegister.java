package rasppi.api.objects;

import rasppi.api.exceptions.DuplicateVariableId;
import rasppi.api.exceptions.InvalidVariableId;
import rasppi.api.exceptions.ProtectedVariableException;
import rasppi.api.requests.CreateVariableRequest;
import rasppi.api.requests.GetVariableRequest;
import rasppi.api.requests.RemoveVariableRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;

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
    private Boolean addVariableId(Integer id){
        if(id == -1){
            return false;
        } else if(!this.IdStack.contains(id)) {
            this.IdStack.add(id);
            return true;
        } else {
            return false;
        }
    }

    /**
     * removeVariableId:
     *      Removes the provided id from the IdStack.
     *
     * @param id
     */
    private Boolean removeVariableId(Integer id){
        if(this.IdStack.contains(id)) {
            this.IdStack.remove(id);
            return true;
        } else {
            return false;
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
        if(DummyPi.exists()){
            return DummyPi.createVariable();
        } else {
            Integer id;

            Map<String, Object> emptyParams = new HashMap<>();
            emptyParams.put("name", "TEMP");
            emptyParams.put("type", "TEMP");
            emptyParams.put("value", "TEMP");
            emptyParams.put("protected", 0);

            CreateVariableRequest request = new CreateVariableRequest(emptyParams);
            request.execute();
            id = (Integer) request.getResponse().getBodyData().get("id");
            addVariableId(id);

            return id;
        }
    }

    /**
     * retrieveVariable:
     *      Opens a GetVariableRequest to retrieve a variable from the database and returns its attributes.
     *
     * @param id
     * @return Map<String, String>
     */
    public Map<String, Object> retrieveVariable(Integer id){
        if(DummyPi.exists()){
            Map<String, Object> var = DummyPi.loadVariable(id);
            if(var != null){
                this.addVariableId(id);
                return var;
            }
        } else {
            GetVariableRequest request = new GetVariableRequest(this.idToParams(id));
            request.execute();
            try {
                if (addVariableId((Integer) request.getResponse().getBodyData().get("id"))) {
                    return request.getResponse().getBodyData();
                } else {
                    throw new ProtectedVariableException();
                }
            } catch (ProtectedVariableException e) {

            }
        }
        return null;
    }

    /**
     * removeVariable:
     *      Opens a RemoveVariableRequest to remove a variable from the database, and then removes the variable's id
     *      from the IdStack.
     *
     * @param id
     */
    public Boolean removeVariable(Integer id){
        if(DummyPi.exists()){
            DummyPi.removeVariable(id);
            return true;
        } else {
            RemoveVariableRequest request = new RemoveVariableRequest(this.idToParams(id));
            request.execute();
            if ((Integer) request.getResponse().getBody().get("success") == 1) {
                this.removeVariableId(id);
                return true;
            } else {
                return false;
            }
        }
    }

    private Map<String, Object> idToParams(Integer id){
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        return params;
    }



}
