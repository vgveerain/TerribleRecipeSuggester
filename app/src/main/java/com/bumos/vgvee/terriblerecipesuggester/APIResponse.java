package com.bumos.vgvee.terriblerecipesuggester;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class APIResponse implements Parcelable{
    int count;
    ArrayList<recipes> recipes;

    public APIResponse(int count, ArrayList<com.bumos.vgvee.terriblerecipesuggester.recipes> recipes) {
        this.count = count;
        this.recipes = recipes;
    }

    public APIResponse() {
    }

    protected APIResponse(Parcel in) {
        count = in.readInt();
        recipes = in.createTypedArrayList(com.bumos.vgvee.terriblerecipesuggester.recipes.CREATOR);
    }

    public static final Creator<APIResponse> CREATOR = new Creator<APIResponse>() {
        @Override
        public APIResponse createFromParcel(Parcel in) {
            return new APIResponse(in);
        }

        @Override
        public APIResponse[] newArray(int size) {
            return new APIResponse[size];
        }
    };

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<com.bumos.vgvee.terriblerecipesuggester.recipes> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<com.bumos.vgvee.terriblerecipesuggester.recipes> recipes) {
        this.recipes = recipes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeTypedList(recipes);
    }
}
