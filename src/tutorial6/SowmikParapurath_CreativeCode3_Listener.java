package tutorial6;

import Tutorial5.SowmikParapurath_CreativeCode2;
import de.sciss.net.OSCMessage;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.*;
import net.happybrackets.core.HBAction;
import net.happybrackets.core.OSCUDPListener;
import net.happybrackets.core.scheduling.Clock;
import net.happybrackets.device.HB;
import net.happybrackets.device.sensors.AccelerometerListener;

import java.lang.invoke.MethodHandles;
import java.net.SocketAddress;

public class SowmikParapurath_CreativeCode3_Listener implements HBAction {

    int counter = 0;
    int tempoSelector = 0;

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


                // type osclistener to create this code
                OSCUDPListener oscudpListener = new OSCUDPListener(0) {
                    @Override
                    public void OSCReceived(OSCMessage oscMessage, SocketAddress socketAddress, long time) {
                        // type your code below this line

                        String myName = oscMessage.getName();
                        hb.setStatus(myName + " | " + oscMessage.getArg(0));

                        if(oscMessage.getArg(0).equals("0") ){
                            tempoSelector = 0;
                            melodyPattern.pause(false);
                            bassPattern.pause(true);
                        }
                        else if(oscMessage.getArg(0).equals("1")){
                            tempoSelector = 1;
                            melodyPattern.pause(false);
                            bassPattern.pause(true);
                        }
                        else if(oscMessage.getArg(0).equals("2")){
                            tempoSelector = 0;
                            melodyPattern.pause(true);
                            bassPattern.pause(false);
                        }
                        else if(oscMessage.getArg(0).equals("3")){
                            tempoSelector = 1;
                            melodyPattern.pause(true);
                            bassPattern.pause(false);
                        }
                        else if(oscMessage.getArg(0).equals("4")){
                            melodyPattern.pause(true);
                            bassPattern.pause(true);
                        }

                        // type your code above this line
                    }
                };
                if (oscudpListener.getPort() < 0) { //port less than zero is an error
                    String error_message = oscudpListener.getLastError();
                    System.out.println("Error opening port " + 0 + " " + error_message);
                } // end oscListener code


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
