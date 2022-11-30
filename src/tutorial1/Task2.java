package tutorial1;

import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;
import net.happybrackets.core.HBAction;
import net.happybrackets.core.scheduling.Clock;
import net.happybrackets.device.HB;

import java.lang.invoke.MethodHandles;

public class Task2 implements HBAction {
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device

        Envelope gainEnv = new Envelope(0.5f);

        WavePlayer wp = new WavePlayer(1000, Buffer.TRIANGLE);

        Gain g = new Gain(1, gainEnv);

        g.addInput(wp);
        hb.getAudioOutput().addInput(g);

        // To create this, just type clockTimer 
        Clock clock = HB.createClock(500).addClockTickListener((offset, this_clock) -> {// Write your code below this line 
            
            // Write your code above this line 
        });

        clock.start();// End Clock Timer 

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
