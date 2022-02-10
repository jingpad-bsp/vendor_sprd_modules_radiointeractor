
package com.android.sprd.telephony;

import com.android.sprd.telephony.RadioInteractorCore.SuppService;
import com.android.sprd.telephony.uicc.IccIoResult;
import com.android.sprd.telephony.uicc.IccUtils;
import com.android.sprd.telephony.linkturbo.LteSpeedAndSignalStrengthInfo;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.RemoteException;
import android.telephony.data.DataProfile;
import android.telephony.IccOpenLogicalChannelResponse;
import android.telephony.TelephonyManager;

import com.android.sprd.telephony.uicc.IccCardApplicationStatusEx;
import com.android.sprd.telephony.uicc.IccCardApplicationStatusEx.AppState;
import com.android.sprd.telephony.uicc.IccCardApplicationStatusEx.PersoSubState;
import com.android.sprd.telephony.uicc.IccCardStatusEx;
import com.android.sprd.telephony.uicc.IccCardStatusEx.CardState;
import com.android.sprd.telephony.uicc.IccCardApplicationStatusEx.AppType;

import android.content.res.Resources;

import com.android.internal.telephony.PhoneConstants;

import android.app.ActivityManagerNative;

import static android.Manifest.permission.READ_PHONE_STATE;

import android.os.UserHandle;
import android.content.Intent;
import android.os.SystemProperties;

public class RadioInteractorHandler extends Handler {
    public static final String TAG = "RadioInteractorHandler";

    RadioInteractorCore mRadioInteractorCore;
    RadioInteractorNotifier mRadioInteractorNotifier;
    SyncHandler mHandler;

    /*
     * This section defines all requests for events
     */
    protected static final int EVENT_GET_REQUEST_RADIOINTERACTOR_DONE = 1;
    protected static final int EVENT_INVOKE_OEM_RIL_REQUEST_STRINGS_DONE = 2;
    protected static final int EVENT_INVOKE_GET_SIM_CAPACITY_DONE = 3;
    protected static final int EVENT_INVOKE_ENABLE_RAU_NOTIFY_DONE = 4;
    protected static final int EVENT_GET_ATR_DONE = 5;
    // UNISOC: add for HIGH_DEF_AUDIO
    protected static final int EVENT_GET_HD_VOICE_STATE_DONE = 6;
    /*UNISOC: Bug#542214 Add support for store SMS to Sim card @{*/
    protected static final int EVENT_REQUEST_STORE_SMS_TO_SIM_DONE = 7;
    protected static final int EVENT_QUERY_SMS_STORAGE_MODE_DONE = 8;
    /* @} */
    // Explicit Transfer Call REFACTORING
    protected static final int EVENT_ECT_RESULT = 9;
    // UNISOC: add for trafficClass
    protected static final int EVENT_TRAFFIC_CLASS_DONE = 10;
    /* Add for Data Clear Code from Telcel @{ */
    protected static final int EVENT_SET_LTE_ENABLE_DONE = 11;
    protected static final int EVENT_ATTACH_DATA_DONE = 12;
    /* @} */
    // Add for shutdown optimization
    protected static final int EVENT_REQUEST_SHUTDOWN_DONE = 13;
    protected static final int EVENT_GET_REMIAN_TIMES_DONE = 14;
    protected static final int EVENT_GET_SIMLOCK_STATUS_DONE = 15;
    protected static final int EVENT_REQUEST_SET_SIM_POWER = 16;
    protected static final int EVENT_REQUEST_SET_PRE_NETWORK_TYPE = 17;
    protected static final int EVENT_REQUEST_UPDTAE_REAL_ECCLIST = 18;
    protected static final int EVENT_SET_SINGLE_PDN_DONE = 19;
    protected static final int EVENT_REQUEST_QUERY_COLP = 20;
    protected static final int EVENT_REQUEST_QUERY_COLR = 21;
    protected static final int EVENT_REQUEST_UPDATE_OPERATOR_NAME = 22;
    protected static final int EVENT_GET_REALL_SIM_STATUS_DONE = 23;
    protected static final int EVENT_REATTACH_DONE = 24;
    protected static final int EVENT_GET_SIMLOCK_DUMMYS = 25;
    protected static final int EVENT_GET_SIMLOCK_WHITE_LIST = 26;
    protected static final int EVENT_REQUEST_SET_LOCAL_TONE = 27;
    protected static final int EVENT_UPDATE_PLMN_DONE = 28;
    protected static final int EVENT_QUERY_PLMN_DONE = 29;
    protected static final int EVENT_GET_RADIO_PREFERENCE_DONE = 30;
    protected static final int EVENT_SET_RADIO_PREFERENCE_DONE = 31;
    protected static final int EVENT_GET_PREFERRED_NETWORK_TYPE_DONE = 32;
    protected static final int EVENT_REQUEST_ENABLE_RADIO_POWER_FALLBACK = 33;
    protected static final int EVENT_GET_CNAP_DONE = 34;
    protected static final int EVENT_GET_SUBSIDYLOCK_STATE_DONE = 35;
    protected static final int EVENT_DC_FORCE_DETACH_DONE = 36;
    protected static final int EVENT_REQUEST_SET_SIM_POWER_REAL = 37;
    protected static final int EVENT_REQUEST_RESET_MODEM = 38;
    protected static final int EVENT_GET_VOLTE_ALLOWED_PLMN = 39;
    protected static final int EVENT_SET_SMS_BEARER_DONE = 40;
    protected static final int EVENT_GET_SMS_BEARER_DONE = 41;
    protected static final int EVENT_REQUEST_QUERY_ROOT_NODE = 42;
    protected static final int EVENT_REQUEST_SET_PS_DATA_OFF = 43;
    protected static final int EVENT_GET_LTE_SPEED_AND_SIGNAL_STRENGTH = 44;
    protected static final int EVENT_REQUEST_ENABLE_NR_SWITCH = 45;
    protected static final int EVENT_REQUEST_SET_USB_SHARE_STATE_SWITCH = 46;
    protected static final int EVENT_SET_STAND_ALONE_DONE = 47;
    protected static final int EVENT_GET_STAND_ALONE_DONE = 48;

    protected static final int EVENT_UNSOL_RADIOINTERACTOR = 100;
    /**
     * Listen for update the list of embms programs.
     */
    protected static final int EVENT_UNSOL_RADIOINTERACTOR_EMBMS = 101;
    /**
     * Listen for RI has connected.
     */
    protected static final int EVENT_UNSOL_RI_CONNECTED = 102;
    protected static final int EVENT_UNSOL_SIMMGR_SIM_STATUS_CHANGED = 103;
    protected static final int EVENT_UNSOL_EXPIRE_SIM = 104;
    protected static final int EVENT_UNSOL_EARLY_MEDIA = 105;
    protected static final int EVENT_UNSOL_HD_STATUS_INFO = 106;
    protected static final int EVENT_UNSOL_SUBSIDYLOCK_STATE = 107;
    protected static final int EVENT_UNSOL_IMS_CSFB_VENDOR_CAUSE = 108;
    protected static final int EVENT_UNSOL_CNAP = 109;
    protected static final int EVENT_UNSOL_MODEM_STATE_CHANGED = 110;

    static protected final int RESPONSE_DATA_FILE_SIZE_1 = 2;
    static protected final int RESPONSE_DATA_FILE_SIZE_2 = 3;
    static protected final int COMMAND_GET_RESPONSE = 0xc0;
    static protected final int COMMAND_READ_BINARY = 0xb0;

    private PersoSubState mPersoSubState;
    private AppState mAppState;
    private AppType mAppType;
    Context mContext;
    private CardState mCardState;
    private int mSubsidyLockState = -1;

    public RadioInteractorHandler(RadioInteractorCore RadioInteractorCore,
                                  RadioInteractorNotifier RadioInteractorNotifier, Context context) {
        mRadioInteractorCore = RadioInteractorCore;
        mRadioInteractorNotifier = RadioInteractorNotifier;
        mContext = context;
        unsolicitedRegisters(this, EVENT_UNSOL_RADIOINTERACTOR);
        registerForRiConnected(this, EVENT_UNSOL_RI_CONNECTED);
        registerForRadioInteractorEmbms(this, EVENT_UNSOL_RADIOINTERACTOR_EMBMS);
        registerForRealSimStateChanged(this, EVENT_UNSOL_SIMMGR_SIM_STATUS_CHANGED);
        registerForEarlyMedia(this, EVENT_UNSOL_EARLY_MEDIA);
        registerForHdStatusChanged(this, EVENT_UNSOL_HD_STATUS_INFO);
        registerForImsCsfbVendorCause(this, EVENT_UNSOL_IMS_CSFB_VENDOR_CAUSE);
        registerForCnap(this, EVENT_UNSOL_CNAP);
        HandlerThread thread = new HandlerThread("RadioInteractor:SyncSender");
        thread.start();
        mHandler = new SyncHandler(thread.getLooper());
        registerForExpireSim(this, EVENT_UNSOL_EXPIRE_SIM);
        registerForSubsidyLock(this, EVENT_UNSOL_SUBSIDYLOCK_STATE);
        registerForModemStateChanged(this, EVENT_UNSOL_MODEM_STATE_CHANGED);
    }

