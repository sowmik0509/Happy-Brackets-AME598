package Tutorial5;

import com.sun.tools.doclint.Env;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.BiquadFilter;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Noise;
import net.happybrackets.core.HBAction;
import net.happybrackets.device.HB;

import java.lang.invoke.MethodHandles;

public class FilterTest implements HBAction {
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        BiquadFilter filter = new BiquadFilter(1, BiquadFilter.Type.BP_PEAK);
        BiquadFilter filter02 = new BiquadFilter(1, BiquadFilter.Type.LP);
        Noise noise = new Noise();

        Envelope centFreq = new Envelope(1000);
        Envelope qSet = new Envelope(2);

        Gain g = new Gain(1, 0.5f);

        for (int i=0; i<3;i++){

            centFreq.addSegment(3000, 1000);
            centFreq.addSegment(3000, 500);
            centFreq.addSegment(100, 3000);
            centFreq.addSegment(100, 500);
            centFreq.addSegment(1000, 500);

        }

        qSet.addSegment(50, 10000);

        centFreq.addSegment(1000, 100, new KillTrigger(g));

        filter02.setFrequency(centFreq);

        filter02.setQ(10f);

        filter02.addInput(noise);

        g.addInput(filter02);

        hb.sound(g);


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
