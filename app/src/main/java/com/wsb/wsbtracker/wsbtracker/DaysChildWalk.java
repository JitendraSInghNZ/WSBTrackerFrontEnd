package com.wsb.wsbtracker.wsbtracker;

/**
 * Created by jitus_000 on 9/05/2016.
 */
public class DaysChildWalk {
    private int mWalkDayInt;
    private boolean mAm;
    private boolean mPm;

    public int getWalkDayInt() {
        return mWalkDayInt;
    }

    public void setWalkDayInt(int walkDayInt) {
        mWalkDayInt = walkDayInt;
    }

    public boolean isAm(){
        return mAm;
    }

    public void setAm(boolean am) {
        mAm = am;
    }

    public boolean isPm() {
        return mPm;
    }

    public void setPm(boolean pm) {
        mPm = pm;
    }
}
