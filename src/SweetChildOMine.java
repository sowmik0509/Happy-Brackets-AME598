import net.beadsproject.beads.core.Bead;
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


public class SweetChildOMine implements HBAction {
    int count  = 0;
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        int C = 60;
        int cSHARP = 61;
        int cHIGH = C + 12;
        int cSHARPHIGH = cSHARP + 12;

        int D = 62;
        int dHIGH = D + 12;

        int E = 64;
        int eHIGH = E + 12;

        int F = 65;
        int fSHARP = 66;
        int fHIGH = F + 12;
        int fSHARPHIGH = fSHARP + 12;

        int G = 67;
        int gHIGH = G + 12;

        int A = 69;
        int aHIGH = A + 12;

        int B = 71;
        int bHIGH = B + 12;



        float freqC = Pitch.mtof(C);
        float freqCSHARP = Pitch.mtof(cSHARP);
        float freqCHIGH = Pitch.mtof(cHIGH);
        float freqcSHARPHIGH = Pitch.mtof(cSHARPHIGH);

        float freqD = Pitch.mtof(D);
        float freqDHIGH = Pitch.mtof(dHIGH);

        float freqE = Pitch.mtof(E);
        float freqEHIGH = Pitch.mtof(eHIGH);

        float freqF = Pitch.mtof(F);
        float freqfSHARP = Pitch.mtof(fSHARP);
        float freqFHIGH = Pitch.mtof(fHIGH);
        float freqfSHARPHIGH = Pitch.mtof(fSHARPHIGH);

        float freqG = Pitch.mtof(G);
        float freqGHIGH = Pitch.mtof(gHIGH);

        float freqA = Pitch.mtof(A);
        float freqAHIGH = Pitch.mtof(aHIGH);

        float freqB = Pitch.mtof(B);
        float freqBHIGH = Pitch.mtof(bHIGH);



        float[] intro = {
                freqD, freqDHIGH, freqA, freqG, freqGHIGH, freqA, freqfSHARPHIGH, freqA,
                freqD, freqDHIGH, freqA, freqG, freqGHIGH, freqA, freqfSHARPHIGH, freqA,
                freqE, freqDHIGH, freqA, freqG, freqGHIGH, freqA, freqfSHARPHIGH, freqA,
                freqE, freqDHIGH, freqA, freqG, freqGHIGH, freqA, freqfSHARPHIGH, freqA,
                freqG, freqDHIGH, freqA, freqG, freqGHIGH, freqA, freqfSHARPHIGH, freqA,
                freqG, freqDHIGH, freqA, freqG, freqGHIGH, freqA, freqfSHARPHIGH, freqA,

                freqEHIGH, freqA, freqDHIGH, freqA, freqEHIGH, freqA, freqfSHARPHIGH, freqA,
                freqGHIGH, freqA, freqfSHARPHIGH, freqA, freqEHIGH, freqA, freqDHIGH

        };



        // To create this, just type clockTimer
        Clock clock = HB.createClock(200).addClockTickListener((offset, this_clock) -> {// Write your code below this line

            WavePlayer momentary = new WavePlayer(intro[count], Buffer.TRIANGLE);

            Envelope amp = new Envelope(0);
            amp.addSegment(0.5f, 50);
            amp.addSegment(0.5f, 100);

            Gain g = new Gain(1, amp);

            amp.addSegment(0, 50, new KillTrigger(g));

            g.addInput(momentary);

            HB.getAudioOutput().addInput(g);

            count++;
        });

        clock.start();// End Clock Timer

    }
}

