
package com.android.sprd.telephony;

import com.android.sprd.telephony.RadioInteractorCore.SuppService;
import com.android.sprd.telephony.aidl.IRadioInteractorCallback;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.AsyncResult;

public class RadioInteractorCallbackListener {

    /**
     * Stop listening for updates.
     * <p>
     * The RadioInteractorCallbackListener is unregistered for any update.
     */
    public static final int LISTEN_NONE = 0;

    /**
     * Listen for radiointeractor event.
     *
     * @see #onRadiointeractorEvent
     */
    public static final int LISTEN_RADIOINTERACTOR_EVENT = 0x00000001;

    /**
     * Listen for ri connect event.
     *
     * @see #onRiConnectedEvent
     */
    public static final int LISTEN_RI_CONNECTED_EVENT = 0x00000002;

    /**
     * Listen for radiointeractor eMBMS event.
     *
     * @see #onRadiointeractorEmbmsEvent
     */
    public static final int LISTEN_RADIOINTERACTOR_EMBMS_EVENT = 0x00000004;

    /**
     * Listen for video phone codec event.
     *
     * @see #onVideoPhoneCodecEvent
     */
    public static final int LISTEN_VIDEOPHONE_CODEC_EVENT = 0x00000008;

    /**
     * Listen for video phone DSCI event.
     *
     * @see #onVideoPhoneDsciEvent
     */
    public static final int LISTEN_VIDEOPHONE_DSCI_EVENT = 0x00000010;

    /**
     * Listen for video phone string event.
     *
     * @see #onVideoPhoneStringEvent
     */
    public static final int LISTEN_VIDEOPHONE_STRING_EVENT = 0x00000020;

    /**
     * Listen for video phone remote media event.
     *
     * @see #onVideoPhoneRemoteMediaEvent
     */
    public static final int LISTEN_VIDEOPHONE_REMOTE_MEDIA_EVENT = 0x00000040;

    /**
     * Listen for video phone mm ring event.
     *
     * @see #onVideoPhoneMMRingEvent
     */
    public static final int LISTEN_VIDEOPHONE_MM_RING_EVENT = 0x00000080;

    /**
     * Listen for video phone releasing event.
     *
     * @see #onVideoPhoneReleasingEvent
     */
    public static final int LISTEN_VIDEOPHONE_RELEASING_EVENT = 0x00000100;

    /**
     * Listen for video phone record video event.
     *
     * @see #onVideoPhoneRecordVideoEvent
     */
    public static final int LISTEN_VIDEOPHONE_RECORD_VIDEO_EVENT = 0x00000200;

    /**
     * Listen for video phone media start event.
     *
     * @see #onVideoPhoneMediaStartEvent
     */
    public static final int LISTEN_VIDEOPHONE_MEDIA_START_EVENT = 0x00000400;

    /**
     * Listen for supp service failed event.
     *
     * @see #onSuppServiceFailedEvent
     */
    public static final int LISTEN_SUPP_SERVICE_FAILED_EVENT = 0x00000800;

    /**
     * Listen for rau success event.
     *
     * @see #onRauSuccessEvent
     */
    public static final int LISTEN_RAU_SUCCESS_EVENT = 0x00001000;

    /**
     * Listen for clear code fallback event.
     *
     * @see #onClearCodeFallbackEvent
     */
    public static final int LISTEN_CLEAR_CODE_FALLBACK_EVENT = 0x00002000;

    /**
     * Listen for simmer sim status event.
     *
     * @see #onRealSimStateChangedEvent
     */
    public static final int LISTEN_SIMMGR_SIM_STATUS_CHANGED_EVENT = 0x00004000;

    /**
     * Listen for expire sim event.
     *
     * @see #onExpireSimEvent
     */
    public static final int LISTEN_EXPIRE_SIM_EVENT = 0x00008000;

    /**
     * Listen for early media event.
     *
     * @see #onEarlyMediaEvent
     */
    public static final int LISTEN_EARLY_MEDIA_EVENT = 0x00010000;

