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
        vars.put(1, new DummyVariable(1, "appVersion", "double", 0.1, 1));
        vars.put(2, new DummyVariable(2, "deviceName", "string", "null", 1));
        vars.put(3, new DummyVariable(3, "deviceModel", "string", "null", 1));
        vars.put(4, new DummyVariable(4, "deviceSoftware", "string", "null", 1));
        vars.put(5, new DummyVariable(5, "deviceSoftwareVersion", "double", "null", 1));
        vars.put(6, new DummyVariable(6, "piModel", "string", "raspberry pi 3 model b", 2));
        vars.put(7, new DummyVariable(7, "piSoftwareVersion", "double", 0.1, 2));
        vars.put(8, new DummyVariable(8, "company", "string", "VivaTexAI", 2));
        vars.put(9, new DummyVariable(9, "owner", "string", "Owner's Name", 1));
        vars.put(10, new DummyVariable(10, "carMake", "string", "ford", 1));
        vars.put(11, new DummyVariable(11, "carModel", "string", "mustang", 1));
        vars.put(12, new DummyVariable(12, "carYear", "integer", 2004, 1));
        vars.put(13, new DummyVariable(13, "carFactoryDate", "integer", 2003, 1));
        vars.put(14, new DummyVariable(14, "carColor", "string", "blue", 1));
        vars.put(15, new DummyVariable(15, "carTransmission", "string", "manual", 1));
        vars.put(16, new DummyVariable(16, "carWheelDrive", "string", "rwd", 1));
        vars.put(17, new DummyVariable(17, "engineRunning", "boolean", false, 2));
        vars.put(18, new DummyVariable(18, "speed", "integer", 55, 2));
        vars.put(19, new DummyVariable(19, "frontLeftTirePSI", "integer", 30, 2));
        vars.put(20, new DummyVariable(20, "frontRightTirePSI", "integer", 30, 2));
        vars.put(21, new DummyVariable(21, "backLeftTirePSI", "integer", 30, 2));
        vars.put(22, new DummyVariable(22, "backRightTirePSI", "integer", 30, 2));
        vars.put(23, new DummyVariable(23, "oilFluidLevel", "integer", 255, 2));
        vars.put(24, new DummyVariable(24, "coolantFluidLevel", "integer", 255, 2));
        vars.put(25, new DummyVariable(25, "powerSteeringFluidLevel", "integer", 255, 2));
        vars.put(26, new DummyVariable(26, "brakeFluidLevel", "integer", 255, 2));
        vars.put(27, new DummyVariable(27, "windshieldFluidLevel", "integer", 255, 2));
        vars.put(28, new DummyVariable(28, "frontLeftDoorLocked", "boolean", false, 1));
        vars.put(29, new DummyVariable(29, "frontRightDoorLocked", "boolean", false, 1));
        vars.put(30, new DummyVariable(30, "backLeftDoorLocked", "boolean", false, 1));
        vars.put(31, new DummyVariable(31, "backRightDoorLocked", "boolean", false, 1));
        vars.put(32, new DummyVariable(32, "frontLeftDoorOpen", "boolean", false, 1));
        vars.put(33, new DummyVariable(33, "frontRightDoorOpen", "boolean", false, 1));
        vars.put(34, new DummyVariable(34, "backLeftDoorOpen", "boolean", false, 1));
        vars.put(35, new DummyVariable(35, "backRightDoorOpen", "boolean", false, 1));
        vars.put(36, new DummyVariable(36, "trunkOpen", "boolean", false, 1));
        vars.put(37, new DummyVariable(37, "frontLeftWindowLevel", "integer", 255, 1));
        vars.put(38, new DummyVariable(38, "frontRightWindowLevel", "integer", 255, 1));
        vars.put(39, new DummyVariable(39, "backLeftWindowLevel", "integer", 255, 1));
        vars.put(40, new DummyVariable(40, "backRightWindowLevel", "integer", 255, 1));
        for (int i = 41; i <= 100; i++) {
            DummyVariable var = new DummyVariable(i, String.format("reserved%d", i), "null",
                    "null", 1);
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
            vars.put(var.getId(), new DummyVariable(var.getId(), var.getName(), var.getType(),
                    var.getValue(),var.getProtection()));
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
