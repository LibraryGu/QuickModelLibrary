package com.icrane.quickmode.model;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("ALL")
public abstract class ParcelableModel implements Parcelable {

    private static ModelCreator MODELCREATOR;

    public static final Creator<ParcelableModel> CREATOR = new Creator<ParcelableModel>() {

        @Override
        public ParcelableModel createFromParcel(Parcel source) {
            return MODELCREATOR.constructor(source);
        }

        @Override
        public ParcelableModel[] newArray(int size) {
            return MODELCREATOR.newArray(size);
        }
    };

	public ParcelableModel(Parcel source) {
	}

    public void setModelCreator(ModelCreator creator) {
        MODELCREATOR = creator;
    }

    public ModelCreator getModelCreator() {
        return MODELCREATOR;
    }

    public interface ModelCreator {
        public ParcelableModel constructor(Parcel source);
        public ParcelableModel[] newArray(int size);
    }

}
