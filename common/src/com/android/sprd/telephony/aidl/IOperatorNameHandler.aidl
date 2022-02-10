package com.android.sprd.telephony.aidl;

interface IOperatorNameHandler {

    /**
     * Update PLMN network name,try to get operator name from SIM if ONS exists or regplmn matched
     * OPL/PNN PLMN showed priorities: OPL/PNN > ONS(CPHS) > NITZ > numeric_operator.xml > mcc+mnc
     * See 3GPP TS 22.101 for details.
     *
     * @param phoneId of whose high priority PLMN network name is returned
     * @param mccmnc mobile country code and mobile network code
     */
    String getHighPriorityPlmn(int phoneId, String mccmnc, int lac);
    /**
     * Returns high priority operator name if exists according to the giving operator info.
     *
     * @param phoneId of whose high priority PLMN network name is returned
     * @param operatorInfo operator info from network including MCC/MNC and network operator name.
     */
    String updateNetworkList(int phoneId, in String[] operatorInfo);

}

