package rasppi.api.objects;

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

    @Override
    public String toString(){
        return "("+this.ID.toString()+", "+this.NAME.toString()+", "+this.TYPE.toString()+", "+this.VALUE.toString()+
                ", "+this.PROTECTION.toString()+")";
    }

    /**
     * toMDTable:
     *      Helper method that returns the vraiable as a formatted MD Table row
     * @return
     */
    public String toMDTable(){
        return "| "+this.ID.toString()+" | "+this.NAME.toString()+" |  "+this.TYPE.toString()+" |  "+this.VALUE.toString()+
                " |  "+this.PROTECTION.toString()+" | ";
    }
}
