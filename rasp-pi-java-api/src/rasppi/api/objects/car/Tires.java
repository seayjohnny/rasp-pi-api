package rasppi.api.objects.car;

import rasppi.api.objects.VariableObject;

public class Tires {
    private int mFrontLeftPSI;
    private int mFrontRightPSI;
    private int mBackLeftPSI;
    private int mBackRightPSI;

    public Tires() {
        loadVariables();
    }

    private void loadVariables() {
        VariableObject tmp = new VariableObject();
        tmp.load(19);
        mFrontLeftPSI = (int) tmp.getValue();
        tmp.load(20);
        mFrontRightPSI = (int) tmp.getValue();
        tmp.load(21);
        mBackLeftPSI = (int) tmp.getValue();
        tmp.load(22);
        mBackRightPSI = (int) tmp.getValue();
    }

    public int getFrontLeftPSI() {
        return mFrontLeftPSI;
    }

    public int getFrontRightPSI() {
        return mFrontRightPSI;
    }

    public int getBackLeftPSI() {
        return mBackLeftPSI;
    }

    public int getBackRightPSI() {
        return mBackRightPSI;
    }
}
