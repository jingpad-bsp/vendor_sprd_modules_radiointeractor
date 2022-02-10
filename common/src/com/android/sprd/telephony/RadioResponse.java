package com.android.sprd.telephony;

import android.hardware.radio.V1_0.RadioError;
import android.os.AsyncResult;
import android.os.Message;

import com.android.sprd.telephony.linkturbo.LteSpeedAndSignalStrengthInfo;
import com.android.sprd.telephony.uicc.IccCardApplicationStatusEx;
import com.android.sprd.telephony.uicc.IccCardStatusEx;
import com.android.sprd.telephony.uicc.IccIoResult;

import java.util.ArrayList;

import vendor.sprd.hardware.radio.V1_0.CallForwardInfoUri;
import vendor.sprd.hardware.radio.V1_0.CallVoLTE;
import vendor.sprd.hardware.radio.V1_0.ExtAppStatus;
import vendor.sprd.hardware.radio.V1_0.ExtCardStatus;
import vendor.sprd.hardware.radio.V1_0.ExtRadioError;
import vendor.sprd.hardware.radio.V1_0.ExtRadioResponseInfo;
import vendor.sprd.hardware.radio.V1_0.IExtRadioResponse;
import vendor.sprd.hardware.radio.V1_0.ImsNetworkInfo;
import vendor.sprd.hardware.radio.V1_0.LteSpeedAndSignalStrength;

public class RadioResponse extends IExtRadioResponse.Stub {

    RadioInteractorCore mRi;

    public RadioResponse(RadioInteractorCore ri) {
        mRi = ri;
    }

    static void sendMessageResponse(Message msg, Object ret) {
        if (msg != null) {
            AsyncResult.forMessage(msg, ret, null);
            msg.sendToTarget();
        }
    }

    public void videoPhoneDialResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void videoPhoneCodecResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void videoPhoneFallbackResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void videoPhoneStringResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void videoPhoneLocalMediaResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void videoPhoneControlIFrameResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void setTrafficClassResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void enableLTEResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void attachDataResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void forceDeatchResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void getHDVoiceStateResponse(ExtRadioResponseInfo responseInfo, int state) {
        responseInts(responseInfo, state);
    }

    public void simmgrSimPowerResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void enableRauNotifyResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void simGetAtrResponse(ExtRadioResponseInfo responseInfo, String atr) {
        responseString(responseInfo, atr);
    }

    public void getSimCapacityResponse(ExtRadioResponseInfo responseInfo, ArrayList<String> data) {
        responseStringArrayList(responseInfo, data);
    }

    public void storeSmsToSimResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void querySmsStorageModeResponse(ExtRadioResponseInfo responseInfo, String atr) {
        responseString(responseInfo, atr);
    }

    public void getSimlockRemaintimesResponse(ExtRadioResponseInfo responseInfo, int remainingRetries) {
        responseInts(responseInfo, remainingRetries);
    }

    public void setFacilityLockForUserResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void getSimlockStatusResponse(ExtRadioResponseInfo responseInfo, int status) {
        responseInts(responseInfo, status);
    }

    public void getSimlockDummysResponse(ExtRadioResponseInfo responseInfo, ArrayList<Integer> selectResponse) {
        responseIntArrayList(responseInfo, selectResponse);
    }

    public void getSimlockWhitelistResponse(ExtRadioResponseInfo responseInfo, String whitelist) {
        responseString(responseInfo, whitelist);
    }

    public void updateEcclistResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void setSinglePDNResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void queryColpResponse(ExtRadioResponseInfo responseInfo, int result) {
        responseInts(responseInfo, result);
    }

    public void queryColrResponse(ExtRadioResponseInfo responseInfo, int result) {
        responseInts(responseInfo, result);
    }

    public void updateOperatorNameResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void simmgrGetSimStatusResponse(ExtRadioResponseInfo responseInfo, ExtCardStatus cardStatus) {
        responseSimStatus(responseInfo, cardStatus);
    }

    public void setXcapIPAddressResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void sendCmdAsyncResponse(ExtRadioResponseInfo responseInfo, String response) {
        responseString(responseInfo, response);
    }

    public void reAttachResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void setPreferredNetworkTypeExtResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void requestShutdownExtResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void explicitCallTransferExtResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void updateCLIPResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void setTPMRStateResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void getTPMRStateResponse(ExtRadioResponseInfo responseInfo, int result) {
        responseInts(responseInfo, result);
    }

