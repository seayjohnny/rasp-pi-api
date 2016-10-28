package rasppi.api.objects;

import rasppi.api.requests.UpdateVariableRequest;
import rasppi.api.requests.ViewVariableRequest;

import java.util.HashMap;
import java.util.Map;

/**
 *  VariableObject:
 *      A class that represents a variable on the variable database.
 */
public class VariableObject{
    private String Null = "null";
    private Integer Id;
    private String Name = "null";
    private String Type;
    private Object Value;

    private static VariableRegister Register = VariableRegister.open();

    public VariableObject() {
        this.setId(-2);
        this.setValue(Null);
    }

    public VariableObject(String name) {
        this.assignId();
        this.setName(name);
        this.setValue(Null);
        this.update();
    }

    public VariableObject(String name, Object value) {
        this.assignId();
        this.setName(name);
        this.setValue(value);
        this.update();
    }

    public Integer getId() {
        return Id;
    }

    private void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        if(this.getId() == -2){
            this.assignId();
        }
        Name = name;
    }

    public String getType() {
        return Type;
    }

    private void setType(String type) {
        Type = type;
    }

    public Object getValue() {
        return Value;
    }

    public void setValue(Object value) {
        Value = value;
        if(value != Null) {
            this.setType(value.getClass().getSimpleName().toLowerCase());
        } else{
            this.setType(Null);
        }
    }

    /**
     *  assignId:
     *      Makes a call to the VariableRegister to reserve an id on the variable database and assign it to this
     *      variable.
     */
    private void assignId(){
        this.setId(Register.reserveVariable());
    }

    /**
     * viewRemoteValue:
     *      Opens a ViewVariableRequest with only the id parameter to retrieve the variable's value currently stored on
     *      the variable database.
     *
     * @return Object
     */
    public Map<String, Object> viewRemote(){
        ViewVariableRequest request = new ViewVariableRequest(this.toParams("id"));
        Map<String, String> body = request.getResponse().getBody();
        Map<String, Object> remote = new HashMap<>();
        remote.put("id", Integer.valueOf(body.get("id")));
        remote.put("name", body.get("name"));
        remote.put("type", body.get("type"));
        remote.put("value", this.parseResponseBodyValue(body));
        return remote;
    }

    /**
     * refresh:
     *      Opens a ViewVariableRequest with only the id parameter to restore this variable with the attributes stored
     *      on the variable database.
     *
     *      Example:
     *          We get a VariableObject from the variable database with the attributes
     *          {id: 3, name: "var3", type: "integer", value: 12}. Within our code, we change the name and value of the
     *          variable to "myVar" and "thisisvalue", respectively. Without using the update() method for our variable,
     *          there are two versions of variable with id 3:
     *          VariableDatabase - {id: 3, name: "var3", type: "integer", value: 12}
     *          Our Code - {id: 3, name: "myVar", type: "string", value: "thisisvalue"}
     *
     *          refresh() will replace the attributes of our VariableObject with the attributes of the variable with the
     *          same id found on the variable database. So then we have:
     *          VariableDatabase - {id: 3, name: "var3", type: "integer", value: 12}
     *          Our Code - {id: 3, name: "var3", type: "integer", value: 12}
     *
     */
    public void refresh(){
        ViewVariableRequest request = new ViewVariableRequest(this.toParams("id"));
        request.execute();

        String name = request.getResponse().getBody().get("name").toString();
        Object value = parseResponseBodyValue(request.getResponse().getBody());

        this.setName(name);
        this.setValue(value);
    }

    /**
     * update:
     *      Opens an UpdateVariableRequest to update the attributes of the variable on the variable database with the
     *      current attributes of this VariableObject.
     */
    public void update(){
        Map<String, Object> params = new HashMap<>();
        params.put("id", this.getId());
        params.put("name", this.getName());
        params.put("type", this.getType());
        params.put("value", this.getValue());
        UpdateVariableRequest request = new UpdateVariableRequest(params);
        request.execute();
    }

    /**
     *  remove:
     *      Makes a call to the VariableRegister to remove this variable from the variable database, and then set the
     *      id, name, and value of this VariableObject to 0, "", and 0, respectively.
     */
    public void remove(){
        Register.removeVariable(this.getId());
        this.setId(-1);
        this.setName(Null);
        this.setValue(Null);

    }

    /**
     * load:
     *      Makes a call to the VariableRegister to retrieve a variable already on the variable database and store it in
     *      this VariableObject.
     *
     * @param id
     */
    public void load(Integer id){
        Map<String, String> body = Register.retrieveVariable(id);

        this.setId(Integer.parseInt(body.get("id")));
        this.setName(body.get("name"));
        this.setValue(parseResponseBodyValue(body));
    }

    /**
     * toParams:
     *      A helper method that puts the attributes of this VariableObject into a set of parameters.
     * @return
     */
    private Map<String, Object> toParams(String keys){
        Map<String, Object> params = new HashMap<>();
        if(keys.contains("id")){
            if(this.getId() != -1){
                params.put("id", this.getId());
            } else {
                params.put("id", Null);
            }
        }
        if(keys.contains("name")){
            if(this.getName() != Null){
                params.put("name", this.getName());
            } else {
                params.put("name", Null);
            }
        }
        if(keys.contains("type")){
            if(this.getType() != Null){
                params.put("type", this.getType());
            } else {
                params.put("type", Null);
            }
        }
        if(keys.contains("value")){
            if(this.getValue() != Null){
                params.put("value", this.getValue());
            } else {
                params.put("value", Null);
            }
        }
        return params;
    }

    /**
     * parseResponseBodyValue:
     *      A helper method that parses the value, based on type, found within a ResponseObject body.
     *
     * @param body
     * @return Object
     */
    private Object parseResponseBodyValue(Map<String, String> body){
        String type = body.get("type").toString();
        String s_value = body.get("value").toString();

        Object value;
        switch(type){
            case "integer":
                value = Integer.valueOf(s_value);
                break;
            case "float":
                value = Float.valueOf(s_value);
                break;
            case "boolean":
                value = Boolean.valueOf(s_value);
                break;
            case "string":
            default:
                value = s_value;
        }

        return value;
    }
}
