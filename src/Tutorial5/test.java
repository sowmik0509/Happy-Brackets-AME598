package Tutorial5;

import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.*;
import net.happybrackets.core.HBAction;
import net.happybrackets.core.scheduling.Clock;
import net.happybrackets.device.HB;

import java.lang.invoke.MethodHandles;

public class test implements HBAction {

    int counter = 0;

    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below


        float melody[] = {Pitch.mtof(72), Pitch.mtof(76), Pitch.mtof(74), Pitch.mtof(77),
                Pitch.mtof(76), Pitch.mtof(79), Pitch.mtof(77), Pitch.mtof(74),
                Pitch.mtof(72), Pitch.mtof(76), Pitch.mtof(74), Pitch.mtof(77),
                Pitch.mtof(76), Pitch.mtof(72), Pitch.mtof(74), 0f
        };
        TapIn tin = new TapIn(100);
        TapOut tout = new TapOut(tin, 50);

        // To create this, just type clockTimer
        Clock clock = HB.createClock(500).addClockTickListener((offset, this_clock) -> {// Write your code below this line



                        //Playing the melody line with modulating the amplitude and adding a delay
                        WavePlayer melodyLine = new WavePlayer(melody[counter], Buffer.SINE);

                        hb.setStatus("count =" + counter);

                        Envelope amp = new Envelope(0);
                        amp.addSegment(0.5f, 50);
                        amp.addSegment(0.2f, 200);

                        Gain gain = new Gain(1, amp);

                        amp.addSegment(0, 50, new KillTrigger(gain));
                        gain.addInput(melodyLine);

                        hb.getAudioOutput().addInput(gain);

                        counter++;


                        // Write your code above this line
        });

        clock.start();// End Clock Timer

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
