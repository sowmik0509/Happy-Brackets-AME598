package Tutorial5;

import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.*;
import net.happybrackets.core.HBAction;
import net.happybrackets.core.scheduling.Clock;
import net.happybrackets.device.HB;

public class folkyTune implements HBAction {

        // these are class variables - they can be called from anywhere in the program
        int loopCounter = -1;
        int baseFrequency = 24;

        //note this patch only runs on the Pi or the simulator
        @Override
        public void action(HB hb) {
            hb.reset(); //Clears any running code on the device

            // çreate clock
            Clock clock = HB.createClock(500 / 16);
            clock.start();

            //find the ID of the unit
            int myIndex = Math.abs(hb.myIndex());
            hb.setStatus("Index: " +(myIndex));
            //hb.setStatus(String.valueOf(myIndex));

            TapIn ti = new TapIn(1000);
            TapOut to = new TapOut(ti, 333);
            Gain feedBack = new Gain(1, 0.2f);
            feedBack.addInput(to);
            ti.addInput(feedBack);

            TapIn ti2 = new TapIn(1000);
            TapOut to2 = new TapOut(ti2, 666);
            Gain feedBack2 = new Gain(1, 0.3f);
            feedBack2.addInput(to2);
            ti2.addInput(feedBack2);

            Bead bassMelody = new Bead() {
                @Override
                protected void messageReceived(Bead message) {

                    if (clock.getNumberTicks() % 64 == 0) {

                        int scaleFreq = Pitch.minor[ hb.rng.nextInt(7)];
                        float bassPitch = (Pitch.mtof(baseFrequency + scaleFreq));
                        //hb.setStatus("scaleFreq " + scaleFreq);
                        WavePlayer ebmWP = new WavePlayer(bassPitch, Buffer.SQUARE);

                        Envelope ebm = new Envelope(0);
                        ebm.addSegment(0.08f, 10);
                        ebm.addSegment(0.05f, 200);

                        Gain gBass = new Gain(1, ebm);
                        ebm.addSegment(0.0f, 100, new KillTrigger(gBass));
                        gBass.addInput(ebmWP);

                        ti.addInput(gBass);
                        //hb.getAudioOutput().addInput(gBass);
                        //hb.getAudioOutput().addInput(to);
                        gBass.connectTo(HB.getAudioOutput());
                        to.connectTo(HB.getAudioOutput());
                    }

                }
            };

            Bead melodyOne = new Bead() {
                @Override
                protected void messageReceived(Bead message) {

                    if (clock.getNumberTicks() % 16 == 0) {

                        WavePlayer wpM1 = new WavePlayer( (Pitch.mtof(84 + Pitch.minor[ hb.rng.nextInt(7)])), Buffer.SINE);

                        Envelope eM1 = new Envelope( 0);
                        eM1.addSegment(0.05f, 20);
                        eM1.addSegment(0.035f, 100);

                        Gain gM1 = new Gain(1, eM1);
                        eM1.addSegment(0.0f, 100, new KillTrigger(gM1));
                        gM1.addInput(wpM1);

                        ti.addInput(gM1);

                        hb.sound(gM1);
                        //hb.getAudioOutput().addInput(gM1);

                    }
                }
            };

            Bead melodyTwo = new Bead() {
                @Override
                protected void messageReceived(Bead message) {

                    if (clock.getNumberTicks() % 32 == 0) {

                        WavePlayer wpM2 = new WavePlayer((Pitch.mtof(72 + Pitch.minor[ hb.rng.nextInt(7)])), Buffer.SINE);

                        Envelope eM2 = new Envelope(0);
                        eM2.addSegment(0.05f, 100);
                        eM2.addSegment(0.03f, 50);

                        Gain gM2 = new Gain(1, eM2);
                        eM2.addSegment(0.0f, 50, new KillTrigger(gM2));
                        gM2.addInput(wpM2);

                        ti2.addInput(gM2);
                        gM2.connectTo(HB.getAudioOutput());
                        //hb.getAudioOutput().addInput(gM2);
                        //hb.getAudioOutput().addInput(to2);
                    }
                }

            };

            Bead melodyTransposer = new Bead() {
                @Override
                protected void messageReceived(Bead message) {
                    if(clock.getNumberTicks() % 64 == 0){

                        //hb.setStatus("loopCounter: " + loopCounter);
                        loopCounter++;

                        if (loopCounter < 4){
                            baseFrequency = 32;
                            //hb.setStatus("loopCounter: " + 1);
                        }

                        else if (loopCounter < 8){
                            baseFrequency = 39;
                            //hb.setStatus("loopCounter: " + 2);
                        }

                        else if (loopCounter < 13)
                        {
                            baseFrequency = 36;
                            //hb.setStatus("loopCounter: " + 3);
                        }

                        else if (loopCounter < 16){
                            baseFrequency = 24;
                            //hb.setStatus("loopCounter: " + 4);
                        }

                        else if (loopCounter > 16){
                            baseFrequency = 24;
                            loopCounter = -1;
                            //hb.setStatus("loopCounter Reset ");
                        }

                        hb.setStatus("baseFrequency: " +   baseFrequency);

                    }
                }
            };

            //start the patterns
            clock.addClockTickListener((offset, this_clock) -> {
                bassMelody.message(null);
                melodyOne.message(null);
                melodyTwo.message(null);
                melodyTransposer.message(null);

            });

            //start off with all the music patterns paused
            bassMelody.pause(false);
            melodyOne.pause(false);
            melodyTwo.pause(true);
            melodyTransposer.pause(false);

        }

    }


