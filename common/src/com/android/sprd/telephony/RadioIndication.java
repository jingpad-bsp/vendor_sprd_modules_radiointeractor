package com.android.sprd.telephony;

import vendor.sprd.hardware.radio.V1_0.VideoPhoneDSCI;
import vendor.sprd.hardware.radio.V1_0.IExtRadioIndication;
import vendor.sprd.hardware.radio.V1_0.ImsNetworkInfo;
import vendor.sprd.hardware.radio.V1_0.ImsErrorCauseInfo;
import vendor.sprd.hardware.radio.V1_0.SignalConnStatus;
import vendor.sprd.hardware.radio.V1_0.ModemStatusInfo;

import java.util.ArrayList;

import android.os.AsyncResult;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.android.internal.telephony.SignalConnectedStatus;

import com.android.sprd.telephony.aidl.IOperatorNameHandler;

import static com.android.internal.telephony.RILConstants.RIL_UNSOL_RIL_CONNECTED;

import static com.android.sprd.telephony.RIConstants.*;

public class RadioIndication extends IExtRadioIndication.Stub {

    RadioInteractorCore mRi;

    public RadioIndication(RadioInteractorCore ri) {
        mRi = ri;
    }

    public void rilConnected(int indicationType) {
        mRi.processIndication(indicationType);

        mRi.unsljLog(RIL_UNSOL_RIL_CONNECTED);

        mRi.notifyRegistrantsRilConnectionChanged(15);
    }

    public void videoPhoneCodecInd(int indicationType, ArrayList<Integer> data) {
        mRi.processIndication(indicationType);

        int response[] = RadioInteractorCore.arrayListToPrimitiveArray(data);
        if (RadioInteractorCore.DBG) {
            mRi.unsljLogRet(RI_UNSOL_VIDEOPHONE_CODEC, response);
        }

        if (mRi.mUnsolVPCodecRegistrants != null) {
            mRi.mUnsolVPCodecRegistrants.notifyRegistrants(new AsyncResult(null, response, null));
        }
    }

    public void videoPhoneDSCIInd(int indicationType, vendor.sprd.hardware.radio.V1_0.VideoPhoneDSCI data) {
        mRi.processIndication(indicationType);

        VideoPhoneDSCI dsci = new VideoPhoneDSCI();
        dsci.id = data.id;
        dsci.idr = data.idr;
        dsci.stat = data.stat;
        dsci.type = data.type;
        dsci.mpty = data.mpty;
        dsci.number = data.number;
        dsci.numType = data.numType;
        dsci.bsType = data.bsType;
        dsci.cause = data.cause;
        dsci.location = data.location;

        if (RadioInteractorCore.DBG) {
            mRi.unsljLogRet(RI_UNSOL_VIDEOPHONE_DSCI, dsci);
        }

        if (dsci.cause == 47 || dsci.cause == 57 || dsci.cause == 50 ||
                dsci.cause == 58 || dsci.cause == 69 || dsci.cause == 88) {
            if (mRi.mUnsolVPFallBackRegistrants != null) {
                if ((dsci.cause == 50 || dsci.cause == 57) && (dsci.location <= 2)) {
                    mRi.mUnsolVPFallBackRegistrants.notifyRegistrants(
                            new AsyncResult(null, new AsyncResult(dsci.idr,
                                    new AsyncResult(dsci.number, (dsci.location == 2 ?
                                            (dsci.cause + 200) : (dsci.cause + 100)), null), null), null));
                } else {
                    mRi.mUnsolVPFallBackRegistrants.notifyRegistrants(
                            new AsyncResult(null, new AsyncResult(dsci.idr,
                                    new AsyncResult(dsci.number, dsci.cause, null), null), null));
                }
            } else {
                if (mRi.mUnsolVPFailRegistrants != null) {
                    mRi.mUnsolVPFailRegistrants.notifyRegistrants(
                            new AsyncResult(null, new AsyncResult(dsci.idr,
                                    new AsyncResult(dsci.number, dsci.cause, null), null), null));
                }
            }
        }
    }

    public void videoPhoneStringInd(int indicationType, String data) {
        mRi.processIndication(indicationType);

        if (RadioInteractorCore.DBG) {
            mRi.unsljLogRet(RI_UNSOL_VIDEOPHONE_STRING, data);
        }

        if (mRi.mUnsolVPStrsRegistrants != null) {
            mRi.mUnsolVPStrsRegistrants.notifyRegistrants(new AsyncResult(null, data, null));
        }
    }

