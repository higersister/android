package cn.cmy.view.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.cmy.view.Car;
import cn.cmy.view.ICarManager;

public class CarService extends Service {

    private List<Car> mList = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return manager;
    }

    private final ICarManager.Stub manager = new ICarManager.Stub() {
        @Override
        public  List<Car> getAll() throws RemoteException {
            return mList;
        }

        @Override
        public  Car addCarIn(Car car) throws RemoteException {
            Log.i("$$$$$$$$$$", "invoking addCarIn method : " + car.toString());
            car.price = 12200;
            mList.add(car);
            return car;
        }

        @Override
        public  Car addCarOut(Car car) throws RemoteException {
            Log.i("$$$$$$$$$$", "invoking addCarOut method : " + car.toString());
            car.price = 12201;
            car.name = "out";
            mList.add(car);
            return car;
        }

        @Override
        public  Car addCarInOut(Car car) throws RemoteException {
            Log.i("$$$$$$$$$$", "invoking addCarInOut method : " + car.toString());
            car.price = 12202;
            mList.add(car);
            return car;
        }
    };


}
