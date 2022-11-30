package tutorial4.session4;

import net.happybrackets.core.HBAction;
import net.happybrackets.device.HB;
import net.happybrackets.device.sensors.AccelerometerListener;
import net.happybrackets.device.sensors.GyroscopeListener;

import java.lang.invoke.MethodHandles;

public class accelerometerCartesianToPolar implements HBAction {
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        // type accelerometerSensor to create this. Values typically range from -1 to + 1
        new AccelerometerListener(hb) {
            @Override
            public void sensorUpdated(float x_val, float y_val, float z_val) { // Write your code below this line

                double ax = x_val;
                double ay = y_val;
                double az = z_val;

                double xAngle = Math.atan( ax / (Math.sqrt(Math.pow(ay, 2) + Math.pow(az, 2))));
                double yAngle = Math.atan( ay / (Math.sqrt(Math.pow(ax, 2) + Math.pow(az, 2))));
                double zAngle = Math.atan( Math.sqrt(Math.pow(ax, 2) + Math.pow(ay, 2)) / az);

                xAngle *= 180.00;
                yAngle *= 180.00;
                zAngle *= 180.00;

                xAngle /= Math.PI;
                yAngle /= Math.PI;
                zAngle /= Math.PI;

                hb.setStatus("x: " + String.format("%.2f", xAngle) + ", y: " + String.format("%.2f",yAngle) + ", z: " + String.format("%.2f",zAngle));


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
