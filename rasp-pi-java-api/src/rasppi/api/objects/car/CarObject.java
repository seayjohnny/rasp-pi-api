package rasppi.api.objects.car;

public class CarObject {
    private String mOwner;
    private String mCarMake;
    private String mCarModel;
    private int mCarYear;
    private int mCarFactoryDate;
    private String mCarColor;
    private String mCarTransmissionType;
    private String mCarWheelDrive;
    private boolean mEngineRunning;
    private int mSpeed;
    private Tires mTires;
    private Fluids mFluids;
    private Doors mDoors;

    public CarObject() {
    }

    public Tires getTires() {
        return mTires;
    }
}
