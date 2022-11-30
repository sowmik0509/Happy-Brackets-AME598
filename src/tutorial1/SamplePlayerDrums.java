package tutorial1;

import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.happybrackets.core.HBAction;
import net.happybrackets.device.HB;

import java.lang.invoke.MethodHandles;

public class SamplePlayerDrums implements HBAction {
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device

        //Write your sketch below

        Sample sampleDrums = SampleManager.sample("data/audio/long/DrumLoop130.wav");
        SamplePlayer samplePlayer = new SamplePlayer(sampleDrums);
        samplePlayer.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);

        float sampleLength = (float)sampleDrums.getLength();

        Envelope e = new Envelope(0f);
        e.addSegment(0.5f, sampleLength);
        e.addSegment(0.5f, sampleLength*2);

        Gain g = new Gain(1, e);

        e.addSegment(0f, sampleLength, new KillTrigger(g));

        g.addInput(samplePlayer);
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