    public void videoPhoneRemoteMediaInd(int indicationType, ArrayList<Integer> data) {
        mRi.processIndication(indicationType);

        int response[] = RadioInteractorCore.arrayListToPrimitiveArray(data);
        if (RadioInteractorCore.DBG) {
            mRi.unsljLogRet(RI_UNSOL_VIDEOPHONE_REMOTE_MEDIA, response);
        }

        if (mRi.mUnsolVPRemoteMediaRegistrants != null) {
            mRi.mUnsolVPRemoteMediaRegistrants.notifyRegistrants(new AsyncResult(null, response, null));
        }
    }

    public void videoPhoneMMRingInd(int indicationType, int data) {
        mRi.processIndication(indicationType);

        if (RadioInteractorCore.DBG) {
            mRi.unsljLogRet(RI_UNSOL_VIDEOPHONE_MM_RING, data);
        }

        if (mRi.mUnsolVPMMRingRegistrants != null) {
            mRi.mUnsolVPMMRingRegistrants.notifyRegistrants(new AsyncResult(null, data, null));
        }
    }

    public void videoPhoneReleasingInd(int indicationType, String data) {
        mRi.processIndication(indicationType);
        if (RadioInteractorCore.DBG) {
            mRi.unsljLogRet(RI_UNSOL_VIDEOPHONE_RELEASING, data);
        }

        if (mRi.mUnsolVPFailRegistrants != null) {
            mRi.mUnsolVPFailRegistrants.notifyRegistrants(
                    new AsyncResult(null, new AsyncResult(null,
                            new AsyncResult(data, 1000, null), null), null));
        }
    }

    public void videoPhoneRecordVideoInd(int indicationType, int data) {
        mRi.processIndication(indicationType);

        if (RadioInteractorCore.DBG) {
            mRi.unsljLogRet(RI_UNSOL_VIDEOPHONE_RECORD_VIDEO, data);
        }

        if (mRi.mUnsolVPRecordVideoRegistrants != null) {
            mRi.mUnsolVPRecordVideoRegistrants.notifyRegistrants(new AsyncResult(null, data, null));
        }
    }

    public void videoPhoneMediaStartInd(int indicationType, int data) {
        mRi.processIndication(indicationType);

        if (RadioInteractorCore.DBG) {
            mRi.unsljLogRet(RI_UNSOL_VIDEOPHONE_MEDIA_START, data);
        }

        if (mRi.mUnsolVPMediaStartRegistrants != null) {
            mRi.mUnsolVPMediaStartRegistrants.notifyRegistrants(new AsyncResult(null, data, null));
        }
    }

    public void rauSuccessInd(int indicationType) {
        mRi.processIndication(indicationType);

        if (RadioInteractorCore.DBG) {
            mRi.unsljLog(RI_UNSOL_RAU_NOTIFY);
        }

        if (mRi.mUnsolRauSuccessRegistrants != null) {
            mRi.mUnsolRauSuccessRegistrants.notifyRegistrants();
        }
    }

    public void clearCodeFallbackInd(int indicationType) {
        mRi.processIndication(indicationType);

        if (RadioInteractorCore.DBG) {
            mRi.unsljLog(RI_UNSOL_CLEAR_CODE_FALLBACK);
        }

        if (mRi.mUnsolClearCodeFallbackRegistrants != null) {
            mRi.mUnsolClearCodeFallbackRegistrants.notifyRegistrants();
        }
    }

    public void rilConnectedInd(int indicationType) {
        mRi.processIndication(indicationType);

        if (RadioInteractorCore.DBG) {
            mRi.unsljLog(RI_UNSOL_RI_CONNECTED);
        }

        if (mRi.mUnsolRIConnectedRegistrants != null) {
            mRi.mUnsolRIConnectedRegistrants.notifyRegistrants();
        }
    }

    public void simlockSimExpiredInd(int indicationType, int data) {
        mRi.processIndication(indicationType);

        if (RadioInteractorCore.DBG) {
            mRi.unsljLogRet(RI_UNSOL_SIMLOCK_SIM_EXPIRED, data);
        }

        if (mRi.mUnsolExpireSimdRegistrants != null) {
            mRi.mUnsolExpireSimdRegistrants.notifyRegistrants(new AsyncResult(null, data, null));
        }
    }

