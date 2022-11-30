package tutorial4.session4;

import net.happybrackets.core.HBAction;
import net.happybrackets.device.HB;
import net.happybrackets.device.sensors.AccelerometerListener;

import java.lang.invoke.MethodHandles;

public class detectOrientation implements HBAction {

    // Setup all the directions
    enum direction { LEFT, RIGHT, FRONT, BACK, UP, DOWN};

    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below


        // setup an array
        float[] acc_vals = new float[6];
        // type accelerometerSensor to create this. Values typically range from -1 to + 1
        new AccelerometerListener(hb) {
            @Override
            public void sensorUpdated(float x_val, float y_val, float z_val) { // Write your code below this line
                direction orientation = direction.BACK;

                float max_val = 0;
                acc_vals[0] = x_val;
                acc_vals[1] = x_val * -1;
                acc_vals[2] = y_val;
                acc_vals[3] = y_val * -1;
                acc_vals[4] = z_val;
                acc_vals[5] = z_val * -1;


                for (int i =0; i < 6; i++){
                    if (acc_vals[i] > max_val){

                       orientation = direction.values()[i];
                       max_val = acc_vals[i];

                    }

                }


                hb.setStatus("Side: " + orientation + " x:" + acc_vals[4]);
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
