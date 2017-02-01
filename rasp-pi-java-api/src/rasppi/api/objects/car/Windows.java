package rasppi.api.objects.car;

import rasppi.api.objects.VariableObject;

public class Windows {
    private int mFrontLeftWindowLevel;
    private int mFrontRightWindowLevel;
    private int mBackLeftWindowLevel;
    private int mBackRightWindowLevel;

    public Windows() {
        loadVariables();
    }

    private void loadVariables() {
        VariableObject tmp = new VariableObject();
        tmp.load(37);
        mFrontLeftWindowLevel = (int) tmp.getValue();
        tmp.load(38);
        mFrontRightWindowLevel = (int) tmp.getValue();
        tmp.load(39);
        mBackLeftWindowLevel = (int) tmp.getValue();
        tmp.load(40);
        mBackRightWindowLevel = (int) tmp.getValue();
    }

    public int getFrontLeftWindowLevel() {
        return mFrontLeftWindowLevel;
    }

    public int getFrontRightWindowLevel() {
        return mFrontRightWindowLevel;
    }

    public int getBackLeftWindowLevel() {
        return mBackLeftWindowLevel;
    }

    public int getBackRightWindowLevel() {
        return mBackRightWindowLevel;
    }
}