    public void networkErrorCodeInd(int indicationType, ArrayList<Integer> data) {
        mRi.processIndication(indicationType);
        int response[] = RadioInteractorCore.arrayListToPrimitiveArray(data);
        if (RadioInteractorCore.DBG) {
            mRi.unsljLog(RI_UNSOL_NETWORK_ERROR_CODE);
        }

        if (mRi.mUnsolNetworkErrorCodeRegistrants != null) {
            mRi.mUnsolNetworkErrorCodeRegistrants.notifyRegistrants(new AsyncResult(null, response, null));
        }
    }

    public void simMgrSimStatusChangedInd(int indicationType) {
        mRi.processIndication(indicationType);

        if (RadioInteractorCore.DBG) {
            mRi.unsljLog(RI_UNSOL_SIMMGR_SIM_STATUS_CHANGED);
        }

        mRi.mHasRealSimStateChanged = true;

        if (mRi.mUnsolRealSimStateChangedRegistrants != null) {
            mRi.mUnsolRealSimStateChangedRegistrants.notifyRegistrants();
        }
    }

    public void earlyMediaInd(int indicationType, int data) {
        mRi.processIndication(indicationType);

        if (RadioInteractorCore.DBG) {
            mRi.unsljLogRet(RI_UNSOL_EARLY_MEDIA, data);
        }

        if (mRi.mUnsolEarlyMediaRegistrants != null) {
            mRi.mUnsolEarlyMediaRegistrants.notifyRegistrants(new AsyncResult(null, data, null));
        }
    }

    public void updateHdStateInd(int indicationType, int data) {
        mRi.processIndication(indicationType);

        if (RadioInteractorCore.DBG) {
            mRi.unsljLogRet(RI_UNSOL_UPDATE_HD_VOICE_STATE, data);
        }

        if (mRi.mUnsolHdStatusdRegistrants != null) {
            mRi.mUnsolHdStatusdRegistrants.notifyRegistrants(new AsyncResult(null, data, null));
        }
    }

    public String updatePlmn(String mccmnc, int lac) {
        try {
            IOperatorNameHandler operatorNameHandler = getOperatorNameHandler();
            if (operatorNameHandler != null) {
                return operatorNameHandler.getHighPriorityPlmn(mRi.getPhoneId(), mccmnc, lac);
            }
        } catch (RemoteException ex) {
            mRi.riljLoge("RemoteException updatePlmn", ex);
        } catch (NullPointerException ex) {
            mRi.riljLoge("NullPointerException updatePlmn", ex);
        }
        return mccmnc;
    }

    public String updateNetworkList(ArrayList<String> operatorInfo) {
        String[] strings = RadioInteractorCore.arrayListToString(operatorInfo);
        try {
            IOperatorNameHandler operatorNameHandler = getOperatorNameHandler();
            if (operatorNameHandler != null) {
                return operatorNameHandler.updateNetworkList(mRi.getPhoneId(), strings);
            }
        } catch (RemoteException ex) {
            mRi.riljLoge("RemoteException updateNetworkList", ex);
        } catch (NullPointerException ex) {
            mRi.riljLoge("NullPointerException updateNetworkList", ex);
        }
        return strings[0];
    }

    public void availableNetworksInd(int indicationType, ArrayList<String> operators) {
        mRi.processIndication(indicationType);
        String[] strings = RadioInteractorCore.arrayListToString(operators);
        if (RadioInteractorCore.DBG) {
            mRi.unsljLog(RI_UNSOL_AVAILABLE_NETWORKS);
        }

        if (mRi.mUnsolAvailableNetworksRegistrants != null) {
            mRi.mUnsolAvailableNetworksRegistrants.notifyRegistrants(new AsyncResult(null, strings, null));
        }
    }

    public void IMSCallStateChangedInd(int indicationType) {
        mRi.processIndication(indicationType);
        if (RadioInteractorCore.DBG) {
            mRi.unsljLog(RI_UNSOL_RESPONSE_IMS_CALL_STATE_CHANGED);
        }

        if (mRi.mImsCallStateRegistrants != null) {
            mRi.mImsCallStateRegistrants.notifyRegistrants();
        }
    }