    public void setVideoResolutionResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void enableLocalHoldResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void enableWiFiParamReportResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void callMediaChangeRequestTimeOutResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void setLocalToneResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    // White list refactor: get default video resolution
    public void getSpecialRatcapResponse(ExtRadioResponseInfo responseInfo, int result) {
        responseInts(responseInfo, result);
    }

    public void getVideoResolutionResponse(ExtRadioResponseInfo responseInfo, int result) {
        responseInts(responseInfo, result);
    }
    public void updatePlmnPriorityResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void queryPlmnResponse(ExtRadioResponseInfo responseInfo, String response) {
        responseString(responseInfo, response);
    }

    public void setSimPowerRealResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void resetModemResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void getRadioPreferenceResponse(ExtRadioResponseInfo responseInfo, String response) {
        responseString(responseInfo, response);
    }

    public void setRadioPreferenceResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void queryRootNodeResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void getIMSCurrentCallsResponse(ExtRadioResponseInfo responseInfo, java.util.ArrayList<CallVoLTE> calls){
        RIRequest rr = mRi.processResponse(responseInfo);

        if (rr != null) {
            if (responseInfo.error == ExtRadioError.NONE) {
                sendMessageResponse(rr.mResult, calls);
            }
            mRi.processResponseDone(rr, responseInfo, calls);
        }
    }

    public void setIMSVoiceCallAvailabilityResponse(ExtRadioResponseInfo responseInfo){
        responseVoid(responseInfo);
    }

    public void getIMSVoiceCallAvailabilityResponse(ExtRadioResponseInfo responseInfo, int state){
        responseInts(responseInfo, state);
    }

    public void initISIMResponse(ExtRadioResponseInfo responseInfo, int response){
        responseInts(responseInfo, response);
    }

    public void requestVolteCallMediaChangeResponse(ExtRadioResponseInfo responseInfo){
        responseVoid(responseInfo);
    }

    public void responseVolteCallMediaChangeResponse(ExtRadioResponseInfo responseInfo){
        responseVoid(responseInfo);
    }

    public void setIMSSmscAddressResponse(ExtRadioResponseInfo responseInfo){
        responseVoid(responseInfo);
    }

    public void volteCallFallBackToVoiceResponse(ExtRadioResponseInfo responseInfo){
        responseVoid(responseInfo);
    }

    public void queryCallForwardStatusResponse(ExtRadioResponseInfo responseInfo, ArrayList<CallForwardInfoUri> callForwardInfos){
        RIRequest rr = mRi.processResponse(responseInfo);

        if (rr != null) {
            if (responseInfo.error == ExtRadioError.NONE) {
                sendMessageResponse(rr.mResult, callForwardInfos);
            }
            mRi.processResponseDone(rr, responseInfo, callForwardInfos);
        }
    }

    public void getFacilityLockForAppExtResponse(ExtRadioResponseInfo responseInfo, int status, int serviceClass) {
        responseInts(responseInfo, status, serviceClass);
    }

    public void setCallForwardUriResponse(ExtRadioResponseInfo responseInfo){
        responseVoid(responseInfo);
    }

    public void IMSInitialGroupCallResponse(ExtRadioResponseInfo responseInfo){
        responseVoid(responseInfo);
    }

    public void IMSAddGroupCallResponse(ExtRadioResponseInfo responseInfo){
        responseVoid(responseInfo);
    }

    public void enableIMSResponse(ExtRadioResponseInfo responseInfo){
        responseVoid(responseInfo);
    }


    public void getIMSBearerStateResponse(ExtRadioResponseInfo responseInfo, int state) {
        responseInts(responseInfo, state);
    }

    public void setExtInitialAttachApnResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void IMSHandoverResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void notifyIMSHandoverStatusUpdateResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void notifyIMSNetworkInfoChangedResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void notifyIMSCallEndResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void notifyVoWifiEnableResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void notifyVoWifiCallStateChangedResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void notifyDataRouterUpdateResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void IMSHoldSingleCallResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void IMSMuteSingleCallResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void IMSSilenceSingleCallResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void IMSEnableLocalConferenceResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void notifyHandoverCallInfoResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void getSrvccCapbilityResponse(ExtRadioResponseInfo responseInfo, int response) {
        responseInts(responseInfo, response);
    }

    public void getIMSPcscfAddressResponse(ExtRadioResponseInfo responseInfo, String addr) {
        responseString(responseInfo, addr);
    }

