package com.kobe.library.entity;

import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by kobe.gong on 2015/6/17.
 */
public class UpdateInfo implements Parcelable {
    public int newVersion;
    public String newMessage;
    public int required;
    public String url;
    public String versionName;
    public String title;

    public UpdateInfo(JSONObject response) {
        newVersion = response.optInt("build");
        versionName = response.optString("version");
        newMessage = response.optString("message");
        title = response.optString("title");
        required = response.optInt("required");
        url = response.optString("url");
    }

    public static String makeFileName(int version) {
        return "hyc" + version + ".apk";
    }

    public String apkName() {
        return makeFileName(newVersion);
    }

    public File apkFile() {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), apkName());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.newVersion);
        dest.writeString(this.newMessage);
        dest.writeInt(this.required);
        dest.writeString(this.url);
        dest.writeString(this.versionName);
        dest.writeString(this.title);
    }

    private UpdateInfo(Parcel in) {
        this.newVersion = in.readInt();
        this.newMessage = in.readString();
        this.required = in.readInt();
        this.url = in.readString();
        this.versionName = in.readString();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<UpdateInfo> CREATOR = new Parcelable.Creator<UpdateInfo>() {
        public UpdateInfo createFromParcel(Parcel source) {
            return new UpdateInfo(source);
        }

        public UpdateInfo[] newArray(int size) {
            return new UpdateInfo[size];
        }
    };
}