    public void videoQualityInd(int indicationType, java.util.ArrayList<Integer> data) {
        mRi.processIndication(indicationType);
        if (RadioInteractorCore.DBG) {
            mRi.unsljLog(RI_UNSOL_RESPONSE_VIDEO_QUALITY);
        }

        if (mRi.mImsVideoQosRegistrant != null) {
            mRi.mImsVideoQosRegistrant.notifyRegistrants(new AsyncResult(null, arrayListToPrimitiveArray(data), null));
        }
    }

    public void IMSBearerEstablished(int indicationType, int status) {
        mRi.processIndication(indicationType);
        if (RadioInteractorCore.DBG) {
            mRi.unsljLogRet(RI_UNSOL_RESPONSE_IMS_BEARER_ESTABLISTED, status);
        }

        if (mRi.mImsBearerStateRegistrants != null) {
            mRi.mImsBearerStateRegistrants.notifyRegistrants(new AsyncResult(null, status, null));
        }
    }

    public void IMSHandoverRequestInd(int indicationType, int status) {
        mRi.processIndication(indicationType);
        if (RadioInteractorCore.DBG) {
            mRi.unsljLogRet(RI_UNSOL_IMS_HANDOVER_REQUEST, status);
        }

        if (mRi.mImsHandoverRequestRegistrant != null) {
            mRi.mImsHandoverRequestRegistrant.notifyRegistrants(new AsyncResult(null, status, null));
        }
    }

    public void IMSHandoverStatusChangedInd(int indicationType, int status) {
        mRi.processIndication(indicationType);
        if (RadioInteractorCore.DBG) {
            mRi.unsljLogRet(RI_UNSOL_IMS_HANDOVER_STATUS_CHANGE, status);
        }

        if (mRi.mImsHandoverStatusRegistrant != null) {
            mRi.mImsHandoverStatusRegistrant.notifyRegistrants(new AsyncResult(null, status, null));
        }
    }

    public void IMSNetworkInfoChangedInd(int indicationType, ImsNetworkInfo nwInfo) {
        mRi.processIndication(indicationType);
        if (RadioInteractorCore.DBG) {
            mRi.unsljLog(RI_UNSOL_IMS_NETWORK_INFO_CHANGE);
        }

        if (mRi.mImsNetworkInfoRegistrant != null) {
            mRi.mImsNetworkInfoRegistrant.notifyRegistrants(new AsyncResult(null, nwInfo, null));
        }
    }

    public void IMSRegisterAddressChangedInd(int indicationType, ArrayList<String> addr) {
        mRi.processIndication(indicationType);
        if (RadioInteractorCore.DBG) {
            mRi.unsljLog(RI_UNSOL_IMS_REGISTER_ADDRESS_CHANGE);
        }

        if (mRi.mImsRegAddressRegistrant != null) {
            mRi.mImsRegAddressRegistrant.notifyRegistrants(new AsyncResult(null, arrayListToStringArray(addr), null));
        }
    }

    public void IMSWifiParamInd(int indicationType, java.util.ArrayList<Integer> data) {
        mRi.processIndication(indicationType);
        if (RadioInteractorCore.DBG) {
            mRi.unsljLog(RI_UNSOL_IMS_WIFI_PARAM);
        }

        if (mRi.mImsWiFiParamRegistrant != null) {
            mRi.mImsWiFiParamRegistrant.notifyRegistrants(new AsyncResult(null, arrayListToPrimitiveArray(data), null));
        }
    }

    public void IMSNetworkStateChangedInd(int indicationType, int status) {
        mRi.processIndication(indicationType);
        if (RadioInteractorCore.DBG) {
            mRi.unsljLogRet(RI_UNSOL_IMS_NETWORK_STATE_CHANGED, status);
        }

        if (mRi.mImsNetworkStateChangedRegistrants != null) {
            mRi.mImsNetworkStateChangedRegistrants.notifyRegistrants(new AsyncResult(null, status, null));
        }
    }

    public void IMSCsfbVendorCauseInd(int indicationType, String causeCode) {
        mRi.processIndication(indicationType);
        if (RadioInteractorCore.DBG) {
            mRi.unsljLogRet(RI_UNSOL_IMS_CSFB_VENDOR_CAUSE, causeCode);
        }

        if (mRi.mUnsolImsCsfbVendorCauseRegistrant != null) {
            mRi.mUnsolImsCsfbVendorCauseRegistrant.notifyRegistrants(new AsyncResult(null, causeCode, null));
        }
    }

