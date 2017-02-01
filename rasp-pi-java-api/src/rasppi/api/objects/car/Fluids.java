package rasppi.api.objects.car;

import rasppi.api.objects.VariableObject;

public class Fluids {
    private int mOilFluidLevel;
    private int mCoolantFluidLevel;
    private int mPowerSteeringFluidLevel;
    private int mBrakeFluidLevel;
    private int mWindShieldFluidLevel;

    public Fluids() {
        loadVariables();
    }

    private void loadVariables()  {
        VariableObject tmp = new VariableObject();
        tmp.load(23);
        mOilFluidLevel = (int) tmp.getValue();
        tmp.load(24);
        mCoolantFluidLevel = (int) tmp.getValue();
        tmp.load(25);
        mPowerSteeringFluidLevel = (int) tmp.getValue();
        tmp.load(26);
        mBrakeFluidLevel = (int) tmp.getValue();
        tmp.load(27);
        mWindShieldFluidLevel = (int) tmp.getValue();
    }

    public int getOilFluidLevel() {
        return mOilFluidLevel;
    }

    public int getCoolantFluidLevel() {
        return mCoolantFluidLevel;
    }

    public int getPowerSteeringFluidLevel() {
        return mPowerSteeringFluidLevel;
    }

    public int getBrakeFluidLevel() {
        return mBrakeFluidLevel;
    }

    public int getWindShieldFluidLevel() {
        return mWindShieldFluidLevel;
    }
}
