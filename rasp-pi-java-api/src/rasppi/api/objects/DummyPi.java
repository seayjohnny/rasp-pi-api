package rasppi.api.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DummyPi {

    private static DummyPi instance = null;
    public static Map<Integer, DummyVariable> vars = new HashMap<>();

    private DummyPi(){}

    /**
     * open:
     *      Opens up the DummyPi and fills vars.
     * @return
     */
    public static DummyPi open(){
        if(instance == null){
            instance = new DummyPi();
            fillVars();
        }

        return instance;
    }

    /**
     * exists:
     *      Returns if DummyPi exists or not.
     * @return
     */
    public static Boolean exists(){
        if(instance == null){
            return false;
        } else {
            return true;
        }
    }

    /**
     * fillVars:
     *      Upon instance creation, fillVars fills vars with DummyVariables.
     */
    private static void fillVars(){
        vars.put(0, new DummyVariable(0, "null", "null", "null", 3));
        for (int i = 1; i <= 100; i++) {
            DummyVariable var = new DummyVariable(i, String.format("reserved%d", i), "null", "null", 1);
            vars.put(i, var);
        }
    }

    /**
     * createVariable:
     *      Creates a new variable in vars. (Emulates create.php)
     * @return
     */
    public static Integer createVariable(){
        Set ids = vars.keySet();
        for (int i = 101; i <= 500; i++) {
            if (!ids.contains(i)){
                vars.put(i, new DummyVariable(i, "null", "null", "null", 0));

                return i;
            }
        }
        return null;
    }

    /**
     * updateVariable:
     *      Updates variable in vars. (Emulates update.php)
     * @param var
     */
    public static void updateVariable(VariableObject var){
        if(var.getProtection() < 1){
            vars.put(var.getId(), new DummyVariable(var.getId(), var.getName(), var.getType(), var.getValue(),
                    var.getProtection()));
        }

    }

    /**
     * removeVariable:
     *      Removes variable from vars. (Emulates remove.php)
     * @param id
     */
    public static void removeVariable(Integer id){
        if(vars.get(id).PROTECTION < 1){
            vars.remove(id);
        }
    }

    /**
     * loadVariable:
     *      Loads a variable from vars. (Emulates load.php)
     * @param id
     * @return
     */
    public static Map<String, Object> loadVariable(Integer id){
        if(vars.get(id).PROTECTION < 3){
            Map<String, Object> var = new HashMap<>();
            var.put("id", vars.get(id).ID);
            var.put("name", vars.get(id).NAME);
            var.put("type", vars.get(id).TYPE);
            var.put("value", vars.get(id).VALUE);
            var.put("protection", vars.get(id).PROTECTION);

            return var;
        } else {
            return null;
        }

    }

}
