package tutorial1;

import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;
import net.happybrackets.core.HBAction;
import net.happybrackets.core.scheduling.Clock;
import net.happybrackets.device.HB;

import java.lang.invoke.MethodHandles;

public class EchoPatch implements HBAction {
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        float delayTime = 200f;

        Envelope amp = new Envelope(5f);
        amp.addSegment(0.5f, 20);
        amp.addSegment(0f, 20);

        WavePlayer myPitch = new WavePlayer(hb.rng.nextFloat() * 400 + 200, Buffer.SAW);

        Gain g = new Gain(1, amp);

        Envelope amp2 = new Envelope(0f);
        amp2.addSegment(0f, delayTime/2);
        amp2.addSegment(0f, delayTime/2);
        amp2.addSegment(0f, delayTime/2);




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
