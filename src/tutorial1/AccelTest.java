package tutorial1;

import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.WavePlayer;
import net.happybrackets.core.HBAction;
import net.happybrackets.device.HB;
import net.happybrackets.device.sensors.AccelerometerListener;

import java.lang.invoke.MethodHandles;

public class AccelTest implements HBAction {
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        float frq = 200;
        Glide glide = new Glide(frq, 100);
        WavePlayer wp = new WavePlayer(frq, Buffer.SINE);
        Gain g= new Gain(1, 0.4f);
        g.addInput(wp);
        hb.sound(g);

        // type accelerometerSensor to create this. Values typically range from -1 to + 1
        new AccelerometerListener(hb) {
            @Override
            public void sensorUpdated(float x_val, float y_val, float z_val) { // Write your code below this line

                hb.setStatus("X: "+x_val+" Y: "+y_val+" Z: "+z_val);
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
