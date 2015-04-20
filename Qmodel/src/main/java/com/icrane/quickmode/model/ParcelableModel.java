package com.icrane.quickmode.model;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("ALL")
public abstract class ParcelableModel implements Parcelable {

    public static ModelCreator modelCreator;

    public static final Creator<ParcelableModel> CREATOR = new Creator<ParcelableModel>() {

        @Override
        public ParcelableModel createFromParcel(Parcel source) {
            return modelCreator.constructor(source);
        }

        @Override
        public ParcelableModel[] newArray(int size) {
            return modelCreator.newArray(size);
        }
    };

	public ParcelableModel(Parcel source) {
	}

    public interface ModelCreator {
        public ParcelableModel constructor(Parcel source);
        public ParcelableModel[] newArray(int size);
    }

}
