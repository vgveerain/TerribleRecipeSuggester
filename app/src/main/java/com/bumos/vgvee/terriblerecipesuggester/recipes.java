package com.bumos.vgvee.terriblerecipesuggester;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

public class recipes implements Parcelable{
    String title;
    String source_url;
    String publisher;
    String image_url;
    long id;

    public recipes(String title, String source_url, String publisher, String image_url) {
        this.title = title;
        this.source_url = source_url;
        this.publisher = publisher;
        this.image_url = image_url;
        this.id = System.currentTimeMillis();
    }

    public recipes() {
    }

    protected recipes(Parcel in) {
        title = in.readString();
        source_url = in.readString();
        publisher = in.readString();
        image_url = in.readString();
        id = in.readLong();
    }

    public static final Creator<recipes> CREATOR = new Creator<recipes>() {
        @Override
        public recipes createFromParcel(Parcel in) {
            return new recipes(in);
        }

        @Override
        public recipes[] newArray(int size) {
            return new recipes[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(source_url);
        dest.writeString(publisher);
        dest.writeString(image_url);
        dest.writeLong(id);
    }
}
