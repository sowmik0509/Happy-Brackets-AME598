package tutorial1;

import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;
import net.happybrackets.core.HBAction;
import net.happybrackets.device.HB;

import java.lang.invoke.MethodHandles;

public class Task1 implements HBAction {
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device

        Envelope freqEnv = new Envelope(440);

        WavePlayer wp1= new WavePlayer(freqEnv, Buffer.SINE);
        WavePlayer wp2= new WavePlayer(220, Buffer.TRIANGLE);

        Gain g = new Gain(1, 0.1f);

        g.addInput(wp1);
        g.addInput(wp2);
        hb.sound(g);

        for(int i = 0; i < 1000; i++) {
            freqEnv.addSegment(880, 60);
            freqEnv.addSegment(440, 30);
            //freqEnv.addSegment(440, 500);
        }

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
