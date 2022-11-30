package tutorial4.session1;

import net.happybrackets.core.HBAction;
import net.happybrackets.device.HB;
import net.happybrackets.device.sensors.AccelerometerListener;

import java.lang.invoke.MethodHandles;

public class gravityAccelerometer implements HBAction {
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        // type accelerometerSensor to create this. Values typically range from -1 to + 1
        new AccelerometerListener(hb) {
            @Override
            public void sensorUpdated(float x_val, float y_val, float z_val) { // Write your code below this line

                hb.setStatus("x: "   + String.format("%.1g",x_val) +
                             ", y: " + String.format("%.1g",y_val) +
                             ", z: " + String.format("%.1g",z_val));

                // Write your code above this line
            }
        };//  End accelerometerSensor

        // write your code above this line
    }


    //<editor-fold defaultstate="collapsed" desc="Debug Start">

    /**
     * This function is used when running sketch in IntelliJ IDE for debugging or testing
     *
     * @param args standard args required
     */
    public static void main(String[] args) {

        try {
            HB.runDebug(MethodHandles.lookup().lookupClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //</editor-fold>
}
