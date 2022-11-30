package tutorial1;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.*;
import net.happybrackets.core.HBAction;
import net.happybrackets.core.scheduling.Clock;
import net.happybrackets.device.HB;
import java.lang.invoke.MethodHandles;
public class SowmikParapurath_CreativeCode1 implements HBAction
{
    int count = 0;
    @Override
    public void action(HB hb)
    {
        hb.reset();

        //Adding a drum segment to the entire melody line
        Sample sampleDrums = SampleManager.sample("data/audio/long/DrumLoop130.wav");
        SamplePlayer samplePlayer = new SamplePlayer(sampleDrums);
        samplePlayer.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);
        float sampleLength = (float) sampleDrums.getLength();
        Envelope e = new Envelope(0f);
        e.addSegment(0.04f, sampleLength);
        e.addSegment(0.03f, sampleLength * 6);
        Gain g = new Gain(1, e);
        e.addSegment(0f, sampleLength, new KillTrigger(g));
        g.addInput(samplePlayer);
        hb.sound(g);

        //Getting all the frequency values for the corresponding notes

        TapIn tin = new TapIn(200);
        TapOut tout = new TapOut(tin, 100);

        //Creating an array for playing the melody line sequentially
        float[] melody = {
                Pitch.mtof(60), Pitch.mtof(52), Pitch.mtof(53), Pitch.mtof(55), Pitch.mtof(57), Pitch.mtof(59), Pitch.mtof(60), Pitch.mtof(62),
                Pitch.mtof(64), Pitch.mtof(55), Pitch.mtof(57), Pitch.mtof(59), Pitch.mtof(60), Pitch.mtof(62), Pitch.mtof(64), Pitch.mtof(65),
                Pitch.mtof(67), Pitch.mtof(60), Pitch.mtof(62), Pitch.mtof(64), Pitch.mtof(65), Pitch.mtof(67), Pitch.mtof(69), Pitch.mtof(71),
                Pitch.mtof(72), Pitch.mtof(67), Pitch.mtof(69), Pitch.mtof(71), Pitch.mtof(72), Pitch.mtof(74), Pitch.mtof(76), Pitch.mtof(77),
                Pitch.mtof(79), Pitch.mtof(76), Pitch.mtof(77), Pitch.mtof(74), Pitch.mtof(76), Pitch.mtof(72), Pitch.mtof(74), Pitch.mtof(71),
                Pitch.mtof(72), Pitch.mtof(69), Pitch.mtof(71), Pitch.mtof(67), Pitch.mtof(69), Pitch.mtof(65), Pitch.mtof(67), Pitch.mtof(64),
                Pitch.mtof(65), Pitch.mtof(62), Pitch.mtof(64), Pitch.mtof(60), Pitch.mtof(62), Pitch.mtof(59), Pitch.mtof(60), Pitch.mtof(57),
                Pitch.mtof(59), Pitch.mtof(55), Pitch.mtof(57), Pitch.mtof(59), Pitch.mtof(60), 0f, Pitch.mtof(72), 0f
        };

        //Clock at a tempo of 130BPM
        Clock clock = HB.createClock(60000/130).addClockTickListener((offset, this_clock) ->
        {
            //Playing the melody line with modulating the amplitude and adding a delay
            WavePlayer melodyLine = new WavePlayer(melody[count], Buffer.SINE);
            hb.setStatus("count ="+count);
            Envelope amp = new Envelope(0);
            amp.addSegment(0.08f, 50);
            amp.addSegment(0.05f, 200);
            Gain gain = new Gain(1, amp);
            tin.addInput(gain);
            hb.getAudioOutput().addInput(tout);
            Gain feedbackGain = new Gain(1, 0.05f);
            feedbackGain.addInput(tout);
            tin.addInput(feedbackGain);
            amp.addSegment(0, 50, new KillTrigger(gain));
            gain.addInput(melodyLine);

            //Playing the bass note C at every 4th beat
            if(count % 4 == 0 && count <= 32)
            {
                float freq = Pitch.mtof(24);
                WavePlayer wp = new WavePlayer(freq, Buffer.SAW);
                Envelope e1 = new Envelope(0);
                e1.addSegment(0.05f, 50);
                e1.addSegment(0.03f, 200);
                Gain g2 = new Gain(1, e1);
                e1.addSegment(0, 50, new KillTrigger(g2));
                g2.addInput(wp);
                hb.getAudioOutput().addInput(g2);
            }

            //Playing the bass note C at every 2nd beat
            else if(count % 2 == 0 && count > 32 && count <= 56)
            {
                float freq = Pitch.mtof(24);
                WavePlayer wp = new WavePlayer(freq, Buffer.SAW);
                Envelope e1 = new Envelope(0);
                e1.addSegment(0.05f, 50);
                e1.addSegment(0.03f, 200);
                Gain g2 = new Gain(1, e1);
                e1.addSegment(0, 50, new KillTrigger(g2));
                g2.addInput(wp);
                hb.getAudioOutput().addInput(g2);
            }

            //Playing the bass note C at every beat
            else if(count > 56 && count < 63 && count != 61)
            {
                float freq = Pitch.mtof(24);
                WavePlayer wp = new WavePlayer(freq, Buffer.SAW);
                Envelope e1 = new Envelope(0);
                e1.addSegment(0.05f, 50);
                e1.addSegment(0.03f, 200);
                Gain g2 = new Gain(1, e1);
                e1.addSegment(0, 50, new KillTrigger(g2));
                g2.addInput(wp);
                hb.getAudioOutput().addInput(g2);
            }

            hb.getAudioOutput().addInput(gain);
            count++;
        });
        clock.start();
    }
}