    /**
     * Listen for network error code event.
     *
     * @see #onNetowrkErrorCodeChangedEvent
     */
    public static final int LISTEN_NETWORK_ERROR_CODE_EVENT = 0x00020000;

    /**
     * Listen for avaliable network event.
     *
     * @see #onAvailableNetworksEvent
     */
    public static final int LISTEN_AVAILAVLE_NETWORKS_EVENT = 0x00040000;

    /**
     * Listen for HD status changed event.
     *
     * @see #onHdStatusChangedEvent
     */
    public static final int LISTEN_HD_STATUS_CHANGED_EVENT = 0x00080000;

    /**
     * Listen for subsidylock event.
     *
     * @see #onSubsidyLockEvent
     */
    public static final int LISTEN_SUBSIDYLOCK_EVENT = 0x00100000;

    /**
     * Listen for ims csfb vendor cause event.
     *
     * @see #onImsCsfbVendorCauseEvent
     */
    public static final int LISTEN_IMS_CSFB_VENDOR_CAUSE_EVENT = 0x00200000;

    /**
     * Listen for cnap event.
     *
     * @see #onCnapEvent
     */
    public static final int LISTEN_CNAP_EVENT = 0x00400000;
    public static final int LISTEN_SIGNAL_CONNECTION_STATUS_EVENT = 0x00800000;
    public static final int LISTEN_SMART_NR_CHANGED_EVENT = 0x01000000;
    public static final int LISTEN_NR_CFG_INFO_EVENT = 0x02000000;


    /**
     * Listen for modem state event.
     *
     * @see #onModemStateChangedEvent
     */
    public static final int LISTEN_MODEM_STATE_CHANGED_EVENT = 0x04000000;

    final Handler mHandler;
    int mSlotId = -1;

    public RadioInteractorCallbackListener() {
        this(0, Looper.myLooper());
    }

    public RadioInteractorCallbackListener(int slotId) {
        this(slotId, Looper.myLooper());
    }

