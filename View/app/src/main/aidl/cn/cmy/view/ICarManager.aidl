// ICarManager.aidl
package cn.cmy.view;

// Declare any non-default types here with import statements
import cn.cmy.view.Car;

interface ICarManager {

    List<Car> getAll();

   Car addCarIn(in Car car);

   Car addCarOut(out Car car);

   Car addCarInOut(inout Car car);

}
