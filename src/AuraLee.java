import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;
import net.happybrackets.core.HBAction;
import net.happybrackets.core.scheduling.Clock;
import net.happybrackets.device.HB;

import java.lang.invoke.MethodHandles;

public class AuraLee implements HBAction {
    int counter  = 0;
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        int G = 68;
        int A = 70;
        int B = 72;
        int C = 74;
        int D = 76;
        int E = 78;
        int F = 80;

        float freqC = Pitch.mtof(C);
        float freqD = Pitch.mtof(D);
        float freqE = Pitch.mtof(E);
        float freqF = Pitch.mtof(F);
        float freqG = Pitch.mtof(G);
        float freqA = Pitch.mtof(A);
        float freqB = Pitch.mtof(B);

        float[] arr = {freqG, freqC, freqB, freqC, freqD, freqA, freqD, freqC, freqB, freqA, freqB, freqC, 0f};

        // To create this, just type clockTimer
        Clock clock = HB.createClock(300).addClockTickListener((offset, this_clock) -> {// Write your code below this line

            WavePlayer momentary = new WavePlayer(arr[counter], Buffer.SINE);

            Envelope amp = new Envelope(0);
            amp.addSegment(0.5f, 100);
            amp.addSegment(0.5f, 200);
            if(counter == 6 || counter == 11) {
                amp.addSegment(0.5f, 200);
                amp.addSegment(0f, 100);
            }

            Gain g = new Gain(1, amp);

            amp.addSegment(0, 50, new KillTrigger(g));

            g.addInput(momentary);

            HB.getAudioOutput().addInput(g);

            counter++;
        });

        clock.start();// End Clock Timer

    }
}

