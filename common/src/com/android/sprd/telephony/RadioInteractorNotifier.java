
package com.android.sprd.telephony;

import java.util.ArrayList;

import com.android.sprd.telephony.RadioInteractorCore.SuppService;
import com.android.sprd.telephony.aidl.IRadioInteractorCallback;

import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

public class RadioInteractorNotifier {
    public static final String TAG = "RadioInteractorNotifier";

    private final ArrayList<Record> mRecords = new ArrayList<Record>();
    private final ArrayList<IBinder> mRemoveList = new ArrayList<IBinder>();
    private int mNumPhones;

    public RadioInteractorNotifier() {
        int numPhones = TelephonyManager.getDefault().getPhoneCount();
        UtilLog.logd(TAG, " numPhones=" + numPhones);
        mNumPhones = numPhones;

    }

    public void notifyRadiointeractorEventForSubscriber(int slotId) {
        UtilLog.logd(TAG, "notifyRadiointeractorEventForSubscriber slotId=" + slotId);
        synchronized (mRecords) {
            for (Record r : mRecords) {
                UtilLog.logd(TAG, "notifyRadiointeractorEventForSubscriber:  r=" + r + " slotId="
                        + slotId);
                if ((r.matchPhoneStateListenerEvent(
                        RadioInteractorCallbackListener.LISTEN_RADIOINTERACTOR_EVENT)) &&
                        ((r.phoneId == slotId))) {
                    try {
                        r.callback.onRadiointeractorEvent();
                    } catch (RemoteException ex) {
                        mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyRadiointeractorEventForEmbms(int slotId) {
        UtilLog.logd(TAG, "notifyRadiointeractorEventForEmbms slotId=" + slotId);
        synchronized (mRecords) {
            for (Record r : mRecords) {
                UtilLog.logd(TAG, "notifyRadiointeractorEventForEmbms:  r=" + r + " slotId="
                        + slotId);
                if ((r.matchPhoneStateListenerEvent(
                        RadioInteractorCallbackListener.LISTEN_RADIOINTERACTOR_EMBMS_EVENT)) &&
                        ((r.phoneId == slotId))) {
                    try {
                        r.callback.onRadiointeractorEmbmsEvent();
                    } catch (RemoteException ex) {
                        mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifySuppServiceFailed(int slotId, SuppService code) {
        UtilLog.logd(TAG, "notifySuppServiceFailed slotId = " + slotId);
        synchronized (mRecords) {
            for (Record r : mRecords) {
                UtilLog.logd(TAG, "notifySuppServiceFailed:  r = " + r + " slotId = "
                        + slotId);
                if ((r.matchPhoneStateListenerEvent(
                        RadioInteractorCallbackListener.LISTEN_SUPP_SERVICE_FAILED_EVENT)) &&
                        ((r.phoneId == slotId))) {
                    try {
                        r.callback.onSuppServiceFailedEvent(code.ordinal());
                    } catch (RemoteException ex) {
                        mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyRealSimStateChanged(int slotId) {
        UtilLog.logd(TAG, "notifyRealSimStateChanged slotId=" + slotId);
        synchronized (mRecords) {
            for (Record r : mRecords) {
                UtilLog.logd(TAG, "notifyRealSimStateChanged:  r=" + r + " slotId="
                        + slotId);
                if ((r.matchPhoneStateListenerEvent(
                        RadioInteractorCallbackListener.LISTEN_SIMMGR_SIM_STATUS_CHANGED_EVENT)) &&
                        ((r.phoneId == slotId))) {
                    try {
                        r.callback.onRealSimStateChangedEvent();
                    } catch (RemoteException ex) {
                        mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyExpireSimEvent(int info, int slotId) {
        UtilLog.logd(TAG, "notifyExpiredSimEvent slotId=" + slotId);
        synchronized (mRecords) {
            for (Record r : mRecords) {
                UtilLog.logd(TAG, "notifyExpiredSimEvent:  r=" + r + " slotId="
                        + slotId);
                if ((r.matchPhoneStateListenerEvent(
                        RadioInteractorCallbackListener.LISTEN_EXPIRE_SIM_EVENT)) &&
                        ((r.phoneId == slotId))) {
                    try {
                        r.callback.onExpireSimEvent(info);
                    } catch (RemoteException ex) {
                        mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyRadiointeractorEventForEarlyMedia(int em, int slotId) {
        UtilLog.logd(TAG, "notifyRadiointeractorEventForEarlyMedia slotId=" + slotId + " em = " + em);
        synchronized (mRecords) {
            for (Record r : mRecords) {
                UtilLog.logd(TAG, "notifyRadiointeractorEventForEarlyMedia:  r=" + r + " slotId="
                        + slotId);
                if ((r.matchPhoneStateListenerEvent(
                        RadioInteractorCallbackListener.LISTEN_EARLY_MEDIA_EVENT)) &&
                        ((r.phoneId == slotId))) {
                    try {
                        r.callback.onEarlyMediaEvent(em);
                    } catch (RemoteException ex) {
                        mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyRadiointeractorHdStatusInfo(int info, int slotId) {
        UtilLog.logd(TAG, "notifyRadiointeractorHdStatusInfo slotId=" + slotId);
        synchronized (mRecords) {
            for (Record r : mRecords) {
                UtilLog.logd(TAG, "notifyRadiointeractorHdStatusInfo:  r=" + r + " slotId="
                        + slotId);
                if ((r.matchPhoneStateListenerEvent(
                        RadioInteractorCallbackListener.LISTEN_HD_STATUS_CHANGED_EVENT)) &&
                        ((r.phoneId == slotId))) {
                    try {
                        r.callback.onHdStatusChangedEvent(info);
                    } catch (RemoteException ex) {
                        mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifySubsidyLockEvent(int subsidy, int slotId) {
        UtilLog.logd(TAG, "notifySubsidyLockEvent slotId=" + slotId);
        synchronized (mRecords) {
            for (Record r : mRecords) {
                UtilLog.logd(TAG, "notifySubsidyLockEvent:  r=" + r + " slotId="
                        + slotId);
                if ((r.matchPhoneStateListenerEvent(
                        RadioInteractorCallbackListener.LISTEN_SUBSIDYLOCK_EVENT)) &&
                        ((r.phoneId == slotId))) {
                    try {
                        UtilLog.logd(TAG, "notifySubsidyLockEvent:  r=" + r + " slotId="
                                + slotId + "subsidy = " + subsidy);
                        r.callback.onSubsidyLockEvent(subsidy);
                    } catch (RemoteException ex) {
                        mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyRadiointeractorImsCsfbVendorCauseInfo(String causeCode, int slotId) {
        UtilLog.logd(TAG, "notifyRadiointeractorImsCsfbVendorCauseInfo slotId = " + slotId);
        synchronized (mRecords) {
            for (Record r : mRecords) {
                UtilLog.logd(TAG, "notifyRadiointeractorImsCsfbVendorCauseInfo:  r = " + r);
                if ((r.matchPhoneStateListenerEvent(
                        RadioInteractorCallbackListener.LISTEN_IMS_CSFB_VENDOR_CAUSE_EVENT)) &&
                        ((r.phoneId == slotId))) {
                    try {
                        r.callback.onImsCsfbVendorCauseEvent(causeCode);
                    } catch (RemoteException ex) {
                        mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyRadiointeractorCnap(String name, int slotId) {
        UtilLog.logd(TAG, "notifyRadiointeractorCnap slotId = " + slotId);
        synchronized (mRecords) {
            for (Record r : mRecords) {
                UtilLog.logd(TAG, "notifyRadiointeractorCnap:  r = " + r);
                if ((r.matchPhoneStateListenerEvent(
                        RadioInteractorCallbackListener.LISTEN_CNAP_EVENT)) &&
                        ((r.phoneId == slotId))) {
                    try {
                        r.callback.onCnapEvent(name);
                    } catch (RemoteException ex) {
                        mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyModemStateChanegd(int data, String assertInfo, int phoneId) {
        UtilLog.logd(TAG, "notifyModemStateChanegd phoneId = " + phoneId);

        synchronized (mRecords) {
            for (Record r : mRecords) {
                UtilLog.logd(TAG, "notifyModemStateChanegd:  r = " + r);
                if ((r.matchPhoneStateListenerEvent(
                        RadioInteractorCallbackListener.LISTEN_MODEM_STATE_CHANGED_EVENT)) &&
                        ((r.phoneId == phoneId))) {
                    try {
                        r.callback.onModemStateChangedEvent(data, assertInfo);
                    } catch (RemoteException ex) {
                        mRemoveList.add(r.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    private void handleRemoveListLocked() {
        int size = mRemoveList.size();
        UtilLog.logd(TAG, "handleRemoveListLocked: mRemoveList.size()=" + size);
        if (size > 0) {
            for (IBinder b : mRemoveList) {
                remove(b);
            }
            mRemoveList.clear();
        }
    }

    public void listenForSlot(int slotId, IRadioInteractorCallback callback, int events,
                              boolean notifyNow) {
        if (events != RadioInteractorCallbackListener.LISTEN_NONE) {
            synchronized (mRecords) {
                // register
                Record r;
                find_and_add:
                {
                    IBinder b = callback.asBinder();
                    final int N = mRecords.size();
                    for (int i = 0; i < N; i++) {
                        r = mRecords.get(i);
                        if (b == r.binder && events == r.events) {
                            break find_and_add;
                        }
                    }
                    r = new Record();
                    r.binder = b;
                    mRecords.add(r);
                    UtilLog.logd(TAG, "listen: add new record");
                }

                r.callback = callback;
                r.events = events;
                r.phoneId = slotId;

                if (notifyNow && validatePhoneId(slotId)) {
                    if ((events & RadioInteractorCallbackListener.LISTEN_RADIOINTERACTOR_EVENT) != 0) {
                        try {
                            r.callback.onRadiointeractorEvent();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    if ((events & RadioInteractorCallbackListener.LISTEN_RADIOINTERACTOR_EMBMS_EVENT) != 0) {
                        try {
                            r.callback.onRadiointeractorEmbmsEvent();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        } else {
            UtilLog.logd(TAG, "listen: Unregister");
            remove(callback.asBinder());
        }

    }

    private boolean validatePhoneId(int phoneId) {
        boolean valid = (phoneId >= 0) && (phoneId < mNumPhones);
        UtilLog.logd(TAG, "validatePhoneId: " + valid);
        return valid;
    }

    private static class Record {
        // String callingPackage;

        IBinder binder;

        IRadioInteractorCallback callback;

        // int callerUserId;

        int events;

        // int subId = SubscriptionManager.INVALID_SUBSCRIPTION_ID;

        int phoneId = SubscriptionManager.INVALID_PHONE_INDEX;

        // boolean canReadPhoneState;

        boolean matchPhoneStateListenerEvent(int events) {
            return (callback != null) && ((events & this.events) != 0);
        }
    }

    private void remove(IBinder binder) {
        synchronized (mRecords) {
            final int recordCount = mRecords.size();
            for (int i = 0; i < recordCount; i++) {
                if (mRecords.get(i).binder == binder) {
                    Record r = mRecords.get(i);
                    UtilLog.logd(TAG, "remove: binder=" + binder
                            + "r.callback" + r.callback);

                    mRecords.remove(i);
                    remove(binder);
                    return;
                }
            }
        }
    }
}
