package Tutorial5;

import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.*;
import net.happybrackets.core.HBAction;
import net.happybrackets.core.scheduling.Clock;
import net.happybrackets.device.HB;
import net.happybrackets.device.sensors.AccelerometerListener;
import tutorial4.session4.detectOrientation;

import java.lang.invoke.MethodHandles;

public class SowmikParapurath_CreativeCode2 implements HBAction {

    int counter = 0;
    int tempoSelector = 0;
    enum direction { LEFT, RIGHT, FRONT, BACK, UP, DOWN};

    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device

        // To create this, just type clockTimer
        Clock clock = HB.createClock(500/16);
        clock.start();

        float melody[] = {Pitch.mtof(72), Pitch.mtof(76), Pitch.mtof(74), Pitch.mtof(77),
                          Pitch.mtof(76), Pitch.mtof(79), Pitch.mtof(77), Pitch.mtof(74),
                          Pitch.mtof(72), Pitch.mtof(76), Pitch.mtof(74), Pitch.mtof(77),
                          Pitch.mtof(76), Pitch.mtof(72), Pitch.mtof(74), 0f
                         };

        float bass[] = {Pitch.mtof(36), Pitch.mtof(40), Pitch.mtof(38), Pitch.mtof(41),
                        Pitch.mtof(40), Pitch.mtof(43), Pitch.mtof(41), Pitch.mtof(38),
                        Pitch.mtof(36), Pitch.mtof(40), Pitch.mtof(38), Pitch.mtof(41),
                        Pitch.mtof(40), Pitch.mtof(36), Pitch.mtof(38), 0f
                       };

        float tempo[] = {16, 32};

        float[] acc_vals = new float[6];

        TapIn tin = new TapIn(100);
        TapOut tout = new TapOut(tin, 80);

        Bead melodyPattern = new Bead() {
            @Override
            protected void messageReceived(Bead message) {

                if (clock.getNumberTicks() % tempo[tempoSelector] == 0){

                    //Playing the melody line with modulating the amplitude and adding a delay
                    WavePlayer melodyLine = new WavePlayer(melody[counter], Buffer.SINE);

                    //Low pass butterworth filter added for some grainy emphasis on the notes
                    Noise noise = new Noise();
                    BiquadFilter filter = new BiquadFilter(1, BiquadFilter.Type.BUTTERWORTH_LP);
                    filter.setFrequency(melody[counter]);
                    filter.setQ(10f);
                    filter.addInput(noise);

                    hb.setStatus("Count = "+counter);

                    Envelope amp = new Envelope(0);
                    amp.addSegment(0.05f, 50);
                    amp.addSegment(0.02f, 200);

                    Gain gain = new Gain(1, amp);

                    amp.addSegment(0, 50, new KillTrigger(gain));

                    gain.addInput(melodyLine);
                    gain.addInput(filter);

                    hb.getAudioOutput().addInput(gain);

                    counter++;
                    if(counter == 16) counter = 0;

                }
            }
        };

        Bead bassPattern = new Bead() {
            @Override
            protected void messageReceived(Bead message) {

                if (clock.getNumberTicks() % tempo[tempoSelector] == 0){

                    //Playing the melody line with modulating the amplitude and adding a delay
                    WavePlayer melodyLine = new WavePlayer(bass[counter], Buffer.SAW);

                    hb.setStatus("Count = "+counter);

                    Envelope amp = new Envelope(0);
                    amp.addSegment(0.05f, 50);
                    amp.addSegment(0.02f, 200);

                    Gain gain = new Gain(1, amp);

                    amp.addSegment(0, 50, new KillTrigger(gain));

                    gain.addInput(melodyLine);

                    hb.sound(gain);

                    //Adding delay
                    tin.addInput(gain);
                    hb.sound(tout);

                    counter++;
                    if(counter == 16) counter = 0;

                }
            }
        };

        Bead sequencer = new Bead() {
            @Override
            protected void messageReceived(Bead message) {

                new AccelerometerListener(hb) {
                    @Override
                    public void sensorUpdated(float x_val, float y_val, float z_val) { // Write your code below this line
                        direction orientation = direction.BACK;

                        float max_val = 0;
                        acc_vals[0] = x_val;
                        acc_vals[1] = x_val * -1;
                        acc_vals[2] = y_val;
                        acc_vals[3] = y_val * -1;
                        acc_vals[4] = z_val;
                        acc_vals[5] = z_val * -1;

                        for (int i = 0; i < 6; i++) {
                            if (acc_vals[i] > max_val) {
                                orientation = direction.values()[i];
                                max_val = acc_vals[i];
                            }
                        }

                        if(orientation == direction.FRONT){
                            tempoSelector = 0;
                            melodyPattern.pause(false);
                            bassPattern.pause(true);
                        }
                        else if(orientation == direction.BACK){
                            tempoSelector = 1;
                            melodyPattern.pause(false);
                            bassPattern.pause(true);
                        }
                        else if(orientation == direction.LEFT){
                            tempoSelector = 0;
                            melodyPattern.pause(true);
                            bassPattern.pause(false);
                        }
                        else if(orientation == direction.RIGHT){
                            tempoSelector = 1;
                            melodyPattern.pause(true);
                            bassPattern.pause(false);
                        }
                        else if(orientation == direction.UP){
                            hb.setStatus("Side: " + orientation + "/ Melody status: Paused / Count = " +counter);
                            melodyPattern.pause(true);
                            bassPattern.pause(true);
                        }
                    }
                };
            }
        };

        // To create this, just type clockTimer
        clock.addClockTickListener((offset, this_clock) -> {// Write your code below this line

            melodyPattern.message(null);
            bassPattern.message(null);
            sequencer.message(null);

        });

        melodyPattern.pause(true);
        bassPattern.pause(true);

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