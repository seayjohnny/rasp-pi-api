package raspipi.api.objects;

import raspipi.api.exceptions.DuplicateVariableId;
import raspipi.api.exceptions.InvalidVariableId;
import raspipi.api.requests.CreateVariableRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariableRegister {
    private static VariableRegister instance = null;
    private static List<Integer> IdStack= new ArrayList<>();

    public static List<Integer> getIdStack() {
        return IdStack;
    }

    private VariableRegister(){}

    public static VariableRegister open(){
        if(instance == null){
            instance = new VariableRegister();
        }

        return instance;
    }

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
    public Integer assignVariableId(){
        // Send a create request to reserve a variable id on variable database and assign it to the provided variable

        Map<String, String> emptyParams = new HashMap<>();
        emptyParams.put("name", "TEMP");
        emptyParams.put("type", "TEMP");
        emptyParams.put("value", "TEMP");

        CreateVariableRequest request = new CreateVariableRequest(emptyParams);
        request.execute();

        Integer id = (Integer)request.getResponse().getBody().get("id");
        this.addVariableId(id);

        return id;

    }

    public void removeVariable(Integer id){
        // open a remove request to remove variable from database
        this.removeVariableId(id);
    }



}
