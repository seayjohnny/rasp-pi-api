package raspipi.api.objects;

import raspipi.api.requests.GetVariableRequest;
import raspipi.api.requests.UpdateVariableRequest;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class VariableObject{
    private Integer Id;
    private String Name;
    private String Type;
    private Object Value;
    private static VariableRegister Register = VariableRegister.open();

    public VariableObject() {
        this.assignId();
        this.setName(null);
        this.setValue(null);
        this.update();
    }

    public VariableObject(String name) {
        this.assignId();
        this.setName(name);
        this.setValue(null);
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
        this.setType(value.getClass().getSimpleName().toLowerCase());
    }

    private void assignId(){
        this.setId(Register.assignVariableId());
    }

    public Object viewRemoteValue(){
        // opens a view variable request to get current value stored on database
        Object value = null;
        return value;
    }

    public void refresh(){
        // opens a get variable request to restore variable to how it's stored on database
        Map<String, String> params = new HashMap<>();
        params.put("id", this.getId().toString());

        GetVariableRequest request = new GetVariableRequest(params);
        request.execute();

        String name = request.getResponse().getBody().get("name").toString();
        String type = request.getResponse().getBody().get("type").toString();
        String s_value = request.getResponse().getBody().get("value").toString();
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
        this.setName(name);
        this.setValue(value);
    }

    public void update(){
        UpdateVariableRequest request = new UpdateVariableRequest(this.toParams());
        request.execute();
    }

    public void remove(){
        // asks VariableRegister to remove variable from database
        Register.removeVariable(this.Id);
        this.setId(0);
        this.setName("");
        this.setValue(0);

    }

    private Map<String, String> toParams(){
        Map<String, String> params = new HashMap<>();
        params.put("id", this.getId().toString());
        params.put("name", this.getName());
        params.put("type", this.getType());
        params.put("value", this.getValue().toString());

        return params;
    }
}
