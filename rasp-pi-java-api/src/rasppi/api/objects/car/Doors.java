package rasppi.api.objects.car;

import rasppi.api.objects.VariableObject;

public class Doors {
    private boolean mFrontLeftDoorLocked;
    private boolean mFrontRightDoorLocked;
    private boolean mBackLeftDoorLocked;
    private boolean mBackRightDoorLocked;
    private boolean mFrontLeftDoorOpened;
    private boolean mFrontRightDoorOpened;
    private boolean mBackLeftDoorOpened;
    private boolean mBackRightDoorOpened;
    private boolean mTrunkOpen;

    public Doors() {
        loadVariables();
    }

    private void loadVariables() {
        VariableObject tmp = new VariableObject();
        tmp.load(28);
        mFrontLeftDoorLocked = (boolean) tmp.getValue();
        tmp.load(29);
        mFrontRightDoorLocked = (boolean) tmp.getValue();
        tmp.load(30);
        mBackLeftDoorLocked = (boolean) tmp.getValue();
        tmp.load(31);
        mBackRightDoorLocked = (boolean) tmp.getValue();
        tmp.load(32);
        mFrontLeftDoorOpened = (boolean) tmp.getValue();
        tmp.load(33);
        mFrontRightDoorOpened = (boolean) tmp.getValue();
        tmp.load(34);
        mBackLeftDoorOpened = (boolean) tmp.getValue();
        tmp.load(35);
        mBackRightDoorOpened = (boolean) tmp.getValue();
        tmp.load(36);
        mTrunkOpen = (boolean) tmp.getValue();
    }

    public boolean isFrontLeftDoorLocked() {
        return mFrontLeftDoorLocked;
    }

    public boolean isFrontRightDoorLocked() {
        return mFrontRightDoorLocked;
    }

    public boolean isBackLeftDoorLocked() {
        return mBackLeftDoorLocked;
    }

    public boolean isBackRightDoorLocked() {
        return mBackRightDoorLocked;
    }

    public boolean isFrontLeftDoorOpened() {
        return mFrontLeftDoorOpened;
    }

    public boolean isFrontRightDoorOpened() {
        return mFrontRightDoorOpened;
    }

    public boolean isBackLeftDoorOpened() {
        return mBackLeftDoorOpened;
    }

    public boolean isBackRightDoorOpened() {
        return mBackRightDoorOpened;
    }

    public boolean isTrunkOpen() {
        return mTrunkOpen;
    }
}