    class SyncHandler extends Handler {
        SyncHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            AsyncResult ar;
            ThreadRequest request;
            String strCapacity[];
            UtilLog.logd(TAG, " handleMessage msg.what:" + msg.what);
            switch (msg.what) {

                case EVENT_GET_REQUEST_RADIOINTERACTOR_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    UtilLog.logd(TAG, "handleMessage EVENT_GET_REQUEST_RADIOINTERACTOR_DONE");
                    synchronized (request) {
                        if (ar.exception == null) {
                            request.result = (((int[]) ar.result))[0];
                        } else {
                            UtilLog.loge(TAG, "handleMessage registration state error!");
                            request.result = -1;
                        }
                        request.notifyAll();
                    }
                    break;
                case EVENT_INVOKE_OEM_RIL_REQUEST_STRINGS_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    String[] oemResp = (String[]) request.argument;
                    int returnOemValue = -1;
                    UtilLog.logd(TAG, "handleMessage EVENT_INVOKE_OEM_RIL_REQUEST_STRINGS_DONE");
                    synchronized (request) {
                        try {
                            if (ar.exception == null) {
                                if (ar.result != null) {
                                    String responseData = (String) (ar.result);
                                    UtilLog.logd(TAG, "responseData = " + responseData);
                                    oemResp[0] = responseData;
                                    returnOemValue = 0;
                                }
                            } else {
                                CommandException ex = (CommandException) ar.exception;
                                returnOemValue = ex.getCommandError().ordinal();
                                if (returnOemValue > 0)
                                    returnOemValue *= -1;
                            }
                        } catch (RuntimeException e) {
                            UtilLog.loge(TAG, "sendOemRilRequestRaw: Runtime Exception");
                            returnOemValue = (CommandException.Error.GENERIC_FAILURE.ordinal());
                            if (returnOemValue > 0)
                                returnOemValue *= -1;
                        }
                        request.result = returnOemValue;
                        request.notifyAll();
                    }
                    break;
                case EVENT_INVOKE_GET_SIM_CAPACITY_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    UtilLog.logd(TAG, "handleMessage EVENT_INVOKE_GET_SIM_CAPACITY_DONE");
                    synchronized (request) {
                        if (ar.exception == null) {
                            strCapacity = (String[]) ar.result;
                            if (strCapacity != null && strCapacity.length >= 2) {
                                UtilLog.logd(TAG, "[sms]sim used:" + strCapacity[0] + " total:"
                                        + strCapacity[1]);
                                request.result = strCapacity[0] + ":" + strCapacity[1];
                                UtilLog.logd(TAG, "[sms]simCapacity: " + request.result);
                            } else {
                                request.result = "ERROR";
                            }
                        } else {
                            request.result = "ERROR";
                            UtilLog.loge(TAG, "[sms]get sim capacity fail");
                        }
                        request.notifyAll();
                    }
                    break;
                case EVENT_INVOKE_ENABLE_RAU_NOTIFY_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    UtilLog.logd(TAG, "handleMessage EVENT_INVOKE_ENABLE_RAU_NOTIFY_DONE");
                    synchronized (request) {
                        if (ar.exception == null) {
                            request.result = ar;
                            UtilLog.logd(TAG, "enable rau: " + request.result);
                        } else {
                            UtilLog.loge(TAG, "enable rau:fail");
                        }
                        request.notifyAll();
                    }
                    break;

                case EVENT_GET_ATR_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    UtilLog.logd(TAG, "handleMessage EVENT_GET_ATR_DONE");
                    synchronized (request) {
                        if (ar.exception == null && ar.result != null) {
                            request.result = (String) ar.result;
                        } else {
                            request.result = "ERROR";
                            if (ar.result == null) {
                                UtilLog.loge(TAG, "iccGetAtr: Empty response");
                            }
                            if (ar.exception != null) {
                                UtilLog.loge(TAG, "iccGetAtr: Exception: " + ar.exception);
                            }
                        }
                        request.notifyAll();
                    }
                    break;

                case EVENT_GET_HD_VOICE_STATE_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    UtilLog.logd(TAG, "handleMessage EVENT_GET_HD_VOICE_STATE_DONE");
                    synchronized (request) {
                        if (ar.exception == null && ar.result != null) {
                            int resultArray[] = (int[]) ar.result;
                            request.result = (resultArray[0] == 1);
                        } else {
                            request.result = false;
                            if (ar.exception != null) {
                                UtilLog.loge(TAG, "get HD Voice state fail: " + ar.exception);
                            }
                        }
                        request.notifyAll();
                    }
                    break;

                /* UNISOC: Bug#542214 Add support for store SMS to Sim card @{ */
                case EVENT_REQUEST_STORE_SMS_TO_SIM_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    UtilLog.logd(TAG, "handleMessage EVENT_REQUEST_STORE_SMS_TO_SIM_DONE");
                    synchronized (request) {
                        if (ar.exception == null) {
                            request.result = true;
                            UtilLog.logd(TAG, "store sms to sim: " + request.result);
                        } else {
                            request.result = false;
                            UtilLog.loge(TAG, "store sms to sim:fail" + ar.exception);
                        }
                        request.notifyAll();
                    }
                    break;

                case EVENT_QUERY_SMS_STORAGE_MODE_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    UtilLog.logd(TAG, "handleMessage EVENT_QUERY_SMS_STORAGE_MODE_DONE");
                    synchronized (request) {
                        if (ar.exception == null && ar.result != null) {
                            request.result = (String) ar.result;
                        } else {
                            request.result = "ERROR";
                            if (ar.result == null) {
                                UtilLog.loge(TAG, "query sms storage mode: Empty response");
                            }
                            if (ar.exception != null) {
                                UtilLog.loge(TAG,
                                        "query sms storage mode: Exception: " + ar.exception);
                            }
                        }
                        request.notifyAll();
                    }
                    break;
                /* @} */

                case EVENT_ECT_RESULT:
                    ar = (AsyncResult) msg.obj;
                    if (ar.exception != null) {
                        UtilLog.logd(TAG, "Explicit call failed: " + ar.exception +
                                ", failed reason is : " + msg.what);
                        mRadioInteractorNotifier.notifySuppServiceFailed(mRadioInteractorCore.getPhoneId(),
                                getFailedService(msg.what));
                    }
                    break;

                case EVENT_TRAFFIC_CLASS_DONE:
                    ar = (AsyncResult) msg.obj;
                    UtilLog.logd(TAG, "handleMessage EVENT_TRAFFIC_CLASS_DONE");
                    if (ar.exception == null) {
                        UtilLog.logd(TAG, "traffic class :success");
                    } else {
                        UtilLog.loge(TAG, "traffic class :fail" + ar.exception);
                    }
                    break;

                case EVENT_SET_LTE_ENABLE_DONE:
                    ar = (AsyncResult) msg.obj;
                    UtilLog.logd(TAG, "handleMessage EVENT_SET_LTE_ENABLE_DONE");
                    if (ar.exception == null) {
                        UtilLog.logd(TAG, "set lte enable :success");
                    } else {
                        UtilLog.loge(TAG, "set lte enable :fail" + ar.exception);
                    }
                    break;

                case EVENT_ATTACH_DATA_DONE:
                    ar = (AsyncResult) msg.obj;
                    UtilLog.logd(TAG, "handleMessage EVENT_ATTACH_DATA_DONE");
                    if (ar.exception == null) {
                        UtilLog.logd(TAG, "attach data :success");
                    } else {
                        UtilLog.loge(TAG, "attach data :fail" + ar.exception);
                    }
                    break;

                case EVENT_REQUEST_SHUTDOWN_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    UtilLog.logd(TAG, "handleMessage EVENT_REQUEST_SHUTDOWN_DONE");
                    synchronized (request) {
                        if (ar.exception == null) {
                            request.result = true;
                        } else {
                            request.result = false;
                            UtilLog.loge(TAG, "shutdown fail: " + ar.exception);
                        }
                        request.notifyAll();
                    }
                    break;

                case EVENT_REQUEST_SET_SIM_POWER:
                    ar = (AsyncResult) msg.obj;
                    UtilLog.logd(TAG, "handleMessage EVENT_REQUEST_SET_SIM_POWER");
                    if (ar.exception == null) {
                        UtilLog.logd(TAG, "set sim power :success" + ar.result);
                    } else {
                        UtilLog.loge(TAG, "set sim power :fail");
                    }
                    break;

                case EVENT_GET_REMIAN_TIMES_DONE:
                case EVENT_GET_SIMLOCK_STATUS_DONE:
                case EVENT_GET_SUBSIDYLOCK_STATE_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    synchronized (request) {
                        if (ar.exception == null && ar.result != null) {
                            request.result = ar.result;
                        } else {
                            request.result = -1;
                        }
                        request.notifyAll();
                    }
                    break;

                case EVENT_REQUEST_SET_PRE_NETWORK_TYPE:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    UtilLog.logd(TAG, "handleMessage EVENT_REQUEST_SET_PRE_NETWORK_TYPE");
                    synchronized (request) {
                        if (ar.exception == null) {
                            request.result = 0;
                        } else {
                            request.result = -1;
                            UtilLog.loge(TAG, "set pre network type fail: " + ar.exception);
                        }
                        request.notifyAll();
                    }
                    break;

                case EVENT_REQUEST_UPDTAE_REAL_ECCLIST:
                    ar = (AsyncResult) msg.obj;
                    UtilLog.logd(TAG, "handleMessage EVENT_REQUEST_UPDTAE_REAL_ECCLIST");
                    if (ar.exception == null) {
                        UtilLog.logd(TAG, "update real ecclist :success");
                    } else {
                        UtilLog.loge(TAG, "update real ecclist :fail" + ar.exception);
                    }
                    break;
                /*UNISOC: bug618350 add single pdp allowed by plmns feature@{*/
                case EVENT_SET_SINGLE_PDN_DONE:
                    ar = (AsyncResult) msg.obj;
                    UtilLog.logd(TAG, "handleMessage EVENT_SET_SINGLE_PDN_DONE");
                    if (ar.exception == null) {
                        UtilLog.logd(TAG, "set single pdn :success");
                    } else {
                        UtilLog.loge(TAG, "set single pdn :fail" + ar.exception);
                    }
                    break;
                /* @} */
                case EVENT_REQUEST_QUERY_COLP:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    UtilLog.logd(TAG, "handleMessage EVENT_REQUEST_QUERY_COLP");
                    synchronized (request) {
                        if (ar.exception == null && ar.result != null) {
                            int resultArray[] = (int[]) ar.result;
                            request.result = resultArray[0];
                        } else {
                            request.result = -1;
                            if (ar.exception != null) {
                                UtilLog.loge(TAG, "query colp fail: " + ar.exception);
                            }
                        }
                        request.notifyAll();
                    }
                    break;

                case EVENT_REQUEST_QUERY_COLR:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    UtilLog.logd(TAG, "handleMessage EVENT_REQUEST_QUERY_COLR");
                    synchronized (request) {
                        if (ar.exception == null && ar.result != null) {
                            int resultArray[] = (int[]) ar.result;
                            request.result = resultArray[0];
                        } else {
                            request.result = -1;
                            if (ar.exception != null) {
                                UtilLog.loge(TAG, "query colr fail: " + ar.exception);
                            }
                        }
                        request.notifyAll();
                    }
                    break;

                case EVENT_REQUEST_UPDATE_OPERATOR_NAME:
                    ar = (AsyncResult) msg.obj;
                    UtilLog.logd(TAG, "handleMessage EVENT_REQUEST_UPDATE_OPERATOR_NAME");
                    if (ar.exception == null) {
                        UtilLog.logd(TAG, "update operator name :success");
                    } else {
                        UtilLog.loge(TAG, "update operator name :fail" + ar.exception);
                    }
                    break;

                case EVENT_REATTACH_DONE:
                    ar = (AsyncResult) msg.obj;
                    UtilLog.logd(TAG, "handleMessage EVENT_REATTACH_DONE");
                    if (ar.exception == null) {
                        UtilLog.logd(TAG, "reattach :success");
                    } else {
                        UtilLog.loge(TAG, "reattach :fail" + ar.exception);
                    }
                    break;

                case EVENT_GET_SIMLOCK_DUMMYS:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    UtilLog.logd(TAG, "handleMessage EVENT_GET_SIMLOCK_DUMMYS");
                    synchronized (request) {
                        if (ar.exception == null && ar.result != null) {
                            request.result = (int[]) ar.result;
                        } else {
                            request.result = null;
                            if (ar.result == null) {
                                UtilLog.loge(TAG, "get simlock dummys: Empty response");
                            }
                            if (ar.exception != null) {
                                UtilLog.loge(TAG,
                                        "get simlock dummys: Exception: " + ar.exception);
                            }
                        }
                        request.notifyAll();
                    }
                    break;

                case EVENT_GET_SIMLOCK_WHITE_LIST:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    UtilLog.logd(TAG, "handleMessage EVENT_GET_SIMLOCK_WHITE_LIST");
                    synchronized (request) {
                        if (ar.exception == null && ar.result != null) {
                            request.result = (String) ar.result;
                        } else {
                            request.result = "ERROR";
                            if (ar.result == null) {
                                UtilLog.loge(TAG, "get simlock white list: Empty response");
                            }
                            if (ar.exception != null) {
                                UtilLog.loge(TAG,
                                        "get simlock white list: Exception: " + ar.exception);
                            }
                        }
                        request.notifyAll();
                    }
                    break;

                case EVENT_REQUEST_SET_LOCAL_TONE:
                    ar = (AsyncResult) msg.obj;
                    UtilLog.logd(TAG, "handleMessage EVENT_REQUEST_SET_LOCAL_TONE");
                    if (ar.exception == null) {
                        UtilLog.logd(TAG, "set local tone :success");
                    } else {
                        UtilLog.loge(TAG, "set local tone :fail" + ar.exception);
                    }
                    break;

                case EVENT_UPDATE_PLMN_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    synchronized (request) {
                        if (ar.exception == null) {
                            request.result = 1;
                        } else {
                            request.result = -1;
                        }
                        request.notifyAll();
                    }
                    break;

                case EVENT_QUERY_PLMN_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    synchronized (request) {
                        if (ar.exception == null && ar.result != null) {
                            request.result = ar.result;
                        } else {
                            request.result = "";
                        }
                        request.notifyAll();
                    }
                    break;

                case EVENT_REQUEST_SET_SIM_POWER_REAL:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    UtilLog.logd(TAG, "handleMessage EVENT_REQUEST_SET_SIM_POWER_REAL");
                    synchronized (request) {
                        if (ar.exception == null) {
                            request.result = true;
                        } else {
                            request.result = false;
                            UtilLog.loge(TAG, "set sim power readl fail: " + ar.exception);
                        }
                        request.notifyAll();
                    }
                    break;

                case EVENT_REQUEST_RESET_MODEM:
                    ar = (AsyncResult) msg.obj;
                    UtilLog.logd(TAG, "handleMessage EVENT_REQUEST_RESET_MODEM");
                    if (ar.exception == null) {
                        UtilLog.logd(TAG, "reset modem :success");
                    } else {
                        UtilLog.loge(TAG, "reset modem :fail" + ar.exception);
                    }
                    break;

                case EVENT_GET_RADIO_PREFERENCE_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    synchronized (request) {
                        if (ar.exception == null && ar.result != null) {
                            request.result = ar.result;
                        } else {
                            request.result = "";
                        }
                        request.notifyAll();
                    }
                    break;

                case EVENT_SET_RADIO_PREFERENCE_DONE:
                    ar = (AsyncResult) msg.obj;
                    if (ar.exception == null) {
                        UtilLog.logd(TAG, "set radio preference: success");
                    } else {
                        UtilLog.loge(TAG, "set radio preference: fail" + ar.exception);
                    }
                    break;

                case EVENT_GET_PREFERRED_NETWORK_TYPE_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    UtilLog.logd(TAG, "handleMessage EVENT_GET_PREFERRED_NETWORK_TYPE_DONE");
                    synchronized (request) {
                        if (ar.exception == null && ar.result != null) {
                            request.result = (((int[]) ar.result))[0];
                        } else {
                            request.result = -1;
                            UtilLog.loge(TAG, "get pre network type fail: " + ar.exception);
                        }
                        request.notifyAll();
                    }
                    break;

                case EVENT_REQUEST_ENABLE_RADIO_POWER_FALLBACK:
                    ar = (AsyncResult) msg.obj;
                    UtilLog.logd(TAG, "handleMessage EVENT_REQUEST_ENABLE_RADIO_POWER_FALLBACK");
                    if (ar.exception == null) {
                        UtilLog.logd(TAG, "enableRadioPowerFallback :success");
                    } else {
                        UtilLog.loge(TAG, "enableRadioPowerFallback :fail" + ar.exception);
                    }
                    break;

                case EVENT_REQUEST_SET_PS_DATA_OFF:
                    ar = (AsyncResult) msg.obj;
                    UtilLog.logd(TAG, "handleMessage EVENT_REQUEST_SET_PS_DATA_OFF");
                    if (ar.exception == null) {
                        UtilLog.logd(TAG, "setPsDataOff success");
                    } else {
                        UtilLog.loge(TAG, "setPsDataOff fail" + ar.exception);
                    }
                    break;

                case EVENT_GET_CNAP_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    UtilLog.logd(TAG, "handleMessage EVENT_GET_CNAP_DONE");
                    synchronized (request) {
                        if (ar.exception == null && ar.result != null) {
                            request.result = (((int[]) ar.result))[0];
                        } else {
                            request.result = -1;
                            UtilLog.loge(TAG, "get CNAP fail: " + ar.exception);
                        }
                        request.notifyAll();
                    }
                    break;

                case EVENT_DC_FORCE_DETACH_DONE:
                    ar = (AsyncResult) msg.obj;
                    Messenger messenger = (Messenger) ar.userObj;
                    try {
                        messenger.send(Message.obtain());
                    } catch (RemoteException e) {
                        UtilLog.loge(TAG, "Exception in notifyMessenger: " + e);
                    }
                    break;

                case EVENT_GET_VOLTE_ALLOWED_PLMN:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    UtilLog.logd(TAG, "handleMessage EVENT_GET_VOLTE_ALLOWED_PLMN");
                    synchronized (request) {
                        if (ar.exception == null && ar.result != null) {
                            request.result = ar.result;
                        } else {
                            request.result = -1;
                            UtilLog.loge(TAG, "getVoLTEAllowedPLMN fail: " + ar.exception);
                        }
                        request.notifyAll();
                    }
                    break;

                case EVENT_SET_SMS_BEARER_DONE:
                    ar = (AsyncResult) msg.obj;
                    UtilLog.logd(TAG, "handleMessage EVENT_SET_SMS_BEARER_DONE");
                    if (ar.exception == null) {
                        UtilLog.logd(TAG, "set sms bearer:success");
                    } else {
                        UtilLog.loge(TAG, "set sms bearer:fail");
                    }
                    break;

                case EVENT_GET_SMS_BEARER_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    UtilLog.logd(TAG, "handleMessage EVENT_GET_SMS_BEARER_DONE");
                    synchronized (request) {
                        if (ar.exception == null && ar.result != null) {
                            request.result = (((int[]) ar.result))[0];
                        } else {
                            request.result = 0;
                            UtilLog.loge(TAG, "get SmsBearer fail: " + ar.exception);
                        }
                        request.notifyAll();
                    }
                    break;

                case EVENT_REQUEST_QUERY_ROOT_NODE:
                    ar = (AsyncResult) msg.obj;
                    UtilLog.logd(TAG, "handleMessage EVENT_REQUEST_QUERY_ROOT_NODE");
                    if (ar.exception == null) {
                        UtilLog.logd(TAG, "query root node :success" + ar.result);
                    } else {
                        UtilLog.loge(TAG, "query root node :fail");
                    }
                    break;

                case EVENT_GET_LTE_SPEED_AND_SIGNAL_STRENGTH:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    synchronized (request) {
                        if (ar.exception == null && ar.result != null) {
                            request.result = (LteSpeedAndSignalStrengthInfo) (ar.result);
                        } else {
                            request.result = null;
                            UtilLog.loge(TAG, "getLteSpeedAndSignalStrength fail: " + ar.exception);
                        }
                        request.notifyAll();
                    }
                    break;
                case EVENT_REQUEST_ENABLE_NR_SWITCH:
                    ar = (AsyncResult) msg.obj;
                    UtilLog.logd(TAG, "handleMessage EVENT_REQUEST_ENABLE_NR_SWITCH");
                    if (ar.exception == null) {
                        UtilLog.logd(TAG, "enable nr switch:success");
                    } else {
                        UtilLog.loge(TAG, "enable nr switch:fail");
                    }
                    break;
                case EVENT_REQUEST_SET_USB_SHARE_STATE_SWITCH:
                    ar = (AsyncResult) msg.obj;
                    UtilLog.logd(TAG, "handleMessage EVENT_REQUEST_SET_USB_SHARE_STATE_SWITCH");
                    if (ar.exception == null) {
                        UtilLog.logd(TAG, "setUsbShareStateSwitch :success");
                    } else {
                        UtilLog.loge(TAG, "setUsbShareStateSwitch :fail" + ar.exception);
                    }
                    break;
                case EVENT_SET_STAND_ALONE_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    UtilLog.logd(TAG, "handleMessage EVENT_SET_STAND_ALONE_DONE");
                    synchronized (request) {
                        if (ar.exception == null) {
                            UtilLog.logd(TAG, "set stand alone:success");
                            request.result = 0;
                        } else {
                            UtilLog.loge(TAG, "set stand alone:fail");
                            request.result = -1;
                        }
                        request.notifyAll();
                    }
                    break;
                case EVENT_GET_STAND_ALONE_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (ThreadRequest) ar.userObj;
                    UtilLog.logd(TAG, "handleMessage EVENT_GET_STAND_ALONE_DONE");
                    synchronized (request) {
                        if (ar.exception == null && ar.result != null) {
                            request.result = (((int[]) ar.result))[0];
                        } else {
                            request.result = -1;
                            UtilLog.loge(TAG, "get stand alone fail: " + ar.exception);
                        }
                        request.notifyAll();
                    }
                    break;
                default:
                    throw new RuntimeException("Unrecognized request event radiointeractor: " + msg.what);
            }
        }
    }

    @Override
    public void handleMessage(Message msg) {
        AsyncResult ar;
        switch (msg.what) {

            case EVENT_UNSOL_RADIOINTERACTOR:
                ar = (AsyncResult) msg.obj;
                if (ar.exception == null) {
                    UtilLog.logd(TAG, "EVENT_UNSOL_RADIOINTERACTOR");
                    mRadioInteractorNotifier
                            .notifyRadiointeractorEventForSubscriber(mRadioInteractorCore
                                    .getPhoneId());
                } else {
                    UtilLog.loge(TAG, "unsolicitedRegisters exception: " + ar.exception);
                }
                break;

            case EVENT_UNSOL_RI_CONNECTED:
                ar = (AsyncResult) msg.obj;
                if (ar.exception == null) {
                    UtilLog.logd(TAG, "EVENT_UNSOL_RI_CONNECTED");
                } else {
                    UtilLog.loge(TAG, "unsolicitedRiConnected exception: " + ar.exception);
                }
                break;

            case EVENT_UNSOL_RADIOINTERACTOR_EMBMS:
                ar = (AsyncResult) msg.obj;
                if (ar.exception == null) {
                    UtilLog.logd(TAG, "EVENT_UNSOL_RADIOINTERACTOR_EMBMS");
                    mRadioInteractorNotifier
                            .notifyRadiointeractorEventForEmbms(mRadioInteractorCore
                                    .getPhoneId());
                } else {
                    UtilLog.loge(TAG, "unsolicitedRadioInteractorEmbms exception: " + ar.exception);
                }
                break;

            case EVENT_UNSOL_SIMMGR_SIM_STATUS_CHANGED:
                ar = (AsyncResult) msg.obj;
                if (ar.exception == null) {
                    UtilLog.logd(TAG, "EVENT_UNSOL_SIMMGR_SIM_STATUS_CHANGED - "
                            + mRadioInteractorCore.getPhoneId());
                    mRadioInteractorCore.simmgrGetSimStatus(obtainMessage(EVENT_GET_REALL_SIM_STATUS_DONE));
                } else {
                    UtilLog.loge(TAG, "unsolicitedRealSimStateChanged exception: " + ar.exception);
                }
                break;

            case EVENT_UNSOL_EXPIRE_SIM:
                ar = (AsyncResult) msg.obj;
                if (ar.exception == null && ar.result != null) {
                    UtilLog.logd(TAG, "EVENT_UNSOL_SIMLOCK_SIM_EXPIRED ");
                    int result = (int) ar.result;
                    UtilLog.logd(TAG, "result = " + result);
                    mRadioInteractorNotifier.notifyExpireSimEvent(result, mRadioInteractorCore.getPhoneId());
                } else {
                    UtilLog.loge(TAG, "unsolicitedBandInfo exception: " + ar.exception);
                }
                break;

            case EVENT_UNSOL_EARLY_MEDIA:
                ar = (AsyncResult) msg.obj;
                if (ar.exception == null && ar.result != null) {
                    UtilLog.logd(TAG, "EVENT_UNSOL_EARLY_MEDIA");
                    int result = (int) ar.result;
                    UtilLog.logd(TAG, "result = " + result);
                    mRadioInteractorNotifier
                            .notifyRadiointeractorEventForEarlyMedia(result, mRadioInteractorCore
                                    .getPhoneId());
                } else {
                    UtilLog.loge(TAG, "unsolicited early media exception: " + ar.exception);
                }
                break;

            case EVENT_GET_REALL_SIM_STATUS_DONE:
                ar = (AsyncResult) msg.obj;
                onGetRealIccCardStatusDone(ar);
                break;

            case EVENT_UNSOL_HD_STATUS_INFO:
                ar = (AsyncResult) msg.obj;
                if (ar.exception == null && ar.result != null) {
                    UtilLog.logd(TAG, "EVENT_UNSOL_HD_STATUS_INFO");
                    mRadioInteractorNotifier
                            .notifyRadiointeractorHdStatusInfo((int) ar.result, mRadioInteractorCore
                                    .getPhoneId());
                } else {
                    UtilLog.loge(TAG, "unsolicitedHdStatusInfo exception: " + ar.exception);
                }
                break;

            case EVENT_UNSOL_SUBSIDYLOCK_STATE:
                ar = (AsyncResult) msg.obj;
                if (ar.exception == null && ar.result != null) {
                    UtilLog.logd(TAG, "EVENT_UNSOL_SUBSIDYLOCK_STATE ");
                    int result = (int) ar.result;
                    UtilLog.logd(TAG, "result = " + result);
                    mSubsidyLockState = result;
                    mRadioInteractorNotifier.notifySubsidyLockEvent(result, mRadioInteractorCore.getPhoneId());
                } else {
                    UtilLog.loge(TAG, "unsolicitedBandInfo exception: " + ar.exception);
                }
                break;

            case EVENT_UNSOL_IMS_CSFB_VENDOR_CAUSE:
                ar = (AsyncResult) msg.obj;
                if (ar.exception == null && ar.result != null) {
                    UtilLog.logd(TAG, "EVENT_UNSOL_IMS_CSFB_VENDOR_CAUSE");
                    mRadioInteractorNotifier
                            .notifyRadiointeractorImsCsfbVendorCauseInfo((String) ar.result, mRadioInteractorCore
                                    .getPhoneId());
                } else {
                    UtilLog.loge(TAG, "unsolicitedImsCsfbVendorCause exception: " + ar.exception);
                }
                break;

            case EVENT_UNSOL_CNAP:
                ar = (AsyncResult) msg.obj;
                if (ar.exception == null && ar.result != null) {
                    UtilLog.logd(TAG, "EVENT_UNSOL_CNAP");
                    mRadioInteractorNotifier
                            .notifyRadiointeractorCnap((String)ar.result, mRadioInteractorCore
                                    .getPhoneId());
                } else {
                    UtilLog.loge(TAG, "unsolicitedCnap exception: " + ar.exception);
                }
                break;

            case EVENT_UNSOL_MODEM_STATE_CHANGED:
                ar = (AsyncResult) msg.obj;
                if (ar.exception == null && ar.result != null) {
                    UtilLog.logd(TAG, "EVENT_UNSOL_MODEM_STATE_CHANGED");
                    ModemStatusIndication modemStatusIndication = (ModemStatusIndication) ar.result;
                    mRadioInteractorNotifier
                        .notifyModemStateChanegd(modemStatusIndication.getStatus(), modemStatusIndication.getAssertInfo(),
                        mRadioInteractorCore.getPhoneId());
                } else {
                    UtilLog.loge(TAG, "unsolicitedModemStateChanegd exception: " + ar.exception);
                }
                break;
            default:
                throw new RuntimeException("Unrecognized event unsol radiointeractor: " + msg.what);

        }
    }

    private synchronized void onGetRealIccCardStatusDone(AsyncResult ar) {
        UtilLog.logd(TAG, "onGetRealIccCardStatusDone");
        if (ar.exception != null) {
            UtilLog.loge(TAG, "Error getting ICC status. "
                    + "RIL_REQUEST_GET_ICC_STATUS should "
                    + "never return an error :" + ar.exception);
            return;
        }
        IccCardStatusEx ics = (IccCardStatusEx) ar.result;
        if (!ics.mCardState.equals(mCardState)) {
            mCardState = ics.mCardState;
            UtilLog.logd(TAG, "Notify real SIM state changed: " + mCardState);
            mRadioInteractorNotifier.notifyRealSimStateChanged(mRadioInteractorCore.getPhoneId());
            if (SystemProperties.getInt("persist.vendor.radio.vsim.product", 0) >= 1) {
                broadcastIccStateChangedIntent(mCardState);
            }
        }
    }

    private static final class ThreadRequest {
        public Object argument;
        public Object result;

        public ThreadRequest(Object argument) {
            this.argument = argument;
        }
    }

    private void waitForResult(ThreadRequest request) {
        try {
            request.wait();
        } catch (InterruptedException e) {
            UtilLog.logd(TAG, "interrupted while trying to get remain times");
        }
    }

    /**
     * Some fields (like ICC ID) in GSM SIMs are stored as nibble-swizzled BCH
     */
    private String
    bchToString(byte[] data, int offset, int length) {
        StringBuilder ret = new StringBuilder(length * 2);

        for (int i = offset; i < offset + length; i++) {
            int v;

            v = data[i] & 0xf;
            ret.append("0123456789abcdef".charAt(v));

            v = (data[i] >> 4) & 0xf;
            ret.append("0123456789abcdef".charAt(v));
        }

        return ret.toString();
    }

    private SuppService getFailedService(int what) {
        switch (what) {
            case EVENT_ECT_RESULT:
                return SuppService.TRANSFER;
        }
        return SuppService.UNKNOWN;
    }

    public int getSimlockTypes(PersoSubState persoSubState) {
        int simlockType = 0;
        if (persoSubState == PersoSubState.PERSOSUBSTATE_SIM_NETWORK) {
            UtilLog.logd(TAG, "Notifying registrants: NetworkLocked");
            simlockType = TelephonyManager.SIM_STATE_NETWORK_LOCKED;
        } else if (persoSubState == PersoSubState.PERSOSUBSTATE_SIM_NETWORK_SUBSET) {
            UtilLog.logd(TAG, "Notifying registrants: NetworkSubsetLocked");
            simlockType = IccCardStatusEx.SIM_STATE_NETWORKSUBSET_LOCKED;
        } else if (persoSubState == PersoSubState.PERSOSUBSTATE_SIM_SERVICE_PROVIDER) {
            UtilLog.logd(TAG, "Notifying registrants: ServiceProviderLocked");
            simlockType = IccCardStatusEx.SIM_STATE_SERVICEPROVIDER_LOCKED;
        } else if (persoSubState == PersoSubState.PERSOSUBSTATE_SIM_CORPORATE) {
            UtilLog.logd(TAG, "Notifying registrants: corporateLocked");
            simlockType = IccCardStatusEx.SIM_STATE_CORPORATE_LOCKED;
        } else if (persoSubState == PersoSubState.PERSOSUBSTATE_SIM_SIM) {
            UtilLog.logd(TAG, "Notifying registrants: simLocked");
            simlockType = IccCardStatusEx.SIM_STATE_SIM_LOCKED;
        } else if (persoSubState == PersoSubState.PERSOSUBSTATE_SIM_NETWORK_PUK) {
            UtilLog.logd(TAG, "Notifying registrants: NetworkLocked puk");
            simlockType = IccCardStatusEx.SIM_STATE_NETWORK_LOCKED_PUK;
        } else if (persoSubState == PersoSubState.PERSOSUBSTATE_SIM_NETWORK_SUBSET_PUK) {
            UtilLog.logd(TAG, "Notifying registrants: NetworkSubsetLocked puk");
            simlockType = IccCardStatusEx.SIM_STATE_NETWORK_SUBSET_LOCKED_PUK;
        } else if (persoSubState == PersoSubState.PERSOSUBSTATE_SIM_CORPORATE_PUK) {
            UtilLog.logd(TAG, "Notifying registrants: corporateLocked puk");
            simlockType = IccCardStatusEx.SIM_STATE_CORPORATE_LOCKED_PUK;
        } else if (persoSubState == PersoSubState.PERSOSUBSTATE_SIM_SERVICE_PROVIDER_PUK) {
            UtilLog.logd(TAG, "Notifying registrants: ServiceProviderLocked puk");
            simlockType = IccCardStatusEx.SIM_STATE_SERVICE_PROVIDER_LOCKED_PUK;
        } else if (persoSubState == PersoSubState.PERSOSUBSTATE_SIM_SIM_PUK) {
            UtilLog.logd(TAG, "Notifying registrants: simLocked puk");
            simlockType = IccCardStatusEx.SIM_STATE_SIM_LOCKED_PUK;
        } else if (persoSubState == PersoSubState.PERSOSUBSTATE_SIM_LOCK_FOREVER) {
            UtilLog.logd(TAG, "Notifying registrants: simlock forever");
            simlockType = IccCardStatusEx.SIM_STATE_SIM_LOCKED_FOREVER;
        } else {
            simlockType = 0;
        }
        return simlockType;
    }

    private int parsePinPukErrorResultEx(AsyncResult ar) {
        int[] result = (int[]) ar.result;
        if (result == null) {
            return -1;
        } else {
            int length = result.length;
            int attemptsRemaining = -1;
            if (length > 0) {
                attemptsRemaining = result[0];
            }
            UtilLog.logd(TAG, "parsePinPukErrorResult: attemptsRemaining=" + attemptsRemaining);
            return attemptsRemaining;
        }
    }

    /**
     * Make sure the caller has the SPRD_MODIFY_PHONE_STATE permission.
     *
     * @throws SecurityException if the caller does not have the required permission
     */
    private void enforceSprdModifyPermission(String packageName) {
        /*int resultOfCheck = mContext.getPackageManager()
                .checkPermission(android.Manifest.permission.SPRD_MODIFY_PHONE_STATE, packageName);
        if (resultOfCheck != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException(
                    "Neither user " + Binder.getCallingUid() + " nor current process has " +
                            android.Manifest.permission.SPRD_MODIFY_PHONE_STATE +
                            ".");
        }*/
    }

    //broadcast Icc State Changed Intent for vsim even the sim state not change
    private void broadcastIccStateChangedIntent(CardState state) {
        int phoneId = mRadioInteractorCore.getPhoneId();
        Intent intent = new Intent("android.intent.action.VSIM_STATE_CHANGED" + phoneId);
        intent.putExtra("phoneId", phoneId);
        intent.putExtra("state", state);
        UtilLog.logd(TAG, "broadcastIccStateChangedIntent intent android.intent.action.VSIM_STATE_CHANGED for phoneId " + phoneId);
        ActivityManagerNative.broadcastStickyIntent(intent, READ_PHONE_STATE,
                UserHandle.USER_ALL);
    }


    // --- for register

    public void unsolicitedRegisters(Handler h, int what) {
        mRadioInteractorCore
                .registerForUnsolRadioInteractor(h, what, null);
    }

    public void unregisterForUnsolRadioInteractor(Handler h) {
        mRadioInteractorCore.unregisterForUnsolRadioInteractor(h);
    }

    public void registerForRiConnected(Handler h, int what) {
        mRadioInteractorCore.registerForUnsolRiConnected(h, what, null);
    }

    public void unregisterForRiConnected(Handler h) {
        mRadioInteractorCore.unregisterForUnsolRiConnected(h);
    }

    public void registerForRadioInteractorEmbms(Handler h, int what) {
        mRadioInteractorCore.registerForUnsolRadioInteractor(h, what, null);
    }

    public void unregisterForRadioInteractorEmbms(Handler h) {
        mRadioInteractorCore.unregisterForUnsolRadioInteractor(h);
    }

    public void registerForsetOnVPCodec(Handler h, int what) {
        mRadioInteractorCore.registerForsetOnVPCodec(h, what, null);
    }

    public void unregisterForsetOnVPCodec(Handler h) {
        mRadioInteractorCore.unregisterForsetOnVPCodec(h);
    }

    public void registerForsetOnVPFallBack(Handler h, int what) {
        mRadioInteractorCore.registerForsetOnVPFallBack(h, what, null);
    }

    public void unregisterForsetOnVPFallBack(Handler h) {
        mRadioInteractorCore.unregisterForsetOnVPFallBack(h);
    }

    public void registerForsetOnVPString(Handler h, int what) {
        mRadioInteractorCore.registerForsetOnVPString(h, what, null);
    }

    public void unregisterForsetOnVPString(Handler h) {
        mRadioInteractorCore.unregisterForsetOnVPString(h);
    }

    public void registerForsetOnVPRemoteMedia(Handler h, int what) {
        mRadioInteractorCore.registerForsetOnVPRemoteMedia(h, what, null);
    }

    public void unregisterForsetOnVPRemoteMedia(Handler h) {
        mRadioInteractorCore.unregisterForsetOnVPRemoteMedia(h);
    }

    public void registerForsetOnVPMMRing(Handler h, int what) {
        mRadioInteractorCore.registerForsetOnVPMMRing(h, what, null);
    }

    public void unregisterForsetOnVPMMRing(Handler h) {
        mRadioInteractorCore.unregisterForsetOnVPMMRing(h);
    }

    public void registerForsetOnVPFail(Handler h, int what) {
        mRadioInteractorCore.registerForsetOnVPFail(h, what, null);
    }

    public void unregisterForsetOnVPFail(Handler h) {
        mRadioInteractorCore.unregisterForsetOnVPFail(h);
    }

    public void registerForsetOnVPRecordVideo(Handler h, int what) {
        mRadioInteractorCore.registerForsetOnVPRecordVideo(h, what, null);
    }

    public void unregisterForsetOnVPRecordVideo(Handler h) {
        mRadioInteractorCore.unregisterForsetOnVPRecordVideo(h);
    }

    public void registerForsetOnVPMediaStart(Handler h, int what) {
        mRadioInteractorCore.registerForsetOnVPMediaStart(h, what, null);
    }

    public void unregisterForsetOnVPMediaStart(Handler h) {
        mRadioInteractorCore.unregisterForsetOnVPMediaStart(h);
    }

    public void registerForRauSuccess(Handler h, int what) {
        mRadioInteractorCore.registerForRauSuccess(h, what, null);
    }

    public void unregisterForRauSuccess(Handler h) {
        mRadioInteractorCore.unregisterForRauSuccess(h);
    }

    public void registerForClearCodeFallback(Handler h, int what) {
        mRadioInteractorCore.registerForClearCodeFallback(h, what, null);
    }

    public void unregisterForClearCodeFallback(Handler h) {
        mRadioInteractorCore.unregisterForClearCodeFallback(h);
    }

    public void registerForRealSimStateChanged(Handler h, int what) {
        mRadioInteractorCore.registerForRealSimStateChanged(h, what, null);
    }

    public void unregisterForRealSimStateChanged(Handler h) {
        mRadioInteractorCore.unregisterForRealSimStateChanged(h);
    }

    public void registerForSubsidyLock(Handler h, int what) {
        mRadioInteractorCore.registerForSubsidyLock(h, what, null);
    }

    public void unregisterForSubsidyLock(Handler h) {
        mRadioInteractorCore.unregisterForSubsidyLock(h);
    }


    public void registerForExpireSim(Handler h, int what) {
        mRadioInteractorCore.registerForExpireSim(h, what, null);
    }

    public void unregisterForExpireSim(Handler h) {
        mRadioInteractorCore.unregisterForExpireSim(h);
    }

    public void registerForEarlyMedia(Handler h, int what) {
        mRadioInteractorCore.registerForEarlyMedia(h, what, null);
    }

    public void unregisterForEarlyMedia(Handler h) {
        mRadioInteractorCore.unregisterForEarlyMedia(h);
    }

    public void registerForHdStatusChanged(Handler h, int what) {
        mRadioInteractorCore.registerForHdStautsChanged(h, what, null);
    }

    public void unregisterForHdStatusChanged(Handler h) {
        mRadioInteractorCore.unregisterForHdStautsChanged(h);
    }

    public void registerForNetowrkErrorCode(Handler h, int what) {
        mRadioInteractorCore.registerForNetowrkErrorCode(h, what, null);
    }

    public void unregisterForNetowrkErrorCode(Handler h) {
        mRadioInteractorCore.unregisterForNetowrkErrorCode(h);
    }

    public void registerForAvailableNetworks(Handler h, int what) {
        mRadioInteractorCore.registerForAvailableNetworks(h, what, null);
    }

    public void unregisterForAvailableNetworks(Handler h) {
        mRadioInteractorCore.unregisterForAvailableNetworks(h);
    }

    public void unregisterForImsVideoQos(Handler h) {
        mRadioInteractorCore.unregisterForImsVideoQos(h);
    }

    public void registerForImsCsfbVendorCause(Handler h, int what) {
        mRadioInteractorCore.registerForImsCsfbVendorCause(h, what, null);
    }

    public void unregisterForImsCsfbVendorCause(Handler h) {
        mRadioInteractorCore.unregisterForImsCsfbVendorCause(h);
    }

    public void registerForCnap(Handler h, int what) {
        mRadioInteractorCore.registerForCnap(h, what, null);
    }

    public void unregisterForCnap(Handler h) {
        mRadioInteractorCore.unregisterForCnap(h);
    }

    public void registerForSignalConnectionStatus(Handler h, int what) {
        mRadioInteractorCore.registerForSignalConnectionStatus(h, what, null);
    }

    public void unregisterForSignalConnectionStatus(Handler h) {
        mRadioInteractorCore.unregisterForSignalConnectionStatus(h);
    }

    public void registerForSmartNrChanged(Handler h, int what) {
        mRadioInteractorCore.registerForSmartNrChanged(h, what, null);
    }

    public void unregisterForSmartNrChanged(Handler h) {
        mRadioInteractorCore.unregisterForSmartNrChanged(h);
    }

    public void registerForNrCfgInfo(Handler h, int what) {
        mRadioInteractorCore.registerForNrCfgInfo(h, what, null);
    }

    public void unregisterForNrCfgInfo(Handler h) {
        mRadioInteractorCore.unregisterForNrCfgInfo(h);
    }

    public void registerForModemStateChanged(Handler h, int what) {
        mRadioInteractorCore.registerForModemStateChanged(h, what, null);
    }

    public void unregisterForModemStateChanged(Handler h) {
        mRadioInteractorCore.unregisterForModemStateChanged(h);
    }
    // --- register end.

    // --- for request

    public int invokeOemRILRequestStrings(String oemReq, String[] oemResp) {
        ThreadRequest request = new ThreadRequest(oemResp);
        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_INVOKE_OEM_RIL_REQUEST_STRINGS_DONE,
                    request);
            mRadioInteractorCore.sendCmdAsync(oemReq, response);
            waitForResult(request);
        }
        try {
            return (Integer) request.result;
        } catch (ClassCastException e) {
            return -1;
        }
    }

    public String getSimCapacity() {
        ThreadRequest request = new ThreadRequest(null);
        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_INVOKE_GET_SIM_CAPACITY_DONE, request);
            mRadioInteractorCore.getSimCapacity(response);
            waitForResult(request);
        }
        try {
            return (String) request.result;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public void enableRauNotify() {
        ThreadRequest request = new ThreadRequest(null);
        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_INVOKE_ENABLE_RAU_NOTIFY_DONE, request);
            mRadioInteractorCore.enableRauNotify(response);
            waitForResult(request);
        }
    }

    public String iccGetAtr() {
        ThreadRequest request = new ThreadRequest(null);
        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_GET_ATR_DONE, request);
            mRadioInteractorCore.simGetAtr(response);
            waitForResult(request);
        }
        try {
            return (String) request.result;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public boolean queryHdVoiceState() {
        ThreadRequest request = new ThreadRequest(null);
        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_GET_HD_VOICE_STATE_DONE, request);
            mRadioInteractorCore.getHDVoiceState(response);
            waitForResult(request);
        }
        try {
            return (boolean) request.result;
        } catch (ClassCastException e) {
            return false;
        }
    }

    /* UNISOC: Bug#542214 Add support for store SMS to Sim card @{ */
    public boolean storeSmsToSim(boolean enable) {
        ThreadRequest request = new ThreadRequest(null);
        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_REQUEST_STORE_SMS_TO_SIM_DONE, request);
            mRadioInteractorCore.storeSmsToSim(enable, response);
            waitForResult(request);
        }
        try {
            return (boolean) request.result;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public String querySmsStorageMode() {
        ThreadRequest request = new ThreadRequest(null);
        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_QUERY_SMS_STORAGE_MODE_DONE, request);
            mRadioInteractorCore.querySmsStorageMode(response);
            waitForResult(request);
        }
        try {
            return (String) request.result;
        } catch (ClassCastException e) {
            return null;
        }
    }
    /* @} */

    /**
     * Explicit Transfer Call REFACTORING
     *
     * @param result
     */
    public void explicitCallTransfer() {
        mRadioInteractorCore.explicitCallTransfer(mHandler.obtainMessage(EVENT_ECT_RESULT));
    }

    /* add for TV @{*/
    public void dialVP(String address, String sub_address, int clirMode, Message response) {
        mRadioInteractorCore.videoPhoneDial(address, sub_address, clirMode, response);
    }

    public void codecVP(int type, Bundle param, Message response) {
        mRadioInteractorCore.videoPhoneCodec(type, param, response);
    }

    public void fallBackVP(Message response) {
        mRadioInteractorCore.videoPhoneFallback(response);
    }

    public void sendVPString(String str, Message response) {
        mRadioInteractorCore.videoPhoneString(str, response);
    }

    public void controlVPLocalMedia(int datatype, int sw, boolean bReplaceImg, Message response) {
        mRadioInteractorCore.videoPhoneLocalMedia(datatype, sw, bReplaceImg, response);
    }

    public void controlIFrame(boolean isIFrame, boolean needIFrame, Message response) {
        mRadioInteractorCore.videoPhoneControlIFrame(isIFrame, needIFrame, response);
    }

    /* @} */
    /* Add for trafficClass @{ */
    public void requestDCTrafficClass(int type) {
        Message response = mHandler.obtainMessage(EVENT_TRAFFIC_CLASS_DONE);
        mRadioInteractorCore.setTrafficClass(type, response);
    }
    /* @} */

    /* Add for do recovery @{ */
    public void requestReattach() {
        Message response = mHandler.obtainMessage(EVENT_REATTACH_DONE);
        mRadioInteractorCore.reAttach(response);
    }
    /* @} */

    /*UNISOC: bug618350 add single pdp allowed by plmns feature@{*/
    public void requestSetSinglePDNByNetwork(boolean isSinglePDN) {
        Message response = mHandler.obtainMessage(EVENT_SET_SINGLE_PDN_DONE);
        mRadioInteractorCore.setSinglePDN(isSinglePDN, response);
    }

    /* @} */
    /* Add for Data Clear Code from Telcel @{ */
    public void setLteEnabled(boolean enable) {
        Message response = mHandler.obtainMessage(EVENT_SET_LTE_ENABLE_DONE);
        mRadioInteractorCore.enableLTE(enable, response);
    }

    public void attachDataConn(boolean enable) {
        Message response = mHandler.obtainMessage(EVENT_ATTACH_DATA_DONE);
        mRadioInteractorCore.attachData(enable, response);
    }
    /* @} */

    public void forceDetachDataConn(Messenger messenger) {
        Message result = mHandler.obtainMessage(EVENT_DC_FORCE_DETACH_DONE, messenger);
        mRadioInteractorCore.forceDetachDataConn(result);
    }

    public boolean requestShutdown() {
        ThreadRequest request = new ThreadRequest(null);
        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_REQUEST_SHUTDOWN_DONE, request);
            mRadioInteractorCore.requestShutdown(response);
            waitForResult(request);
        }
        try {
            return (boolean) request.result;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public void setSimPower(String pkgname, boolean enabled) {
        enforceSprdModifyPermission(pkgname);
        Message response = mHandler.obtainMessage(EVENT_REQUEST_SET_SIM_POWER);
        mRadioInteractorCore.simmgrSimPower(enabled, response);
    }

    public int setPreferredNetworkType(int networkType) {
        ThreadRequest request = new ThreadRequest(null);
        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_REQUEST_SET_PRE_NETWORK_TYPE, request);
            mRadioInteractorCore.setPreferredNetworkType(networkType, response);
            waitForResult(request);
        }
        try {
            return (Integer) request.result;
        } catch (ClassCastException e) {
            return -1;
        }
    }

    public void updateRealEccList(String realEccList) {
        Message response = mHandler.obtainMessage(EVENT_REQUEST_UPDTAE_REAL_ECCLIST);
        mRadioInteractorCore.updateEcclist(realEccList, response);
    }

    public int queryColp() {
        ThreadRequest request = new ThreadRequest(null);
        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_REQUEST_QUERY_COLP, request);
            mRadioInteractorCore.queryColp(response);
            waitForResult(request);
        }
        try {
            return (Integer) request.result;
        } catch (ClassCastException e) {
            return -1;
        }
    }

    public int queryColr() {
        ThreadRequest request = new ThreadRequest(null);
        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_REQUEST_QUERY_COLR, request);
            mRadioInteractorCore.queryColr(response);
            waitForResult(request);
        }
        try {
            return (Integer) request.result;
        } catch (ClassCastException e) {
            return -1;
        }
    }

    public void updateOperatorName(String plmn) {
        Message response = mHandler.obtainMessage(EVENT_REQUEST_UPDATE_OPERATOR_NAME);
        mRadioInteractorCore.updateOperatorName(plmn, response);
    }

    public int getRealSimStatus() {
        return mCardState == null ? -1 : mCardState.ordinal();
    }

    public void setXcapIPAddress(String ifName, String ipv4Addr, String ipv6Addr, Message response) {
        mRadioInteractorCore.setXcapIPAddress(ifName, ipv4Addr, ipv6Addr, response);
    }

    public int[] getSimlockDummys() {
        ThreadRequest request = new ThreadRequest(null);
        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_GET_SIMLOCK_DUMMYS, request);
            mRadioInteractorCore.getSimlockDummys(response);
            waitForResult(request);
        }
        try {
            return (int[]) request.result;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public String getSimlockWhitelist(int type) {
        ThreadRequest request = new ThreadRequest(null);
        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_GET_SIMLOCK_WHITE_LIST, request);
            mRadioInteractorCore.getSimlockWhitelist(type, response);
            waitForResult(request);
        }
        try {
            return (String) request.result;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public void updateCLIP(int enable, Message result) {
        mRadioInteractorCore.updateCLIP(enable, result);
    }

    public void setTPMRState(int state, Message result) {
        mRadioInteractorCore.setTPMRState(state, result);
    }

    public void getTPMRState(Message result) {
        mRadioInteractorCore.getTPMRState(result);
    }

    public void setVideoResolution(int resolution, Message result) {
        mRadioInteractorCore.setVideoResolution(resolution, result);
    }

    public void enableLocalHold(boolean enable, Message result) {
        mRadioInteractorCore.enableLocalHold(enable, result);
    }

    public void enableWiFiParamReport(boolean enable, Message result) {
        mRadioInteractorCore.enableWiFiParamReport(enable, result);
    }

    public void callMediaChangeRequestTimeOut(int callId, Message result) {
        mRadioInteractorCore.callMediaChangeRequestTimeOut(callId, result);
    }

    public void setLocalTone(int data) {
        Message response = mHandler.obtainMessage(EVENT_REQUEST_SET_LOCAL_TONE);
        mRadioInteractorCore.setLocalTone(data, response);
    }

    public int updatePlmn(int type, int action, String plmn,
                          int act1, int act2, int act3) {
        ThreadRequest request = new ThreadRequest("updatePlmn");
        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_UPDATE_PLMN_DONE,
                    request);

            mRadioInteractorCore.updatePlmn(type, action, plmn, act1, act2, act3, response);
            waitForResult(request);
        }
        try {
            return (int) request.result;
        } catch (ClassCastException e) {
            return -1;
        }
    }

    public String queryPlmn(int type) {
        ThreadRequest request = new ThreadRequest("queryPlmn");

        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_QUERY_PLMN_DONE,
                    request);
            mRadioInteractorCore.queryPlmn(type, response);
            waitForResult(request);
        }
        try {
            return (String) request.result;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public void setSimPowerReal(String pkgname, boolean enabled) {
        enforceSprdModifyPermission(pkgname);
        ThreadRequest request = new ThreadRequest(null);
        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_REQUEST_SET_SIM_POWER_REAL, request);
            mRadioInteractorCore.setSimPowerReal(enabled, response);
            waitForResult(request);
        }
    }

    public void resetModem() {
        Message response = mHandler.obtainMessage(EVENT_REQUEST_RESET_MODEM);
        mRadioInteractorCore.resetModem(response);
    }

    public void enableRadioPowerFallback(boolean enable) {
        Message response = mHandler.obtainMessage(EVENT_REQUEST_ENABLE_RADIO_POWER_FALLBACK);
        mRadioInteractorCore.enableRadioPowerFallback(enable, response);
    }

    public void setPsDataOff(boolean onOff, int value) {
        Message response = mHandler.obtainMessage(EVENT_REQUEST_SET_PS_DATA_OFF);
        mRadioInteractorCore.setPsDataOff(onOff, value, response);
    }

    public void setLocationInfo(String longitude, String latitude, Message result) {
        mRadioInteractorCore.setLocationInfo(longitude, latitude, result);
    }

    public void setEmergencyOnly(boolean emergencyOnly, Message result) {
        mRadioInteractorCore.setEmergencyOnly(emergencyOnly, result);
    }

    public int getSubsidyLockStatus() {
        if (mSubsidyLockState != -1) {
            UtilLog.logd(TAG, "no necessary to get from modem return " + mSubsidyLockState);
            return mSubsidyLockState;
        }
        ThreadRequest request = new ThreadRequest("getSubsidyLockdyStatus");

        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_GET_SUBSIDYLOCK_STATE_DONE,
                    request);
            mRadioInteractorCore.getSubsidyLockdyStatus(response);
            waitForResult(request);
        }

        try {
            int result = ((int[]) request.result)[0];
            UtilLog.logd(TAG, "get SubsidyLock Status from modem " + result);
            mSubsidyLockState = result;
            return result;
        } catch (ClassCastException e) {
            return -1;
        }
    }

    public void setImsUserAgent(String sipUserAgent, Message result) {
        mRadioInteractorCore.setImsUserAgent(sipUserAgent, result);
    }


    public void setFacilityLockByUser(String facility, boolean lockState, Message response) {
        mRadioInteractorCore.setFacilityLockForUser(facility, lockState, response);
    }

    public int getSimLockRemainTimes(int type) {
        ThreadRequest request = new ThreadRequest("getSimLockRemainTimes");

        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_GET_REMIAN_TIMES_DONE,
                    request);

            mRadioInteractorCore.getSimlockRemaintimes(type, response);
            waitForResult(request);
        }

        try {
            return ((int[]) request.result)[0];
        } catch (ClassCastException e) {
            UtilLog.loge(TAG, "getSimLockRemainTimes ClassCastException");
            return -1;
        }
    }

    public int getSimLockStatus(int type) {
        ThreadRequest request = new ThreadRequest("getSimLockStatus");

        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_GET_SIMLOCK_STATUS_DONE,
                    request);

            mRadioInteractorCore.getSimlockStatus(type, response);
            waitForResult(request);
        }

        try {
            return ((int[]) request.result)[0];
        } catch (ClassCastException e) {
            UtilLog.loge(TAG, "getSimLockStatus ClassCastException");
            return -1;
        }
    }

    public String getRadioPreference(String key) {
        ThreadRequest request = new ThreadRequest("getRadioPreference");

        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_GET_RADIO_PREFERENCE_DONE, request);
            mRadioInteractorCore.getRadioPreference(key, response);
            waitForResult(request);
        }

        try {
            return (String) request.result;
        } catch (ClassCastException e) {
            UtilLog.loge(TAG, "getRadioPreference ClassCastException");
            return null;
        }
    }

    public void setRadioPreference(String key, String value) {
        Message response = mHandler.obtainMessage(EVENT_SET_RADIO_PREFERENCE_DONE);
        mRadioInteractorCore.setRadioPreference(key, value, response);
    }

    public void getImsCurrentCalls(Message result) {
        mRadioInteractorCore.getImsCurrentCalls(result);
    }

    public void setImsVoiceCallAvailability(int state, Message result) {
        mRadioInteractorCore.setImsVoiceCallAvailability(state, result);
    }

    public void getImsVoiceCallAvailability(Message result) {
        mRadioInteractorCore.getImsVoiceCallAvailability(result);
    }

    public void initISIM(Message result) {
        mRadioInteractorCore.initISIM(result);
    }

    public void requestVolteCallMediaChange(int action, int callId, Message result) {
        mRadioInteractorCore.requestVolteCallMediaChange(action, callId, result);
    }

    public void responseVolteCallMediaChange(boolean isAccept, int callId, Message result) {
        mRadioInteractorCore.responseVolteCallMediaChange(isAccept, callId, result);
    }

    public void setImsSmscAddress(String smsc, Message result) {
        mRadioInteractorCore.setImsSmscAddress(smsc, result);
    }

    public void requestVolteCallFallBackToVoice(int callId, Message result) {
        mRadioInteractorCore.requestVolteCallFallBackToVoice(callId, result);
    }

    public void setExtInitialAttachApn(DataProfile dataProfileInfo, Message result) {
        mRadioInteractorCore.setExtInitialAttachApn(dataProfileInfo, result);
    }

    public void queryCallForwardStatus(int cfReason, int serviceClass,
                                       String number, String ruleSet, Message result) {
        mRadioInteractorCore.queryCallForwardStatus(cfReason, serviceClass, number, ruleSet, result);
    }

    public void setCallForward(int action, int cfReason, int serviceClass,
                               String number, int timeSeconds, String ruleSet, Message result) {
        mRadioInteractorCore.setCallForward(action, cfReason, serviceClass,
                number, timeSeconds, ruleSet, result);
    }

    public void requestInitialGroupCall(String numbers, Message result) {
        mRadioInteractorCore.requestInitialGroupCall(numbers, result);
    }

    public void requestAddGroupCall(String numbers, Message result) {
        mRadioInteractorCore.requestAddGroupCall(numbers, result);
    }

    public void enableIms(boolean enable, Message result) {
        mRadioInteractorCore.enableIms(enable, result);
    }

    public void getImsBearerState(Message result) {
        mRadioInteractorCore.getImsBearerState(result);
    }

    public void requestImsHandover(int type, Message result) {
        mRadioInteractorCore.requestImsHandover(type, result);
    }

    public void notifyImsHandoverStatus(int status, Message result) {
        mRadioInteractorCore.notifyImsHandoverStatus(status, result);
    }

    public void notifyImsNetworkInfo(int type, String info, Message result) {
        mRadioInteractorCore.notifyImsNetworkInfo(type, info, result);
    }

    public void notifyImsCallEnd(int type, Message result) {
        mRadioInteractorCore.notifyImsCallEnd(type, result);
    }

    public void notifyVoWifiEnable(boolean enable, Message result) {
        mRadioInteractorCore.notifyVoWifiEnable(enable, result);
    }

    public void notifyVoWifiCallStateChanged(boolean incall, Message result) {
        mRadioInteractorCore.notifyVoWifiCallStateChanged(incall, result);
    }

    public void notifyDataRouter(Message result) {
        mRadioInteractorCore.notifyDataRouter(result);
    }

    public void imsHoldSingleCall(int callid, boolean enable, Message result) {
        mRadioInteractorCore.imsHoldSingleCall(callid, enable, result);
    }

    public void imsMuteSingleCall(int callid, boolean enable, Message result) {
        mRadioInteractorCore.imsMuteSingleCall(callid, enable, result);
    }

    public void imsSilenceSingleCall(int callid, boolean enable, Message result) {
        mRadioInteractorCore.imsSilenceSingleCall(callid, enable, result);
    }

    public void imsEnableLocalConference(boolean enable, Message result) {
        mRadioInteractorCore.imsEnableLocalConference(enable, result);
    }

    public void notifyHandoverCallInfo(String callInfo, Message result) {
        mRadioInteractorCore.notifyHandoverCallInfo(callInfo, result);
    }

    public void getSrvccCapbility(Message result) {
        mRadioInteractorCore.getSrvccCapbility(result);
    }

    public void getImsPcscfAddress(Message result) {
        mRadioInteractorCore.getImsPcscfAddress(result);
    }

    public void setImsPcscfAddress(String addr, Message result) {
        mRadioInteractorCore.setImsPcscfAddress(addr, result);
    }

    public void queryFacilityLockForAppExt(String facility, String password, int serviceClass,
                                           Message result) {
        mRadioInteractorCore.queryFacilityLockForAppExt(facility, password, serviceClass, result);
    }

    public void getImsRegAddress(Message result) {
        mRadioInteractorCore.getImsRegAddress(result);
    }

    public void getImsPaniInfo(Message result) {
        mRadioInteractorCore.getImsPaniInfo(result);
    }

    public int getPreferredNetworkType() {
        ThreadRequest request = new ThreadRequest(null);
        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_GET_PREFERRED_NETWORK_TYPE_DONE, request);
            mRadioInteractorCore.getPreferredNetworkType(response);
            waitForResult(request);
        }
        try {
            return (Integer) request.result;
        } catch (ClassCastException e) {
            UtilLog.loge(TAG, "getPreferredNetworkType ClassCastException");
            return -1;
        }
    }

    public int getCnap() {
        ThreadRequest request = new ThreadRequest(null);
        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_GET_CNAP_DONE, request);
            mRadioInteractorCore.getCnap(response);
            waitForResult(request);
        }
        try {
            return (Integer) request.result;
        } catch (ClassCastException e) {
            UtilLog.loge(TAG, "getCnap ClassCastException");
            return -1;
        }
    }

    public int getVoLTEAllowedPLMN() {
        ThreadRequest request = new ThreadRequest("getVoLTEAllowedPLMN");

        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_GET_VOLTE_ALLOWED_PLMN,
                    request);
            mRadioInteractorCore.getVoLTEAllowedPLMN(response);
            waitForResult(request);
        }

        try {
            return (Integer) request.result;
        } catch (ClassCastException e) {
            UtilLog.loge(TAG, "getVoLTEAllowedPLMN ClassCastException");
            return -1;
        }
    }

    public void queryRootNode() {
        Message response = mHandler.obtainMessage(EVENT_REQUEST_QUERY_ROOT_NODE);
        mRadioInteractorCore.queryRootNode(response);
    }

    public void setSmsBearer(int type) {
        Message response = mHandler.obtainMessage(EVENT_SET_SMS_BEARER_DONE);
        mRadioInteractorCore.setSmsBearer(type, response);
    }

    public int getSmsBearer() {
        ThreadRequest request = new ThreadRequest(null);
        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_GET_SMS_BEARER_DONE, request);
            mRadioInteractorCore.getSmsBearer(response);
            waitForResult(request);
        }
        try {
            return (Integer) request.result;
        } catch (ClassCastException e) {
            return 0;
        }
    }

    public LteSpeedAndSignalStrengthInfo getLteSpeedAndSignalStrength() {
        ThreadRequest request = new ThreadRequest("getLteSpeedAndSignalStrength");

        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_GET_LTE_SPEED_AND_SIGNAL_STRENGTH,
                    request);
            mRadioInteractorCore.getLteSpeedAndSignalStrength(response);
            waitForResult(request);
        }
        try {
            return (LteSpeedAndSignalStrengthInfo) request.result;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public void enableNrSwitch(int mode, int enable) {
        Message response = mHandler.obtainMessage(EVENT_REQUEST_ENABLE_NR_SWITCH);
        mRadioInteractorCore.enableNrSwitch(mode, enable, response);
    }

    public void setUsbShareStateSwitch(boolean enable) {
        Message response = mHandler.obtainMessage(EVENT_REQUEST_SET_USB_SHARE_STATE_SWITCH);
        mRadioInteractorCore.setUsbShareStateSwitch(enable, response);
    }

    public int setStandAlone(int type) {
        ThreadRequest request = new ThreadRequest(null);
        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_SET_STAND_ALONE_DONE, request);
            mRadioInteractorCore.setStandAlone(type, response);
            waitForResult(request);
        }
        try {
            return (Integer) request.result;
        } catch (ClassCastException e) {
            return -1;
        }
    }

    public int getStandAlone() {
        ThreadRequest request = new ThreadRequest(null);
        synchronized (request) {
            Message response = mHandler.obtainMessage(EVENT_GET_STAND_ALONE_DONE, request);
            mRadioInteractorCore.getStandAlone(response);
            waitForResult(request);
        }
        try {
            return (Integer) request.result;
        } catch (ClassCastException e) {
            return -1;
        }
    }
    // --- request end.
}
