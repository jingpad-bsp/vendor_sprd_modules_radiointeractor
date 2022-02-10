package com.android.sprd.telephony.aidl;

import com.android.sprd.telephony.aidl.IRadioInteractorCallback;
import android.telephony.IccOpenLogicalChannelResponse;
import android.os.Messenger;
import com.android.sprd.telephony.linkturbo.LteSpeedAndSignalStrengthInfo;

interface IRadioInteractor
{
    void listenForSlot(in int slotId, IRadioInteractorCallback callback, int events, boolean notifyNow);
    int sendAtCmd(in String oemReq, out String[] oemResp, int slotId);
    String getSimCapacity(int slotId);
    void enableRauNotify(int slotId);
    boolean queryHdVoiceState(int slotId);
    boolean storeSmsToSim(boolean enable,int slotId);
    String querySmsStorageMode(int slotId);
    boolean requestShutdown(int slotId);
    String iccGetAtr(int slotId);
    void explicitCallTransfer(int slotId);
    int getSimLockRemainTimes(int type, int slotId);
    int getSimLockStatus(int type, int slotId);
    void setSimPower(String pkgname,int phoneId, boolean enabled);
    int setPreferredNetworkType(int phoneId, int networkType);
    int getRealSimSatus(int phoneId);
    int[] getSimlockDummys(int phoneId);
    String getSimlockWhitelist(int type, int phoneId);
    void setLocalTone(int data, int phoneId);
    int updatePlmn(int phoneId, int type, int action, String plmn,
                          int act1, int act2, int act3);
    String queryPlmn(int phoneId, int type);
    void setSimPowerReal(String pkgname, int phoneId, boolean enabled);
    void resetModem(int phoneId);
    String getRadioPreference(int PhoneId, String key);
    void setRadioPreference(int phoneId, String key, String value);
    int getPreferredNetworkType(int phoneId);
    void enableRadioPowerFallback(boolean enable, int phoneId);
    void forceDetachDataConn(in Messenger messenger, int phoneId);
    void setPsDataOff(int phoneId, boolean onOff, int value);
    LteSpeedAndSignalStrengthInfo getLteSpeedAndSignalStrength(int phoneId);
    void enableNrSwitch(int phoneId, int mode, int enable);
    void setUsbShareStateSwitch(int phoneId, boolean enable);
    int setStandAlone(int type, int phoneId);
    int getStandAlone(int phoneId);
}