    public void IMSErrorCauseInd(int indicationType, ImsErrorCauseInfo errCauseInfo) {
        mRi.processIndication(indicationType);
        if (RadioInteractorCore.DBG) {
            mRi.unsljLog(RI_UNSOL_IMS_ERROR_CAUSE);
        }

        if (mRi.mUnsolImsErrorCauseRegistrant != null) {
            mRi.mUnsolImsErrorCauseRegistrant.notifyRegistrants(new AsyncResult(null, errCauseInfo, null));
        }
    }

    public void subsidyLockStatusChangedInd(int indicationType, int data) {
        mRi.processIndication(indicationType);

        if (RadioInteractorCore.DBG) {
            mRi.unsljLogRet(RI_UNSOL_SUBSIDYLOCK_STATE, data);
        }

        if (mRi.mUnsolSubsidyLockStateRegistrants != null) {
            mRi.mUnsolSubsidyLockStateRegistrants.notifyRegistrants(new AsyncResult(null, data, null));
        }
    }

    public void cnapInd(int indicationType, String name) {
        mRi.processIndication(indicationType);

        if (RadioInteractorCore.DBG) {
            mRi.unsljLog(RI_UNSOL_CNAP);
        }

        if(mRi.mUnsolCnapRegistrant != null) {
            mRi.mUnsolCnapRegistrant.notifyRegistrants(new AsyncResult(null, name, null));
        }
    }

    public void signalConnStatusInd(int indicationType, SignalConnStatus sigConnStatus) {
        mRi.processIndication(indicationType);
        SignalConnectedStatus connectedStatus = new SignalConnectedStatus(sigConnStatus.mode,
                sigConnStatus.state, sigConnStatus.access, sigConnStatus.coreNetwork);

        if (RadioInteractorCore.DBG) {
            mRi.unsljLogRet(RI_UNSOL_SIGNAL_CONNECTION_STATUS, connectedStatus);
        }

        if(mRi.mUnsolSignalConnectionStatusRegistrant != null) {
            mRi.mUnsolSignalConnectionStatusRegistrant.notifyRegistrants(new AsyncResult(null, connectedStatus, null));
        }
    }

    public void smartNrChangedInd(int indicationType) {
        mRi.processIndication(indicationType);

        if (RadioInteractorCore.DBG) {
            mRi.unsljLog(RI_UNSOL_SMART_NR_CHANNGED);
        }

        if (mRi.mUnsolSmartNrChangedRegistrants != null) {
            mRi.mUnsolSmartNrChangedRegistrants.notifyRegistrants();
        }
    }

    public void nrCfgInfoInd(int indicationType, ArrayList<Integer> data) {
        mRi.processIndication(indicationType);
        int response[] = RadioInteractorCore.arrayListToPrimitiveArray(data);

        if (RadioInteractorCore.DBG) {
            mRi.unsljLogRet(RI_UNSOL_NR_CFG_INFO, data);
        }

        if (mRi.mUnsolNrCfgInfoRegistrants != null) {
            mRi.mUnsolNrCfgInfoRegistrants.notifyRegistrants(new AsyncResult(null, response, null));
        }
    }

    public void modemStateChangedInd(int indicationType, ModemStatusInfo statusInfo) {
        mRi.processIndication(indicationType);

        ModemStatusIndication modemStatusIndication = new ModemStatusIndication(statusInfo.status, statusInfo.assertInfo);

        if (RadioInteractorCore.DBG) {
            mRi.unsljLogRet(RI_UNSOL_MODEM_STATE_CHANGED, statusInfo.status);
        }

        mRi.mUnsolModemStateChangedRegistrants.notifyRegistrants(new AsyncResult(null, modemStatusIndication, null));
    }

    public static int[] arrayListToPrimitiveArray(ArrayList<Integer> ints) {
        int[] ret = new int[ints.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = ints.get(i);
        }
        return ret;
    }

    public static String[] arrayListToStringArray(ArrayList<String> data) {
        String[] ret = new String[data.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = data.get(i);
        }
        return ret;
    }

    private IOperatorNameHandler getOperatorNameHandler() {
        return IOperatorNameHandler.Stub.asInterface(ServiceManager.getService("ions_ex"));
    }

}
