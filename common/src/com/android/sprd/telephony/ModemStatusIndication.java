package com.android.sprd.telephony;

public class ModemStatusIndication {
    int status;
    String assertInfo;

    public ModemStatusIndication() {
    }

    public ModemStatusIndication(int status, String assertInfo) {
        this.status = status;
        this.assertInfo = assertInfo;
    }

    public int getStatus() {
        return status;
    }

    public String getAssertInfo() {
        return assertInfo;
    }

    @Override
    public String toString() {
        return "ModemStatusIndication (status="
                + status
                + ", assertInfo="
                + assertInfo
                + ")";
    }
}