    public void setIMSPcscfAddressResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void getImsRegAddressResponse(ExtRadioResponseInfo responseInfo, ArrayList<String> addr) {
        responseStringArrayList(responseInfo, addr);
    }

    public void getImsPaniInfoResponse(ExtRadioResponseInfo responseInfo, ImsNetworkInfo networkInfo) {
        RIRequest rr = mRi.processResponse(responseInfo);

        if (rr != null) {
            if (responseInfo.error == ExtRadioError.NONE) {
                sendMessageResponse(rr.mResult, networkInfo);
            }
            mRi.processResponseDone(rr, responseInfo, networkInfo);
        }
    }

    public void getPreferredNetworkTypeExtResponse(ExtRadioResponseInfo responseInfo, int nwType) {
        responseInts(responseInfo, nwType);
    }

    public void setRadioPowerFallbackResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void getCnapResponse(ExtRadioResponseInfo responseInfo, int active, int state) {
        responseInts(responseInfo, active, state);
    }

    public void setLocationInfoResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void setEmergencyOnlyResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void getSubsidyLockdyStatusResponse(ExtRadioResponseInfo responseInfo, int result) {
        responseInts(responseInfo, result);
    }

    public void setImsUserAgentResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void getVoLTEAllowedPLMNResponse(ExtRadioResponseInfo responseInfo, int delta) {
        responseInts(responseInfo, delta);
    }

    public void setSmsBearerResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void getSmsBearerResponse(ExtRadioResponseInfo responseInfo, int type) {
        responseInts(responseInfo, type);
    }

    public void setPsDataOffResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void requestLteSpeedAndSignalStrengthResponse(ExtRadioResponseInfo responseInfo,
                                                         LteSpeedAndSignalStrength lteSpeedAndSignalStrength) {
        responseLteSpeedAndSignalStrength(responseInfo, lteSpeedAndSignalStrength);
    }

    private void responseLteSpeedAndSignalStrength(ExtRadioResponseInfo responseInfo,
                                                   LteSpeedAndSignalStrength lteSpeedAndSignalStrength) {
        RIRequest rr = mRi.processResponse(responseInfo);
        if (rr != null) {
            LteSpeedAndSignalStrengthInfo lteSpeedAndSignalStrengthInfo = new LteSpeedAndSignalStrengthInfo();
            lteSpeedAndSignalStrengthInfo.setTxSpeed(lteSpeedAndSignalStrength.txSpeed);
            lteSpeedAndSignalStrengthInfo.setRxSpeed(lteSpeedAndSignalStrength.rxSpeed);
            lteSpeedAndSignalStrengthInfo.setSnr(lteSpeedAndSignalStrength.snr);
            lteSpeedAndSignalStrengthInfo.setRsrp(lteSpeedAndSignalStrength.rsrp);

            UtilLog.logd(UtilLog.TAG, "responseLteSpeedAndSignalStrength: from HIDL: " + lteSpeedAndSignalStrengthInfo);
            if (responseInfo.error == RadioError.NONE) {
                sendMessageResponse(rr.mResult, lteSpeedAndSignalStrengthInfo);
            }
            mRi.processResponseDone(rr, responseInfo, lteSpeedAndSignalStrengthInfo);
        }
    }

    public void enableNrSwitchResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void setUsbShareStateSwitchResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void setStandAloneResponse(ExtRadioResponseInfo responseInfo) {
        responseVoid(responseInfo);
    }

    public void getStandAloneResponse(ExtRadioResponseInfo responseInfo, int result) {
        responseInts(responseInfo, result);
    }

