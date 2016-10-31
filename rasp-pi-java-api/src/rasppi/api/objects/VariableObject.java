package rasppi.api.objects;

import rasppi.api.requests.GetVariableRequest;
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
    private Integer Protection = 0;

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

    public Integer getProtection() { return Protection;}

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
        if(DummyPi.exists()){
            return DummyPi.loadVariable(this.getId());
        } else {
            GetVariableRequest request = new GetVariableRequest(this.toParams("id"));
            request.execute();
            Map<String, Object> data = request.getResponse().getBodyData();
            Map<String, Object> remote = new HashMap<>();
            remote.put("id", data.get("id"));
            remote.put("name", data.get("name"));
            remote.put("type", data.get("type"));
            remote.put("value", data.get("value"));
            remote.put("protected", data.get("protected"));
            return remote;
        }
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
        if(DummyPi.exists()){
            Map<String, Object> remote = DummyPi.loadVariable(this.getId());
            this.setName((String)remote.get("name"));
            this.setValue(remote.get("value"));
        } else {
            ViewVariableRequest request = new ViewVariableRequest(this.toParams("id"));
            request.execute();

            String name = request.getResponse().getBody().get("name").toString();
            Object value = request.getResponse().getBodyData().get("value");

            this.setName(name);
            this.setValue(value);
        }
    }

    /**
     * update:
     *      Opens an UpdateVariableRequest to update the attributes of the variable on the variable database with the
     *      current attributes of this VariableObject.
     */
    public void update(){
        if(Protection < 2) {
            if(DummyPi.exists()){
                DummyPi.updateVariable(this);
            } else {
                UpdateVariableRequest request = new UpdateVariableRequest(toParams("id, name, type, value"));
                request.execute();
            }
        } else {
            // attempting to write to protected variable
        }
    }

    /**
     *  remove:
     *      Makes a call to the VariableRegister to remove this variable from the variable database, and then set the
     *      id, name, and value of this VariableObject to 0, "", and 0, respectively.
     */
    public void remove(){
        if(Register.removeVariable(this.getId())){
            this.setId(-1);
            this.setName(Null);
            this.setValue(Null);
            Protection = 0;
        } else {
            // attempting to remove protected variable
        }
    }

    /**
     * load:
     *      Makes a call to the VariableRegister to retrieve a variable already on the variable database and store it in
     *      this VariableObject.
     *
     * @param id
     */
    public void load(Integer id){
        Map<String, Object> data = Register.retrieveVariable(id);
        if((Integer) data.get("protection") < 3) {
            this.setId((Integer) data.get("id"));
            this.setName((String) data.get("name"));
            this.setValue(data.get("value"));
        } else {
            // attempting to load inaccessible variable
        }
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

    @Override
    public String toString(){
        return String.format("id: %d, name: %s, type: %s, value: %s, protection: %d", this.getId(), this.getName(),
                this.getType(), this.getValue().toString(), this.getProtection());
    }
}
