package tutorial4.session3;

import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Function;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.WavePlayer;
import net.happybrackets.core.HBAction;
import net.happybrackets.core.instruments.WaveModule;
import net.happybrackets.device.HB;
import net.happybrackets.device.sensors.AccelerometerListener;

import java.lang.invoke.MethodHandles;

public class accelerometerToFM implements HBAction {
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        // Here we add the code for the bsicFM synthesis. We
        // will use the three parameters and control them from the accelerometer

        final float INITIAL_VOLUME = 0.1f; // define how loud we want the sound
        Glide audioVolume = new Glide(INITIAL_VOLUME);

        final float CARRIER_FREQUENCY = 1000; //This is the centre frequency
        final float MODULATOR_DEPTH_FREQUENCY = 500;    // THis is how much we will change the centre frequency
        final float MODULATOR_RATE_FREQUENCY = 1;  // This is the rate at which we will change the frequency

        // We need to create UGen objects so we can use them inside a function
        //these are the parameters that will be used to calculate the frequency at any point in time
        Glide modulatorFrequency = new Glide(MODULATOR_RATE_FREQUENCY, 50);
        Glide modulatorDepthFrequency = new Glide(MODULATOR_DEPTH_FREQUENCY, 50);
        Glide carrierFrequency = new Glide(CARRIER_FREQUENCY, 50);

        // We need to create a sine wave that will change the modulatorDepth
        WavePlayer FM_modulator = new WavePlayer(modulatorFrequency, Buffer.SINE);

        // We need to create a function to define what the frequency of the carrier wavePlayer will be
        // As the value of the FM_modulator changes from -1, through 0, and then to 1 and then back
        // The output of this function will give a value ranging from 500 to 1500
        Function modFunction = new Function(FM_modulator, modulatorDepthFrequency, carrierFrequency) {
            @Override
            public float calculate() {
                return
                        // this is first argument of the function, the FM_modulator.
                        x[0] // This is swinging from -1 to + 1
                        * x[1] // this is the second argument, which is our depth x[0] * x[1] gives a value of -500 to +500
                        + x[2]; // this is our third argument, carrierFrequency. Adding x[0] * x[1] to this value will give 500 to 1500
            }
        };

        // Now make our player using the modFunction as the frequency
        WaveModule player = new WaveModule(modFunction, INITIAL_VOLUME, Buffer.SINE);
        player.connectTo(HB.getAudioOutput());


        // type accelerometerSensor to create this. Values typically range from -1 to + 1
        new AccelerometerListener(hb) {


            float x_val_MAX = 0, y_val_MAX = 0, z_val_MAX = 0;
            float x_val_MIN = 0, y_val_MIN = 0, z_val_MIN = 0;
            float x_RANGE = 0, y_RANGE = 0, z_RANGE = 0;

            float carrier_MIN = 220, carrier_MAX = 880;
            float carrier_RANGE = carrier_MAX - carrier_MIN;
            float carrier_OFFSET = carrier_MIN;

            float modulator_MIN = 1, modulator_MAX = 4;
            float modulator_RANGE = modulator_MAX - modulator_MIN;
            float modulator_OFFSET = modulator_MIN;

            float modulatorDepth_MIN = 0, modulatorDepth_MAX = 500;
            float modulatorDepth_RANGE = modulatorDepth_MAX - modulatorDepth_MIN;
            float modulatorDepth_OFFSET = modulatorDepth_MIN;


            @Override
            public void sensorUpdated(float x_val, float y_val, float z_val) { // Write your code below this line

                if (x_val > x_val_MAX){ x_val_MAX = x_val; }
                if (x_val < x_val_MIN){ x_val_MIN = x_val; }
                x_RANGE = x_val_MAX - x_val_MIN;

                if (y_val > y_val_MAX){ y_val_MAX = y_val; }
                if (y_val < y_val_MIN){ y_val_MIN = y_val; }
                y_RANGE = y_val_MAX - y_val_MIN;

                if (z_val > z_val_MAX){ z_val_MAX = z_val; }
                if (z_val < z_val_MIN){ z_val_MIN = z_val; }
                z_RANGE = z_val_MAX - z_val_MIN;

                float carrier_FREQ = (x_val - x_val_MIN) / x_RANGE * carrier_RANGE + carrier_OFFSET;
                carrierFrequency.setValue(carrier_FREQ);

                float modulator_FREQ = (y_val - y_val_MIN) / y_RANGE * modulator_RANGE + modulator_OFFSET;
                modulatorFrequency.setValue(modulator_FREQ);

                float modulatorDepth_FREQ = (z_val - z_val_MIN) / z_RANGE * modulatorDepth_RANGE + modulatorDepth_OFFSET;
                modulatorDepthFrequency.setValue(modulatorDepth_FREQ);
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