    public RadioInteractorCallbackListener(int slotId, Looper looper) {
        mSlotId = slotId;
        mHandler = new Handler(looper) {
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case LISTEN_RADIOINTERACTOR_EVENT:
                        RadioInteractorCallbackListener.this.onRadiointeractorEvent();
                        break;

                    case LISTEN_RI_CONNECTED_EVENT:
                        RadioInteractorCallbackListener.this.onRiConnectedEvent();
                        break;

                    case LISTEN_RADIOINTERACTOR_EMBMS_EVENT:
                        RadioInteractorCallbackListener.this.onRadiointeractorEmbmsEvent();
                        break;

                    case LISTEN_VIDEOPHONE_CODEC_EVENT:
                        RadioInteractorCallbackListener.this.onVideoPhoneCodecEvent(msg.obj);
                        break;

                    case LISTEN_VIDEOPHONE_DSCI_EVENT:
                        RadioInteractorCallbackListener.this.onVideoPhoneDsciEvent(msg.obj);
                        break;

                    case LISTEN_VIDEOPHONE_STRING_EVENT:
                        RadioInteractorCallbackListener.this.onVideoPhoneStringEvent(msg.obj);
                        break;

                    case LISTEN_VIDEOPHONE_REMOTE_MEDIA_EVENT:
                        RadioInteractorCallbackListener.this.onVideoPhoneRemoteMediaEvent(msg.obj);
                        break;

                    case LISTEN_VIDEOPHONE_MM_RING_EVENT:
                        RadioInteractorCallbackListener.this.onVideoPhoneMMRingEvent(msg.obj);
                        break;

                    case LISTEN_VIDEOPHONE_RELEASING_EVENT:
                        RadioInteractorCallbackListener.this.onVideoPhoneReleasingEvent(msg.obj);
                        break;

                    case LISTEN_VIDEOPHONE_RECORD_VIDEO_EVENT:
                        RadioInteractorCallbackListener.this.onVideoPhoneRecordVideoEvent(msg.obj);
                        break;

                    case LISTEN_VIDEOPHONE_MEDIA_START_EVENT:
                        RadioInteractorCallbackListener.this.onVideoPhoneMediaStartEvent(msg.obj);
                        break;

                    case LISTEN_SUPP_SERVICE_FAILED_EVENT:
                        RadioInteractorCallbackListener.this.onSuppServiceFailedEvent(msg.arg1);
                        break;

                    case LISTEN_RAU_SUCCESS_EVENT:
                        RadioInteractorCallbackListener.this.onRauSuccessEvent();
                        break;

                    case LISTEN_CLEAR_CODE_FALLBACK_EVENT:
                        RadioInteractorCallbackListener.this.onClearCodeFallbackEvent();
                        break;

                    case LISTEN_SIMMGR_SIM_STATUS_CHANGED_EVENT:
                        RadioInteractorCallbackListener.this.onRealSimStateChangedEvent();
                        break;

                    case LISTEN_EXPIRE_SIM_EVENT:
                        RadioInteractorCallbackListener.this.onExpireSimEvent(msg.obj);
                        break;

                    case LISTEN_EARLY_MEDIA_EVENT:
                        RadioInteractorCallbackListener.this.onEarlyMediaEvent(msg.obj);
                        break;

                    case LISTEN_NETWORK_ERROR_CODE_EVENT:
                        RadioInteractorCallbackListener.this.onNetowrkErrorCodeChangedEvent(msg.obj);
                        break;

                    case LISTEN_AVAILAVLE_NETWORKS_EVENT:
                        RadioInteractorCallbackListener.this.onAvailableNetworksEvent(msg.obj);
                        break;

                    case LISTEN_HD_STATUS_CHANGED_EVENT:
                        RadioInteractorCallbackListener.this.onHdStatusChangedEvent(msg.obj);
                        break;

                    case LISTEN_SUBSIDYLOCK_EVENT:
                        RadioInteractorCallbackListener.this.onSubsidyLockEvent(msg.obj);
                        break;

                    case LISTEN_IMS_CSFB_VENDOR_CAUSE_EVENT:
                        RadioInteractorCallbackListener.this.onImsCsfbVendorCauseEvent(msg.obj);
                        break;
                    case LISTEN_CNAP_EVENT:
                        RadioInteractorCallbackListener.this.onCnapEvent(msg.obj);
                        break;
                    case LISTEN_SIGNAL_CONNECTION_STATUS_EVENT:
                        RadioInteractorCallbackListener.this.onSignalConnectionStatusEvent(msg.obj);
                        break;
                    case LISTEN_SMART_NR_CHANGED_EVENT:
                        RadioInteractorCallbackListener.this.onSmartNrChangedEvent();
                        break;
                    case LISTEN_NR_CFG_INFO_EVENT:
                        RadioInteractorCallbackListener.this.onNrCfgInfoEvent(msg.obj);
                        break;
                    case LISTEN_MODEM_STATE_CHANGED_EVENT:
                        AsyncResult ar = (AsyncResult) msg.obj;
                        ModemStatusIndication modemStatusIndication = (ModemStatusIndication) ar.result;;
                        RadioInteractorCallbackListener.this.onModemStateChangedEvent(modemStatusIndication.getStatus(),
                                modemStatusIndication.getAssertInfo());
                        break;
                }
            };
        };
    }

    IRadioInteractorCallback mCallback = new IRadioInteractorCallback.Stub() {

        public void onRadiointeractorEvent() {
            Message.obtain(mHandler, LISTEN_RADIOINTERACTOR_EVENT, 0, 0, null).sendToTarget();
        }

        public void onRadiointeractorEmbmsEvent() {
            Message.obtain(mHandler, LISTEN_RADIOINTERACTOR_EMBMS_EVENT, 0, 0, null).sendToTarget();
        }

        public void onSuppServiceFailedEvent(int service) {
            Message.obtain(mHandler, LISTEN_SUPP_SERVICE_FAILED_EVENT, service, 0, null).sendToTarget();
        }

        public void onRealSimStateChangedEvent() {
            Message.obtain(mHandler, LISTEN_SIMMGR_SIM_STATUS_CHANGED_EVENT, 0, 0, null).sendToTarget();
        }

        public void onExpireSimEvent(int info) {
            Message.obtain(mHandler, LISTEN_EXPIRE_SIM_EVENT, 0, 0, info).sendToTarget();
        }

        public void onEarlyMediaEvent(int earlyMedia) {
            AsyncResult ret = new AsyncResult (null, earlyMedia, null);
            Message.obtain(mHandler, LISTEN_EARLY_MEDIA_EVENT, 0, 0, ret).sendToTarget();
        }

        public void onHdStatusChangedEvent(int info) {
            AsyncResult ret = new AsyncResult(null, info, null);
            Message.obtain(mHandler, LISTEN_HD_STATUS_CHANGED_EVENT, 0, 0, ret).sendToTarget();
        }

        public void onSubsidyLockEvent(int subsidyLock) {
            AsyncResult ret = new AsyncResult(null, subsidyLock, null);
            Message.obtain(mHandler, LISTEN_SUBSIDYLOCK_EVENT, 0, 0, ret).sendToTarget();
        }

        public void onImsCsfbVendorCauseEvent(String causeCode) {
            AsyncResult ret = new AsyncResult(null, causeCode, null);
            Message.obtain(mHandler, LISTEN_IMS_CSFB_VENDOR_CAUSE_EVENT, 0, 0, ret).sendToTarget();
        }

        public void onCnapEvent(String name) {
            AsyncResult ret = new AsyncResult (null, name, null);
            Message.obtain(mHandler, LISTEN_CNAP_EVENT, 0, 0, ret).sendToTarget();
        }

        public void onModemStateChangedEvent(int status, String assertInfo) {
            ModemStatusIndication modemStatusIndication = new ModemStatusIndication(status, assertInfo);
            AsyncResult ret = new AsyncResult(null, modemStatusIndication, null);
            Message.obtain(mHandler, LISTEN_MODEM_STATE_CHANGED_EVENT, 0, 0, ret).sendToTarget();
        }
    };

    public void onRadiointeractorEvent() {
    }

    public void onRiConnectedEvent() {
    }

    public void onRadiointeractorEmbmsEvent() {
    }

    public void onSuppServiceFailedEvent(int service) {
    }

    public void onVideoPhoneCodecEvent(Object object) {
    }

    public void onVideoPhoneDsciEvent(Object object) {
    }

    public void onVideoPhoneStringEvent(Object object) {
    }

    public void onVideoPhoneRemoteMediaEvent(Object object) {
    }

    public void onVideoPhoneMMRingEvent(Object object) {
    }

    public void onVideoPhoneReleasingEvent(Object object) {
    }

    public void onVideoPhoneRecordVideoEvent(Object object) {
    }

    public void onVideoPhoneMediaStartEvent(Object object) {
    }

    public void onRauSuccessEvent() {
    }

    public void onClearCodeFallbackEvent() {
    }

    public void onRealSimStateChangedEvent() {
    }

    public void onExpireSimEvent(Object object) {
    }

    public void onEarlyMediaEvent(Object earlyMedia) {
    }

    public void onNetowrkErrorCodeChangedEvent(Object object) {
    }

    public void onAvailableNetworksEvent(Object object) {
    }

    public void onHdStatusChangedEvent(Object object) {
    }

    public void onImsCsfbVendorCauseEvent(Object object) {
    }

    public void onSubsidyLockEvent(Object object) {
    }

    public void onCnapEvent(Object object) {
    }

    public void onSignalConnectionStatusEvent(Object object) {
    }

    public void onSmartNrChangedEvent() {
    }

    public void onNrCfgInfoEvent(Object object) {
    }

    public void onModemStateChangedEvent(Object status, Object assertInfo) {
    }
}
