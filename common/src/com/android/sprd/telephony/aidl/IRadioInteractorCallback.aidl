package com.android.sprd.telephony.aidl;

interface IRadioInteractorCallback
{
    void onRadiointeractorEvent();
    void onRadiointeractorEmbmsEvent();
    void onSuppServiceFailedEvent(int service);
    void onRealSimStateChangedEvent();
    void onExpireSimEvent(int info);
    void onEarlyMediaEvent(int earlyMedia);
    void onHdStatusChangedEvent(in int info);
    void onSubsidyLockEvent(int subsidyLock);
    void onImsCsfbVendorCauseEvent(String causeCode);
    void onCnapEvent(String name);
    void onModemStateChangedEvent(int status, String assertInfo);
}
