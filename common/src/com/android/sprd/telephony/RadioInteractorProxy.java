
package com.android.sprd.telephony;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.Message;
import android.os.Messenger;
import android.telephony.IccOpenLogicalChannelResponse;

import com.android.sprd.telephony.UtilLog;
import com.android.sprd.telephony.aidl.IRadioInteractor;
import com.android.sprd.telephony.aidl.IRadioInteractorCallback;
import com.android.sprd.telephony.linkturbo.LteSpeedAndSignalStrengthInfo;

import static com.android.sprd.telephony.RIConstants.RADIOINTERACTOR_SERVER;

public class RadioInteractorProxy extends IRadioInteractor.Stub {
    private static RadioInteractorProxy sInstance;
    private static final String TAG = "RadioInteractorProxy";

    private RadioInteractorNotifier mRadioInteractorNotifier;
    private RadioInteractorHandler[] mRadioInteractorHandler;

    public static RadioInteractorProxy init(Context context,
            RadioInteractorNotifier radioInteractorNotifier) {
        return init(context, radioInteractorNotifier, null);
    }

    public static RadioInteractorProxy init(Context context,
            RadioInteractorNotifier radioInteractorNotifier,
            RadioInteractorHandler[] radioInteractorHandler) {
        synchronized (RadioInteractorProxy.class) {
            if (sInstance == null) {
                sInstance = new RadioInteractorProxy(context,
                        radioInteractorNotifier, radioInteractorHandler);
            } else {
                UtilLog.loge("RadioInteractorProxy", "init() called multiple times!  sInstance = "
                        + sInstance);
            }
            return sInstance;
        }
    }

    public static RadioInteractorProxy getInstance() {
        return sInstance;
    }

    private RadioInteractorProxy(Context context, RadioInteractorNotifier radioInteractorNotifier,
            RadioInteractorHandler[] radioInteractorHandler) {
        mRadioInteractorNotifier = radioInteractorNotifier;
        mRadioInteractorHandler = radioInteractorHandler;
        ServiceManager.addService(RADIOINTERACTOR_SERVER, this);
    }

    private RadioInteractorProxy(Context context) {
        this(context, null, null);
    }

    @Override
    public void listenForSlot(int slotId, IRadioInteractorCallback callback, int events,
            boolean notifyNow) throws RemoteException {
        mRadioInteractorNotifier.listenForSlot(slotId, callback, events, notifyNow);
    }


    @Override
    public int sendAtCmd(String oemReq, String[] oemResp, int slotId) {
        if (checkHandlerValid(slotId)) {
            return mRadioInteractorHandler[slotId].invokeOemRILRequestStrings(oemReq, oemResp);
        }
        return 0;
    }

    @Override
    public String getSimCapacity(int slotId) {
        if (checkHandlerValid(slotId)) {
            return mRadioInteractorHandler[slotId].getSimCapacity();
        }
        return null;
    }

    @Override
    public void enableRauNotify(int slotId) {
        if (checkHandlerValid(slotId)) {
            mRadioInteractorHandler[slotId].enableRauNotify();
        }
    }

    @Override
    public String iccGetAtr(int slotId){
        if (checkHandlerValid(slotId)) {
            String response = (String) mRadioInteractorHandler[slotId].iccGetAtr();
            return response;
        }
        return null;
    }

