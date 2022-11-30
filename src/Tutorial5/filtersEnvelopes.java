package Tutorial5;

import net.beadsproject.beads.ugens.*;
import net.happybrackets.core.HBAction;
import net.happybrackets.device.HB;

import java.lang.invoke.MethodHandles;
import java.util.Random;

public class filtersEnvelopes implements HBAction {
    @Override
    public void action(HB hb) {


        hb.reset(); //Clears any running code on the device

        Envelope filterFreq = new Envelope(2000);
        Envelope gainEnvelop    = new Envelope(0f);

        Random random = new Random();

        Noise n = new Noise();
        Gain g1 = new Gain(2, gainEnvelop);

        // add envelop to broad band noise to prevent sudden glitch when app starts
        gainEnvelop.addSegment(0.2f, 300);

        BiquadFilter filter = new BiquadFilter(2, BiquadFilter.Type.LP);

        filter.setFrequency(filterFreq);
        filterFreq.addSegment( 1000, 100);

        for (int i = 0; i < 6; i++){

            filterFreq.addSegment( 50f, 20000);
            filterFreq.addSegment( 1000f, 20000);
        }


        filter.addInput(n);
        g1.addInput(filter);


        Gain g2 = new Gain(2, 0.3f);

        BiquadFilter sweepFilter = new BiquadFilter(2, BiquadFilter.Type.BP_PEAK);
        Envelope sweepFreq = new Envelope(1000);

        for (int i = 0; i < 16; i++){

            sweepFreq.addSegment(100f, 4000);
            sweepFreq.addSegment(4000f, 8000);
        }

        sweepFilter.setFrequency(sweepFreq);
        //sweepFilter.setQ(10);
        sweepFilter.setQ(2);

        // explore changin the Q
        //sweepFilter.setQ(50);
        sweepFilter.addInput(n);
        g2.addInput(sweepFilter);


            //hb.sound(g1);
            //hb.sound(g2);
        hb.ac.out.addInput(g1);
        hb.ac.out.addInput(g2);
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