    private void responseSimStatus(ExtRadioResponseInfo responseInfo, ExtCardStatus cardStatus) {
        RIRequest rr = mRi.processResponse(responseInfo);

        if (rr != null) {
            IccCardStatusEx iccCardStatus = new IccCardStatusEx();
            iccCardStatus.setCardState(cardStatus.cardState);
            iccCardStatus.setUniversalPinState(cardStatus.universalPinState);
            iccCardStatus.mGsmUmtsSubscriptionAppIndex = cardStatus.gsmUmtsSubscriptionAppIndex;
            iccCardStatus.mCdmaSubscriptionAppIndex = cardStatus.cdmaSubscriptionAppIndex;
            iccCardStatus.mImsSubscriptionAppIndex = cardStatus.imsSubscriptionAppIndex;
            int numApplications = cardStatus.applications.size();

            // limit to maximum allowed applications
            if (numApplications > IccCardStatusEx.CARD_MAX_APPS) {
                numApplications = IccCardStatusEx.CARD_MAX_APPS;
            }
            iccCardStatus.mApplications = new IccCardApplicationStatusEx[numApplications];
            for (int i = 0; i < numApplications; i++) {
                ExtAppStatus rilAppStatus = cardStatus.applications.get(i);
                IccCardApplicationStatusEx appStatus = new IccCardApplicationStatusEx();
                appStatus.app_type       = appStatus.AppTypeFromRILInt(rilAppStatus.appType);
                appStatus.app_state      = appStatus.AppStateFromRILInt(rilAppStatus.appState);
                appStatus.perso_substate = appStatus.PersoSubstateFromRILInt(
                        rilAppStatus.persoSubstate);
                appStatus.aid            = rilAppStatus.aidPtr;
                appStatus.app_label      = rilAppStatus.appLabelPtr;
                appStatus.pin1_replaced  = rilAppStatus.pin1Replaced;
                appStatus.pin1           = appStatus.PinStateFromRILInt(rilAppStatus.pin1);
                appStatus.pin2           = appStatus.PinStateFromRILInt(rilAppStatus.pin2);
                iccCardStatus.mApplications[i] = appStatus;
             }
             UtilLog.logd(UtilLog.TAG, "responseSimStatus: from HIDL: " + iccCardStatus);
             if (responseInfo.error == ExtRadioError.NONE) {
                 sendMessageResponse(rr.mResult, iccCardStatus);
             }
             mRi.processResponseDone(rr, responseInfo, iccCardStatus);
        }
    }

    private void responseIccIo(ExtRadioResponseInfo responseInfo,
            android.hardware.radio.V1_0.IccIoResult result) {
        RIRequest rr = mRi.processResponse(responseInfo);

        if (rr != null) {
            IccIoResult ret = null;
            if (responseInfo.error == ExtRadioError.NONE) {
                ret = new IccIoResult(result.sw1, result.sw2, result.simResponse);
                sendMessageResponse(rr.mResult, ret);
            }
            mRi.processResponseDone(rr, responseInfo, ret);
        }
    }

    private void responseVoid(ExtRadioResponseInfo responseInfo) {
        RIRequest rr = mRi.processResponse(responseInfo);

        if (rr != null) {
            Object ret = null;
            if (responseInfo.error == ExtRadioError.NONE) {
                sendMessageResponse(rr.mResult, ret);
            }
            mRi.processResponseDone(rr, responseInfo, ret);
        }
    }

    private void responseInts(ExtRadioResponseInfo responseInfo, int ...var) {
        final ArrayList<Integer> ints = new ArrayList<>();
        for (int i = 0; i < var.length; i++) {
            ints.add(var[i]);
        }
        responseIntArrayList(responseInfo, ints);
    }

    private void responseIntArrayList(ExtRadioResponseInfo responseInfo, ArrayList<Integer> var) {
        RIRequest rr = mRi.processResponse(responseInfo);

        if (rr != null) {
            Object ret = null;
            if (responseInfo.error == ExtRadioError.NONE) {
                int[] response = new int[var.size()];
                for (int i = 0; i < var.size(); i++) {
                    response[i] = var.get(i);
                }
                ret = response;
                sendMessageResponse(rr.mResult, ret);
            }
            mRi.processResponseDone(rr, responseInfo, ret);
        }
    }

    private void responseString(ExtRadioResponseInfo responseInfo, String str) {
        RIRequest rr = mRi.processResponse(responseInfo);

        if (rr != null) {
            if (responseInfo.error == ExtRadioError.NONE) {
                sendMessageResponse(rr.mResult, str);
            }
            mRi.processResponseDone(rr, responseInfo, str);
        }
    }

    private void responseStrings(ExtRadioResponseInfo responseInfo, String ...str) {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < str.length; i++) {
            strings.add(str[i]);
        }
        responseStringArrayList(responseInfo, strings);
    }

    private void responseStringArrayList(ExtRadioResponseInfo responseInfo, ArrayList<String> strings) {
        RIRequest rr = mRi.processResponse(responseInfo);

        if (rr != null) {
            String[] ret = null;
            if (responseInfo.error == ExtRadioError.NONE) {
                ret = new String[strings.size()];
                for (int i = 0; i < strings.size(); i++) {
                    ret[i] = strings.get(i);
                }
                sendMessageResponse(rr.mResult, ret);
            }
            mRi.processResponseDone(rr, responseInfo, ret);
        }
    }
}
