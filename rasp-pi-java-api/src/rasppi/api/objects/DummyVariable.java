package rasppi.api.objects;

/**
 * Created by Dakota on 10/30/2016.
 */
public class DummyVariable {
    public Integer ID;
    public String NAME;
    public String TYPE;
    public Object VALUE;
    public Integer PROTECTION;

    public DummyVariable(Integer ID, String NAME, String TYPE, Object VALUE, Integer PROTECTION) {
        this.ID = ID;
        this.NAME = NAME;
        this.TYPE = TYPE;
        this.VALUE = VALUE;
        this.PROTECTION = PROTECTION;
    }
}
