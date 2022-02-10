
package com.android.sprd.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.data.DataProfile;
import android.telephony.IccOpenLogicalChannelResponse;
import android.os.SystemProperties;
import android.content.res.Resources;

import com.android.sprd.telephony.aidl.IRadioInteractor;
import com.android.sprd.telephony.linkturbo.LteSpeedAndSignalStrengthInfo;

import static com.android.sprd.telephony.RIConstants.RADIOINTERACTOR_SERVER;

public class RadioInteractor {

    IRadioInteractor mRadioInteractorProxy;
    private static RadioInteractorFactory sRadioInteractorFactory;
    private static final String TAG = "RadioInteractor";

    Context mContext;

    public RadioInteractor(Context context) {
        mContext = context;
        mRadioInteractorProxy = IRadioInteractor.Stub
                .asInterface(ServiceManager.getService(RADIOINTERACTOR_SERVER));
    }

    public void listen(RadioInteractorCallbackListener radioInteractorCallbackListener,
                       int events) {
        this.listen(radioInteractorCallbackListener, events, true);
    }

    /**
     * Registers a listener object to receive notification of changes
     * in specified telephony states.
     * <p>
     * To register a listener, pass a {@link RadioInteractorCallbackListener} and specify at least
     * one radioInteractor event in the events argument.
     *
     * At registration, and when a specified event changes, the RadioInteractor invokes
     * the appropriate callback method on the listener object and passes the current (updated)
     * values.
     * <p>
     * To un-register a listener, pass the listener object and set the events argument to
     * {@link RadioInteractorCallbackListener#LISTEN_NONE LISTEN_NONE} (0).
     *

     * @param radioInteractorCallbackListener The {@link RadioInteractorCallbackListener} object to
     *                 register (or unregister)
     * @param events The radiointeractor events to the listener,
     *               as a bitwise-OR combination of {@link RadioInteractorCallbackListener}
     *               LISTEN_ flags.
     *
     * @param notifyNow if true, notify when register.
     */
    public void listen(RadioInteractorCallbackListener radioInteractorCallbackListener,
                       int events, boolean notifyNow) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(radioInteractorCallbackListener.mSlotId);
            if (rih != null) { // for phone process application
                if (events == RadioInteractorCallbackListener.LISTEN_NONE) {
                    if (mRadioInteractorProxy != null) {
                        mRadioInteractorProxy.listenForSlot(radioInteractorCallbackListener.mSlotId,
                                radioInteractorCallbackListener.mCallback,
                                RadioInteractorCallbackListener.LISTEN_NONE, notifyNow);
                    }
                    rih.unregisterForUnsolRadioInteractor(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForRiConnected(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForRadioInteractorEmbms(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForsetOnVPCodec(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForsetOnVPFallBack(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForsetOnVPString(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForsetOnVPRemoteMedia(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForsetOnVPMMRing(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForsetOnVPFail(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForsetOnVPRecordVideo(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForsetOnVPMediaStart(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForClearCodeFallback(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForRauSuccess(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForExpireSim(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForEarlyMedia(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForNetowrkErrorCode(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForAvailableNetworks(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForHdStatusChanged(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForSubsidyLock(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForCnap(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForSignalConnectionStatus(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForSmartNrChanged(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForNrCfgInfo(radioInteractorCallbackListener.mHandler);
                    rih.unregisterForModemStateChanged(radioInteractorCallbackListener.mHandler);
                    return;
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_RADIOINTERACTOR_EVENT) != 0) {
                    rih.unsolicitedRegisters(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_RADIOINTERACTOR_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_RI_CONNECTED_EVENT) != 0) {
                    rih.registerForRiConnected(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_RI_CONNECTED_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_RADIOINTERACTOR_EMBMS_EVENT) != 0) {
                    rih.registerForRadioInteractorEmbms(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_RADIOINTERACTOR_EMBMS_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_VIDEOPHONE_CODEC_EVENT) != 0) {
                    rih.registerForsetOnVPCodec(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_VIDEOPHONE_CODEC_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_VIDEOPHONE_DSCI_EVENT) != 0) {
                    rih.registerForsetOnVPFallBack(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_VIDEOPHONE_DSCI_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_VIDEOPHONE_STRING_EVENT) != 0) {
                    rih.registerForsetOnVPString(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_VIDEOPHONE_STRING_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_VIDEOPHONE_REMOTE_MEDIA_EVENT) != 0) {
                    rih.registerForsetOnVPRemoteMedia(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_VIDEOPHONE_REMOTE_MEDIA_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_VIDEOPHONE_MM_RING_EVENT) != 0) {
                    rih.registerForsetOnVPMMRing(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_VIDEOPHONE_MM_RING_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_VIDEOPHONE_RELEASING_EVENT) != 0) {
                    rih.registerForsetOnVPFail(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_VIDEOPHONE_RELEASING_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_VIDEOPHONE_RECORD_VIDEO_EVENT) != 0) {
                    rih.registerForsetOnVPRecordVideo(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_VIDEOPHONE_RECORD_VIDEO_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_VIDEOPHONE_MEDIA_START_EVENT) != 0) {
                    rih.registerForsetOnVPMediaStart(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_VIDEOPHONE_MEDIA_START_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_RAU_SUCCESS_EVENT) != 0) {
                    rih.registerForRauSuccess(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_RAU_SUCCESS_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_CLEAR_CODE_FALLBACK_EVENT) != 0) {
                    rih.registerForClearCodeFallback(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_CLEAR_CODE_FALLBACK_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_EXPIRE_SIM_EVENT) != 0) {
                    rih.registerForExpireSim(
                            radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_EXPIRE_SIM_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_EARLY_MEDIA_EVENT) != 0) {
                    rih.registerForEarlyMedia(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_EARLY_MEDIA_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_NETWORK_ERROR_CODE_EVENT) != 0) {
                    rih.registerForNetowrkErrorCode(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_NETWORK_ERROR_CODE_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_AVAILAVLE_NETWORKS_EVENT) != 0) {
                    rih.registerForAvailableNetworks(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_AVAILAVLE_NETWORKS_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_HD_STATUS_CHANGED_EVENT) != 0) {
                    rih.registerForHdStatusChanged(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_HD_STATUS_CHANGED_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_SUBSIDYLOCK_EVENT) != 0) {
                    rih.registerForSubsidyLock(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_SUBSIDYLOCK_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_CNAP_EVENT) != 0) {
                    rih.registerForCnap(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_CNAP_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_SIGNAL_CONNECTION_STATUS_EVENT) != 0) {
                    rih.registerForSignalConnectionStatus(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_SIGNAL_CONNECTION_STATUS_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_SMART_NR_CHANGED_EVENT) != 0) {
                    rih.registerForSmartNrChanged(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_SMART_NR_CHANGED_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_NR_CFG_INFO_EVENT) != 0) {
                    rih.registerForNrCfgInfo(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_NR_CFG_INFO_EVENT);
                }
                if ((events & RadioInteractorCallbackListener.LISTEN_MODEM_STATE_CHANGED_EVENT) != 0) {
                    rih.registerForModemStateChanged(radioInteractorCallbackListener.mHandler,
                            RadioInteractorCallbackListener.LISTEN_MODEM_STATE_CHANGED_EVENT);
                }
                return;
            }
            // for other process application
            if (mRadioInteractorProxy != null) {
                mRadioInteractorProxy.listenForSlot(radioInteractorCallbackListener.mSlotId,
                        radioInteractorCallbackListener.mCallback,
                        events, notifyNow);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private RadioInteractorHandler getRadioInteractorHandler(int slotId) {
        UtilLog.logd(TAG, "RadioInteractorFactory:  " + sRadioInteractorFactory
                + "  RadioInteractorFactory class " + RadioInteractor.class.hashCode());
        sRadioInteractorFactory = RadioInteractorFactory.getInstance();
        if (sRadioInteractorFactory != null) {
            return sRadioInteractorFactory.getRadioInteractorHandler(slotId);
        }
        return null;
    }

    // This interface will be eliminated
    public int sendAtCmd(String oemReq, String[] oemResp, int slotId) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(slotId);
            if (rih != null) {
                return rih.invokeOemRILRequestStrings(oemReq, oemResp);
            }
            if (mRadioInteractorProxy != null) {
                return mRadioInteractorProxy.sendAtCmd(oemReq, oemResp, slotId);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getSimCapacity(int slotId) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(slotId);
            if (rih != null) {
                return rih.getSimCapacity();
            }
            if (mRadioInteractorProxy != null) {
                return mRadioInteractorProxy.getSimCapacity(slotId);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void enableRauNotify(int slotId) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(slotId);
            if (rih != null) {
                rih.enableRauNotify();
                return;
            }
            if (mRadioInteractorProxy != null) {
                mRadioInteractorProxy.enableRauNotify(slotId);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the ATR of the UICC if available.
     *
     * @param slotId int
     * @return The ATR of the UICC if available.
     */
    public String iccGetAtr(int slotId) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(slotId);
            if (rih != null) {
                return rih.iccGetAtr();
            }
            if (mRadioInteractorProxy != null) {
                return mRadioInteractorProxy.iccGetAtr(slotId);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean queryHdVoiceState(int slotId) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(slotId);
            if (rih != null) {
                return rih.queryHdVoiceState();
            }
            if (mRadioInteractorProxy != null) {
                return mRadioInteractorProxy.queryHdVoiceState(slotId);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Store Sms To Sim
     *
     * @param enable True is store SMS to SIM card,false is store to phone.
     * @param slotId int
     * @return whether successful store Sms To Sim
     */
    public boolean storeSmsToSim(boolean enable, int slotId) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(slotId);
            if (rih != null) {
                return rih.storeSmsToSim(enable);
            }
            if (mRadioInteractorProxy != null) {
                return mRadioInteractorProxy.storeSmsToSim(enable, slotId);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Return SMS Storage Mode.
     *
     * @param slotId int
     * @return SMS Storage Mode
     */
    public String querySmsStorageMode(int slotId) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(slotId);
            if (rih != null) {
                return rih.querySmsStorageMode();
            }
            if (mRadioInteractorProxy != null) {
                return mRadioInteractorProxy.querySmsStorageMode(slotId);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Explicit Transfer Call REFACTORING
     *
     * @param slotId
     */
    public void explicitCallTransfer(int slotId) {
        try {
            if (mRadioInteractorProxy != null) {
                mRadioInteractorProxy.explicitCallTransfer(slotId);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /* add for TV use in phone process@{*/
    public void dialVP(String address, String sub_address, int clirMode, Message result,
                       int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.dialVP(address, sub_address, clirMode, result);
        }
    }

    public void codecVP(int type, Bundle param, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.codecVP(type, param, result);
        }
    }

    public void fallBackVP(Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.fallBackVP(result);
        }
    }

    public void sendVPString(String str, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.sendVPString(str, result);
        }
    }

    public void controlVPLocalMedia(int datatype, int sw, boolean bReplaceImg, Message result,
                                    int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.controlVPLocalMedia(datatype, sw, bReplaceImg, result);
        }
    }

    public void controlIFrame(boolean isIFrame, boolean needIFrame, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.controlIFrame(isIFrame, needIFrame, result);
        }
    }
    /* @} */

    /* Add for trafficClass @{ */
    public void requestDCTrafficClass(int type, int slotId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(slotId);
        if (rih != null) {
            rih.requestDCTrafficClass(type);
        }
    }
    /* @} */

    /* Add for do recovery @{ */
    public void requestReattach(int slotId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(slotId);
        if (rih != null) {
            rih.requestReattach();
        }
    }
    /* @} */

    /*SPRD: bug618350 add single pdp allowed by plmns feature@{*/
    public void requestSetSinglePDNByNetwork(boolean isSinglePDN, int slotId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(slotId);
        if (rih != null) {
            rih.requestSetSinglePDNByNetwork(isSinglePDN);
        }
    }
    /* @} */

    /* Add for Data Clear Code from Telcel @{ */
    public void setLteEnabled(boolean enable, int slotId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(slotId);
        if (rih != null) {
            rih.setLteEnabled(enable);
        }
    }

    public void attachDataConn(boolean enable, int slotId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(slotId);
        if (rih != null) {
            rih.attachDataConn(enable);
        }
    }
    /* @} */

    public void forceDetachDataConn(Messenger messenger, int phoneId) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
            if (rih != null) {
                rih.forceDetachDataConn(messenger);
            } else {
                if (mRadioInteractorProxy != null) {
                    mRadioInteractorProxy.forceDetachDataConn(messenger, phoneId);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    /* @} */

    /**
     * Add for shutdown optimization
     *
     * @param slotId int
     */
    public boolean requestShutdown(int slotId) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(slotId);
            if (rih != null) {
                return rih.requestShutdown();
            }
            if (mRadioInteractorProxy != null) {
                return mRadioInteractorProxy.requestShutdown(slotId);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get simlock remain times
     *
     * @param type   ref to IccCardStatusEx.UNLOCK_XXXX
     * @param slotId int
     * @return remain times
     */
    public int getSimLockRemainTimes(int type, int slotId) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(slotId);
            if (rih != null) {
                return rih.getSimLockRemainTimes(type);
            }
            if (mRadioInteractorProxy != null) {
                return mRadioInteractorProxy.getSimLockRemainTimes(type, slotId);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Get simlock status by nv
     *
     * @param type   ref to IccCardStatusEx.UNLOCK_XXXX
     * @param slotId int
     * @return status: unlocked: 0 locked: 1
     */
    public int getSimLockStatus(int type, int slotId) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(slotId);
            if (rih != null) {
                return rih.getSimLockStatus(type);
            }
            if (mRadioInteractorProxy != null) {
                return mRadioInteractorProxy.getSimLockStatus(type, slotId);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * lock/unlock for one key simlock
     *
     * @param facility  String
     * @param lockState boolean: lock:true unlock: false
     * @param response  Message
     * @param slotId    int
     */
    public void setFacilityLockByUser(String facility, boolean lockState,
                                      Message response, int slotId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(slotId);
        if (rih != null) {
            rih.setFacilityLockByUser(facility, lockState, response);
            return;
        }
        AsyncResult.forMessage(response, null, new Throwable(
                "simlock lock/unlock should be called in phone process!"));
        response.sendToTarget();
    }

    /**
     * Set SIM power
     */
    public void setSimPower(int phoneId, boolean enabled) {
        if (mContext != null) {
            try {
                RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
                if (rih != null) {
                    rih.setSimPower(mContext.getPackageName(), enabled);
                } else {
                    if (mRadioInteractorProxy != null) {
                        mRadioInteractorProxy.setSimPower(mContext.getPackageName(), phoneId, enabled);
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public int setPreferredNetworkType(int phoneId, int networkType) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
            if (rih != null) {
                return rih.setPreferredNetworkType(networkType);
            } else {
                if (mRadioInteractorProxy != null) {
                    return mRadioInteractorProxy.setPreferredNetworkType(phoneId, networkType);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void updateRealEccList(String realEccList, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.updateRealEccList(realEccList);
        }
    }

    public int queryColp(int slotId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(slotId);
        if (rih != null) {
            return rih.queryColp();
        }
        return -1;
    }

    public int queryColr(int slotId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(slotId);
        if (rih != null) {
            return rih.queryColr();
        }
        return -1;
    }

    public void updateOperatorName(String plmn, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.updateOperatorName(plmn);
        }
    }

    /***
     * In the case of forbidden card state,get real sim state in slot.
     * @param phoneId
     * @return
     * IccCardStatusEx.CardState.CARDSTATE_ABSENT.ordinal()
     * IccCardStatusEx.CardState.CARDSTATE_PRESENT.ordinal()
     * IccCardStatusEx.CardState.CARDSTATE_ERROR.ordinal()
     */
    public int getRealSimSatus(int phoneId) {
        try {
            if (mRadioInteractorProxy != null) {
                return mRadioInteractorProxy.getRealSimSatus(phoneId);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setXcapIPAddress(String ifName, String ipv4Addr, String ipv6Addr,
                                 Message response, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.setXcapIPAddress(ifName, ipv4Addr, ipv6Addr, response);
        }
    }

    public int[] getSimlockDummys(int phoneId) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
            if (rih != null) {
                return rih.getSimlockDummys();
            } else {
                if (mRadioInteractorProxy != null) {
                    return mRadioInteractorProxy.getSimlockDummys(phoneId);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSimlockWhitelist(int type, int phoneId) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
            if (rih != null) {
                return rih.getSimlockWhitelist(type);
            } else {
                if (mRadioInteractorProxy != null) {
                    return mRadioInteractorProxy.getSimlockWhitelist(type, phoneId);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateCLIP(int enable, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.updateCLIP(enable, result);
        }
    }

    public void setTPMRState(int state, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.setTPMRState(state, result);
        }
    }

    public void getTPMRState(Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.getTPMRState(result);
        }
    }

    public void setVideoResolution(int resolution, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.setVideoResolution(resolution, result);
        }
    }

    public void enableLocalHold(boolean enable, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.enableLocalHold(enable, result);
        }
    }

    public void enableWiFiParamReport(boolean enable, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.enableWiFiParamReport(enable, result);
        }
    }

    public void callMediaChangeRequestTimeOut(int callId, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.callMediaChangeRequestTimeOut(callId, result);
        }
    }

    public void setLocalTone(int data, int phoneId) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
            if (rih != null) {
                rih.setLocalTone(data);
            } else {
                if (mRadioInteractorProxy != null) {
                    mRadioInteractorProxy.setLocalTone(data, phoneId);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public int updatePlmn(int phoneId, int type, int action, String plmn,
                          int act1, int act2, int act3) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            return rih.updatePlmn(type, action, plmn, act1, act2, act3);
        }
        try {
            if (mRadioInteractorProxy != null) {
                return mRadioInteractorProxy.updatePlmn(phoneId, type, action, plmn, act1, act2, act3);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String queryPlmn(int phoneId, int type) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            return rih.queryPlmn(type);
        }
        try {
            if (mRadioInteractorProxy != null) {
                return mRadioInteractorProxy.queryPlmn(phoneId, type);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setSimPowerReal(int phoneId, Boolean enable) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.setSimPowerReal(mContext.getPackageName(), enable);
            return;
        }
        try {
            if (mRadioInteractorProxy != null) {
                mRadioInteractorProxy.setSimPowerReal(mContext.getPackageName(), phoneId, enable);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void resetModem(int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.resetModem();
            return;
        }
        try {
            if (mRadioInteractorProxy != null) {
                mRadioInteractorProxy.resetModem(phoneId);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public String getRadioPreference(int phoneId, String key) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            return rih.getRadioPreference(key);
        }
        try {
            if (mRadioInteractorProxy != null) {
                return mRadioInteractorProxy.getRadioPreference(phoneId, key);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setRadioPreference(int phoneId, String key, String value) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.setRadioPreference(key, value);
        }
        try {
            if (mRadioInteractorProxy != null) {
                mRadioInteractorProxy.setRadioPreference(phoneId, key, value);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void getImsCurrentCalls(Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.getImsCurrentCalls(result);
        }
    }

    public void setImsVoiceCallAvailability(int state, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.setImsVoiceCallAvailability(state, result);
        }
    }

    public void getImsVoiceCallAvailability(Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.getImsVoiceCallAvailability(result);
        }
    }

    public void initISIM(Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.initISIM(result);
        }
    }

    public void requestVolteCallMediaChange(int action, int callId, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.requestVolteCallMediaChange(action, callId, result);
        }
    }

    public void responseVolteCallMediaChange(boolean isAccept, int callId, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.responseVolteCallMediaChange(isAccept, callId, result);
        }
    }

    public void setImsSmscAddress(String smsc, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.setImsSmscAddress(smsc, result);
        }
    }

    public void requestVolteCallFallBackToVoice(int callId, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.requestVolteCallFallBackToVoice(callId, result);
        }
    }

    public void setExtInitialAttachApn(DataProfile dataProfileInfo, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.setExtInitialAttachApn(dataProfileInfo, result);
        }
    }

    public void queryCallForwardStatus(int cfReason, int serviceClass,
                                       String number, String ruleSet, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.queryCallForwardStatus(cfReason, serviceClass, number, ruleSet, result);
        }
    }

    public void setCallForward(int action, int cfReason, int serviceClass,
                               String number, int timeSeconds, String ruleSet, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.setCallForward(action, cfReason, serviceClass, number,
                    timeSeconds, ruleSet, result);
        }
    }

    public void requestInitialGroupCall(String numbers, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.requestInitialGroupCall(numbers, result);
        }
    }

    public void requestAddGroupCall(String numbers, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.requestAddGroupCall(numbers, result);
        }
    }

    public void enableIms(boolean enable, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.enableIms(enable, result);
        }
    }

    public void getImsBearerState(Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.getImsBearerState(result);
        }
    }

    public void requestImsHandover(int type, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.requestImsHandover(type, result);
        }
    }

    public void notifyImsHandoverStatus(int status, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.notifyImsHandoverStatus(status, result);
        }
    }

    public void notifyImsNetworkInfo(int type, String info, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.notifyImsNetworkInfo(type, info, result);
        }
    }

    public void notifyImsCallEnd(int type, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.notifyImsCallEnd(type, result);
        }
    }

    public void notifyVoWifiEnable(boolean enable, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.notifyVoWifiEnable(enable, result);
        }
    }

    public void notifyVoWifiCallStateChanged(boolean incall, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.notifyVoWifiCallStateChanged(incall, result);
        }
    }

    public void notifyDataRouter(Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.notifyDataRouter(result);
        }
    }

    public void imsHoldSingleCall(int callid, boolean enable, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.imsHoldSingleCall(callid, enable, result);
        }
    }

    public void imsMuteSingleCall(int callid, boolean enable, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.imsMuteSingleCall(callid, enable, result);
        }
    }

    public void imsSilenceSingleCall(int callid, boolean enable, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.imsSilenceSingleCall(callid, enable, result);
        }
    }

    public void imsEnableLocalConference(boolean enable, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.imsEnableLocalConference(enable, result);
        }
    }

    public void notifyHandoverCallInfo(String callInfo, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.notifyHandoverCallInfo(callInfo, result);
        }
    }

    public void getSrvccCapbility(Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.getSrvccCapbility(result);
        }
    }

    public void getImsPcscfAddress(Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.getImsPcscfAddress(result);
        }
    }

    public void setImsPcscfAddress(String addr, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.setImsPcscfAddress(addr, result);
        }
    }

    public void queryFacilityLockForAppExt(String facility, String password,
                                           int serviceClass, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.queryFacilityLockForAppExt(facility, password, serviceClass, result);
        }
    }

    public void getImsRegAddress(Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.getImsRegAddress(result);
        }
    }

    public void getImsPaniInfo(Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.getImsPaniInfo(result);
        }
    }

    public int getPreferredNetworkType(int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            return rih.getPreferredNetworkType();
        }
        try {
            if (mRadioInteractorProxy != null) {
                return mRadioInteractorProxy.getPreferredNetworkType(phoneId);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void enableRadioPowerFallback(boolean enable, int phoneId) {
        if (!(SystemProperties.getBoolean("ro.vendor.radio.fallback.enable", false))) {
            return;
        }
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
            if (rih != null) {
                rih.enableRadioPowerFallback(enable);
            } else {
                if (mRadioInteractorProxy != null) {
                    mRadioInteractorProxy.enableRadioPowerFallback(enable, phoneId);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Set switch status and current application except service status
     *  @param phoneId: for which sim card to set.
     *  @param onOff: Set the state of the switch on or off.
     *  @param value: except service state of current application.
     */
    public void setPsDataOff(int phoneId, boolean onOff, int value) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.setPsDataOff(onOff, value);
        } else {
            try {
                if (mRadioInteractorProxy != null) {
                    mRadioInteractorProxy.setPsDataOff(phoneId, onOff, value);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public int getCnap(int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            return rih.getCnap();
        }
        return -1;
    }

    public void setLocationInfo(String longitude, String latitude, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.setLocationInfo(longitude, latitude, result);
        }
    }

    public void setEmergencyOnly(boolean emergencyOnly, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.setEmergencyOnly(emergencyOnly, result);
        }
    }

    public int getSubsidyLockStatus(int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            return rih.getSubsidyLockStatus();
        }
        return -1;
    }

    public void setImsUserAgent(String sipUserAgent, Message result, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.setImsUserAgent(sipUserAgent, result);
        }
    }

    /**
     *  Returns the volte white information, to indicate whether current PLMN in volte white list.
     *
     *  @param phoneId for which sim card to check volte white information.
     *  @return the volte white information
     */
    public int getVoLTEAllowedPLMN(int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            return rih.getVoLTEAllowedPLMN();
        }
        return -1;
    }

    /**
     *  Query root node info
     *  @param phoneId for which sim card to query root node info.
     */
    public void queryRootNode(int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.queryRootNode();
        }
    }

    /**
     * Set sms usging IMS is avaliable or not.
     *
     * @param type for smm over ip state.
     *  - 0 for SMS using IMS is not available
     *  - 1 for SMS using IMS is available
     * @param phoneId for which sim card to set sms over ip type.
     *
     * */
    public void setSmsBearer(int type, int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            rih.setSmsBearer(type);
        }
    }

    /**
     * Get sms usging IMS is avaliable or not.
     *
     * @param phoneId for which sim card to get sms over ip type.
     * @return the sms over ip type.
     * Valid return results are:
     *  - 0 for SMS using IMS is not available
     *  - 1 for SMS using IMS is available
     * */
    public int getSmsBearer(int phoneId) {
        RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
        if (rih != null) {
            return rih.getSmsBearer();
        }
        return 0;
    }


    /**
     * Get lte speed and singnal strength info.
     *
     * @param phoneId for which sim card to get info.
     * @return lte speed and singnal strength info. see#LteSpeedAndSignalStrengthInfo
     */
    public LteSpeedAndSignalStrengthInfo getLteSpeedAndSignalStrength(int phoneId) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
            if (rih != null) {
                return rih.getLteSpeedAndSignalStrength();
            } else {
                if (mRadioInteractorProxy != null) {
                    return mRadioInteractorProxy.getLteSpeedAndSignalStrength(phoneId);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void enableNrSwitch(int phoneId, int mode, int enable) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
            if (rih != null) {
                rih.enableNrSwitch(mode, enable);
            } else {
                if (mRadioInteractorProxy != null) {
                    mRadioInteractorProxy.enableNrSwitch(phoneId, mode, enable);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setUsbShareStateSwitch(int phoneId, boolean enable) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
            if (rih != null) {
                rih.setUsbShareStateSwitch(enable);
            } else {
                if (mRadioInteractorProxy != null) {
                    mRadioInteractorProxy.setUsbShareStateSwitch(phoneId, enable);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public int setStandAlone(int type, int phoneId) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
            if (rih != null) {
                return rih.setStandAlone(type);
            } else {
                if (mRadioInteractorProxy != null) {
                    return mRadioInteractorProxy.setStandAlone(type, phoneId);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getStandAlone(int phoneId) {
        try {
            RadioInteractorHandler rih = getRadioInteractorHandler(phoneId);
            if (rih != null) {
                return rih.getStandAlone();
            } else {
                if (mRadioInteractorProxy != null) {
                    return mRadioInteractorProxy.getStandAlone(phoneId);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
