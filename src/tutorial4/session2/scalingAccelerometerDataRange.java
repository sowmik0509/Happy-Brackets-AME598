package tutorial4.session2;

import net.happybrackets.core.HBAction;
import net.happybrackets.device.HB;
import net.happybrackets.device.sensors.AccelerometerListener;

import java.lang.invoke.MethodHandles;

public class scalingAccelerometerDataRange implements HBAction {
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        // type accelerometerSensor to create this. Values typically range from -1 to + 1
        new AccelerometerListener(hb) {


            float x_val_MAX = 0;
            float x_val_MIN = 0;
            float x_RANGE = 0;

            @Override
            public void sensorUpdated(float x_val, float y_val, float z_val) { // Write your code below this line

                if (x_val > x_val_MAX){
                    x_val_MAX = x_val;
                }

                if (x_val < x_val_MIN){
                    x_val_MIN = x_val;
                }

                x_RANGE = x_val_MAX - x_val_MIN;

                hb.setStatus("in: " + String.format("%.1g", x_val) +
                        ", rng: " + String.format("%.1g", x_RANGE) +
                        ", ofst: " + String.format("%.1g", x_val_MIN) +
                        ", nrml: " + String.format("%.1g", (x_val - x_val_MIN) / x_RANGE)

                );


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
