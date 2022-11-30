package tutorial1;

import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;
import net.happybrackets.core.HBAction;
import net.happybrackets.device.HB;

import java.lang.invoke.MethodHandles;

public class Task3 implements HBAction {
    @Override
    public void action(HB hb) {
        hb.reset();

        WavePlayer wp1 = new WavePlayer(400, Buffer.SINE);
        WavePlayer wp2 = new WavePlayer(410, Buffer.SINE);

        Envelope gainEnv1 = new Envelope(0.5f);
        Envelope gainEnv2 = new Envelope(0);

        Gain g1 = new Gain(1, gainEnv1);
        Gain g2 = new Gain(1, gainEnv2);
        Gain g3 = new Gain(2, 0.5f);

        g1.addInput(wp1);
        g2.addInput(wp2);

        g3.addInput(0, g1, 0);
        g3.addInput(1, g2, 0);

        hb.sound(g3);

        for(int i = 0; i < 100; i++){
            gainEnv1.addSegment(0.5f, 1000);
            gainEnv1.addSegment(0, 1000);
            gainEnv1.addSegment(0.5f, 1000);
        }
        for(int i = 0; i < 100; i++){
            gainEnv2.addSegment(0f, 1000);
            gainEnv2.addSegment(0.5f, 1000);
            gainEnv2.addSegment(0f, 1000);
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
