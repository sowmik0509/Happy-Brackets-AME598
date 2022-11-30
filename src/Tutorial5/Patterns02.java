package Tutorial5;

import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.happybrackets.core.HBAction;
import net.happybrackets.core.scheduling.Clock;
import net.happybrackets.device.HB;

public class Patterns02 implements HBAction {

    int counter = 0;

    @Override
    public void action(HB hb) {

        //reset
        hb.reset();
        //load a set of sounds
        SampleManager.group("Guitar", "data/audio/Nylon_Guitar");
        Sample s = SampleManager.fromGroup("Guitar", 1);

        int rate = 1;

        int index = Math.abs(hb.myIndex());
        hb.setStatus("Index: " + index);

        // To create this, just type clockTimer
        Clock clock = HB.createClock(500 / 16);
        clock.start();


        //create 3 musical patterns

        Bead patten01 = new Bead() {
            @Override
            protected void messageReceived(Bead message) {
                if (index % 2 == 0) {
                    if (clock.getNumberTicks() % 32 == 0) {
                        //play a new random sound
                        SamplePlayer sp = new SamplePlayer( s);
                        sp.getRateUGen().setValue(rate);
                        hb.sound(sp);
                    }
                }
            }
        };

        Bead patten02 = new Bead() {
            @Override
            protected void messageReceived(Bead message) {

                if (index % 2 == 1) {
                    if (clock.getNumberTicks() % 12 == 0) {
                        //play a new random sound
                        SamplePlayer sp = new SamplePlayer(s);
                        sp.getRateUGen().setValue(rate * 2);
                        hb.sound(sp);
                    }
                }
            }
        };

        Bead patten03 = new Bead() {
            @Override
            protected void messageReceived(Bead message) {
                if (clock.getNumberTicks() % 16 == 0) {
                    //play a new random sound
                    SamplePlayer sp = new SamplePlayer(s);
                    sp.getRateUGen().setValue(rate * 4f);
                    hb.sound(sp);
                    //hb.getAudioOutput().addInput(sp);
                    //counter++;

                }
            }
        };

        //create a fourth pattern which does not play anything, it just manages the changes
        Bead pattenMixer = new Bead() {
            @Override
            protected void messageReceived(Bead message) {
                if (clock.getNumberTicks() % 32 == 0) {
                    if(counter < 10) {
                        patten01.pause(false);
                        counter++;
                    } else if(counter < 20) {
                        patten01.pause(false);
                        patten02.pause(false);
                        counter++;
                    } else if(counter < 30) {
                        patten01.pause(false);
                        patten02.pause(false);
                        patten03.pause(false);
                        counter++;
                        //etc. etc.
                    } else if(counter < 60) {
                        patten01.pause(true);
                        patten02.pause(false);
                        patten03.pause(false);
                        counter++;

                    } else if(counter < 75) {
                        patten01.pause(true);
                        patten02.pause(true);
                        patten03.pause(true);
                        counter++;
                    } else if (counter > 74){
                        counter = 0;
                    }

                    hb.setStatus("counter " +counter);
                }
            }
        };


        //start the patterns
        clock.addClockTickListener((offset, this_clock) -> {
            patten01.message(null);
            patten02.message(null);
            patten03.message(null);
            pattenMixer.message(null);
        });

        //start off with all the music patterns paused
        patten01.pause(true);
        patten02.pause(true);
        patten03.pause(true);

    }
}
