package com.android.sprd.telephony.linkturbo;

import android.annotation.NonNull;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * A Parcelable class for LteSpeed and SignalStrength Information.
 */
public class LteSpeedAndSignalStrengthInfo implements Parcelable {
    private int mTxSpeed;
    private int mRxSpeed;
    private int mSnr;
    private int mRsrp;

    public LteSpeedAndSignalStrengthInfo() {
    }

    public LteSpeedAndSignalStrengthInfo(int txSpeed, int rxSpeed, int snr, int rsrp) {
        this.mTxSpeed = txSpeed;
        this.mRxSpeed = rxSpeed;
        this.mSnr = snr;
        this.mRsrp = rsrp;
    }

    public int getTxSpeed() {
        return mTxSpeed;
    }

    public int getRxSpeed() {
        return mRxSpeed;
    }

    public int getSnr() {
        return mSnr;
    }

    public int getRsrp() {
        return mRsrp;
    }

    public void setTxSpeed(int txSpeed) {
        this.mTxSpeed = txSpeed;
    }

    public void setRxSpeed(int rxSpeed) {
        this.mRxSpeed = rxSpeed;
    }

    public void setSnr(int snr) {
        this.mSnr = snr;
    }

    public void setRsrp(int rsrp) {
        this.mRsrp = rsrp;
    }

    private LteSpeedAndSignalStrengthInfo(Parcel in) {
        mTxSpeed = in.readInt();
        mRxSpeed = in.readInt();
        mSnr = in.readInt();
        mRsrp = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mTxSpeed);
        dest.writeInt(mRxSpeed);
        dest.writeInt(mSnr);
        dest.writeInt(mRsrp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final @NonNull
    Creator<LteSpeedAndSignalStrengthInfo> CREATOR = new Creator<LteSpeedAndSignalStrengthInfo>() {
        @Override
        public LteSpeedAndSignalStrengthInfo createFromParcel(Parcel in) {
            return new LteSpeedAndSignalStrengthInfo(in);
        }

        @Override
        public LteSpeedAndSignalStrengthInfo[] newArray(int size) {
            return new LteSpeedAndSignalStrengthInfo[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        LteSpeedAndSignalStrengthInfo that = (LteSpeedAndSignalStrengthInfo) obj;
        return ((mTxSpeed == that.mTxSpeed)
                && (mRxSpeed == that.mRxSpeed)
                && (mSnr == that.mSnr)
                && (mRsrp == that.mRsrp));
    }

    @Override
    public int hashCode() {
        return Objects.hash(mTxSpeed, mRxSpeed, mSnr, mRsrp);
    }

    @Override
    public String toString() {
        return "LteSpeedAndSignalStrengthInfo (mTxSpeed="
                + mTxSpeed
                + ", mRxSpeed="
                + mRxSpeed
                + ", mSnr="
                + mSnr
                + ", mRsrp="
                + mRsrp
                + ")";
    }
}