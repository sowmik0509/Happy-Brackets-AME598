package tutorial4.session4;

import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.WavePlayer;
import net.happybrackets.core.HBAction;
import net.happybrackets.device.HB;
import net.happybrackets.device.sensors.AccelerometerListener;
import net.happybrackets.device.sensors.GyroscopeListener;

import java.lang.invoke.MethodHandles;

public class gyroscopeToFrequencySimple implements HBAction {
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        // Set up the synthesis
        Glide freq = new Glide(0, 20);
        WavePlayer wp = new WavePlayer(freq, Buffer.SINE);
        Gain gainAmplifier = new Gain(HB.getNumOutChannels(),0.2f);
        gainAmplifier.addInput(wp);
        gainAmplifier.connectTo(HB.getAudioOutput());


        // type gyroscopeSensor to create this. Values typically range from -1 to + 1
        new GyroscopeListener(hb) {


            float x_val_MAX = 0;
            float x_val_MIN = 0;
            float x_RANGE= 0;
            float out_RANGE = 1000;



            @Override
            public void sensorUpdated(float pitch, float roll, float yaw) { // Write your code below this line

                if (pitch > x_val_MAX){
                    x_val_MAX = pitch;
                }

                if (pitch < x_val_MIN){
                    x_val_MIN = pitch;
                }

                x_RANGE = x_val_MAX - x_val_MIN;

                hb.setStatus("in: " + String.format("%.1g", pitch) +
                        ", nrml: " + String.format("%.1g", (pitch - x_val_MIN) / x_RANGE)+
                        ", mlt: " + String.format("%.1g", (pitch - x_val_MIN) / x_RANGE * out_RANGE) +
                        ", out: " + String.format("%.1g", (pitch - x_val_MIN) / x_RANGE * out_RANGE)
                );

                float out = (pitch - x_val_MIN) / x_RANGE * out_RANGE;

                freq.setValue(out);

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
