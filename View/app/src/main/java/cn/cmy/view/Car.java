package cn.cmy.view;

import android.os.Parcel;
import android.os.Parcelable;

public class Car implements Parcelable {

    public String name;
    public double price;

    public Car(){}

    protected Car(Parcel in) {
        name = in.readString();
        price = in.readDouble();
    }

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(price);
    }
    public void readFromParcel(Parcel dest){
        name = dest.readString();
        price = dest.readDouble();
    }

    @Override
    public String toString() {
        return "{name:" + name + ",price:" + price + "}";
    }
}