    /**
     * Check the handler is not null
     * @param slotId int
     * @return true if the handler is not null
     */
    private boolean checkHandlerValid(int slotId){
        try {
            if(mRadioInteractorHandler != null && mRadioInteractorHandler[slotId] != null){
                return true;
            }
            UtilLog.logd("RadioInteractorProxy", "checkHandlerValid: false");
            return false;
        } catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            UtilLog.loge("RadioInteractorProxy", "ArrayIndexOutOfBoundsException occured" +
                    " probably invalid slotId " + slotId);
            return false;
        }
    }
    /* @} */

    public boolean queryHdVoiceState(int slotId) {
        if (checkHandlerValid(slotId)) {
            return mRadioInteractorHandler[slotId].queryHdVoiceState();
        }
        return false;
    }

    public boolean storeSmsToSim(boolean enable, int slotId) {
        if (checkHandlerValid(slotId)) {
            return mRadioInteractorHandler[slotId].storeSmsToSim(enable);
        }
        return false;
    }

    public String querySmsStorageMode(int slotId) {
        if (checkHandlerValid(slotId)) {
            return mRadioInteractorHandler[slotId].querySmsStorageMode();
        }
        return null;
    }

    /**
    * Explicit Transfer Call REFACTORING
    */
    public void explicitCallTransfer(int slotId) {
        if (checkHandlerValid(slotId)) {
            mRadioInteractorHandler[slotId].explicitCallTransfer();
        }
    }

    public boolean requestShutdown(int slotId) {
        if (checkHandlerValid(slotId)) {
            return mRadioInteractorHandler[slotId].requestShutdown();
        }
        return false;
    }

    public int getSimLockRemainTimes(int type, int slotId) {
        if (checkHandlerValid(slotId)) {
            return mRadioInteractorHandler[slotId].getSimLockRemainTimes(type);
        }
        return -1;
    }

    public int getSimLockStatus(int type, int slotId) {
        if (checkHandlerValid(slotId)) {
            return mRadioInteractorHandler[slotId].getSimLockStatus(type);
        }
        return -1;
    }

    public void setSimPower(String packageName,int phoneId, boolean enabled) {
        if (checkHandlerValid(phoneId)) {
            mRadioInteractorHandler[phoneId].setSimPower(packageName,enabled);
        }
    }

    public int setPreferredNetworkType(int phoneId, int networkType) {
        if (checkHandlerValid(phoneId)) {
            return mRadioInteractorHandler[phoneId].setPreferredNetworkType(networkType);
        }
        return -1;
    }

    public int getRealSimSatus(int phoneId) {
        if (checkHandlerValid(phoneId)) {
            return mRadioInteractorHandler[phoneId].getRealSimStatus();
        }
        return 0;
    }

    public int[] getSimlockDummys(int phoneId) {
        UtilLog.logd(TAG,"getSimlockDummys checkHandlerValid "+ checkHandlerValid(phoneId));
        if (checkHandlerValid(phoneId)) {
            return mRadioInteractorHandler[phoneId].getSimlockDummys();
        }
        return null;
    }

    public String getSimlockWhitelist(int type, int phoneId) {
        UtilLog.logd(TAG,"getSimlockWhitelist checkHandlerValid "+ checkHandlerValid(phoneId));
        if (checkHandlerValid(phoneId)) {
            return mRadioInteractorHandler[phoneId].getSimlockWhitelist(type);
        }
        return null;
    }

    public void setLocalTone(int data, int phoneId) {
        UtilLog.logd(TAG,"setLocalTone checkHandlerValid "+ checkHandlerValid(phoneId));
        if (checkHandlerValid(phoneId)) {
            mRadioInteractorHandler[phoneId].setLocalTone(data);
        }
    }

    public int updatePlmn(int phoneId, int type, int action, String plmn,
                          int act1, int act2, int act3){
        if (checkHandlerValid(phoneId)) {
            return mRadioInteractorHandler[phoneId].updatePlmn(type, action, plmn, act1, act2, act3);
        }
        return -1;
    }

    public String queryPlmn(int phoneId, int type){
        if (checkHandlerValid(phoneId)) {
            return mRadioInteractorHandler[phoneId].queryPlmn(type);
        }
        return null;
    }

    public void setSimPowerReal(String packageName,int phoneId, boolean enabled) {
        if (checkHandlerValid(phoneId)) {
            mRadioInteractorHandler[phoneId].setSimPowerReal(packageName,enabled);
        }
    }

    public void resetModem(int phoneId) {
        if (checkHandlerValid(phoneId)) {
            mRadioInteractorHandler[phoneId].resetModem();
        }
    }

    public String getRadioPreference(int phoneId, String key) {
        if (checkHandlerValid(phoneId)) {
            return mRadioInteractorHandler[phoneId].getRadioPreference(key);
        }
        return null;
    }

    public void setRadioPreference(int phoneId, String key, String value) {
        if (checkHandlerValid(phoneId)) {
            mRadioInteractorHandler[phoneId].setRadioPreference(key, value);
        }
    }

    public int getPreferredNetworkType(int phoneId) {
        if (checkHandlerValid(phoneId)) {
            return mRadioInteractorHandler[phoneId].getPreferredNetworkType();
        }
        return -1;
    }

    public void enableRadioPowerFallback(boolean enable, int phoneId) {
        UtilLog.logd(TAG,"enableRadioPowerFallback checkHandlerValid "+ checkHandlerValid(phoneId));
        if (checkHandlerValid(phoneId)) {
            mRadioInteractorHandler[phoneId].enableRadioPowerFallback(enable);
        }
    }

    public void setPsDataOff(int phoneId, boolean onOff, int value) {
        UtilLog.logd(TAG,"setPsDataOff checkHandlerValid "+ checkHandlerValid(phoneId));
        if (checkHandlerValid(phoneId)) {
            mRadioInteractorHandler[phoneId].setPsDataOff(onOff, value);
        }
    }

    public void forceDetachDataConn(Messenger messenger, int phoneId) {
        if (checkHandlerValid(phoneId)) {
            mRadioInteractorHandler[phoneId].forceDetachDataConn(messenger);
        }
    }

    public LteSpeedAndSignalStrengthInfo getLteSpeedAndSignalStrength(int phoneId) {
        UtilLog.logd(TAG,"getLteSpeedAndSignalStrength checkHandlerValid "+ checkHandlerValid(phoneId));
        if (checkHandlerValid(phoneId)) {
            return mRadioInteractorHandler[phoneId].getLteSpeedAndSignalStrength();
        }
        return null;
    }

    public void enableNrSwitch(int phoneId, int mode, int enable) {
        if (checkHandlerValid(phoneId)) {
            mRadioInteractorHandler[phoneId].enableNrSwitch(mode, enable);
        }
    }

    public void setUsbShareStateSwitch(int phoneId, boolean enable) {
        UtilLog.logd(TAG,"setUsbShareStateSwitch checkHandlerValid " + checkHandlerValid(phoneId));
        if (checkHandlerValid(phoneId)) {
            mRadioInteractorHandler[phoneId].setUsbShareStateSwitch(enable);
        }
    }

    public int setStandAlone(int type, int phoneId) {
        if (checkHandlerValid(phoneId)) {
            return mRadioInteractorHandler[phoneId].setStandAlone(type);
        }
        return -1;
    }

    public int getStandAlone(int phoneId) {
        if (checkHandlerValid(phoneId)) {
            return mRadioInteractorHandler[phoneId].getStandAlone();
        }
        return -1;
    }
}
