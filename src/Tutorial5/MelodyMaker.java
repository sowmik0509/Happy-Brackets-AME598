/*
 * Copyright 2016 Ollie Bown
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package Tutorial5;

import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;
import net.happybrackets.core.HBAction;
import net.happybrackets.core.scheduling.Clock;
import net.happybrackets.device.HB;


/*
 * Created by Garth Paine, June 2020.  Extending a patch by Oliver Bown
 *
 * This patch demonstrates the use of the Pitch class to generate notes in a scale
 * Note the use of MIDI notes and the Pitch.mtof(pitch) function to convert the MIDI note to a frequency as required by the WavePlayer
 *
 * Take Note of
 * hb.rng.nextInt() which generates a new random note each time it is called
 * Pitch.forceToScale() which forces the selected integer into a defined scale
 * The use of the KillTrigger in the envelop generation
 *
 * Hack and enjoy
 *
 * nb. note that envelope segments are generated in each clock cycle with the WavePlayer
 *
 * */

public class MelodyMaker implements HBAction {

    @Override
    public void action(HB hb) {
        /*
         * experiment with the following options for clearing sounds
         */
        hb.reset();
//        hb.resetLeaveSounding();
//        hb.fadeOutReset(1000);
//        hb.clearSound();
        /*
         * make a new sound, with a random note from minor scale...
         */

        // To create this, just type clockTimer
        // the clock triggers and event every 500ms - twice a second = 120BPM
        Clock clock = HB.createClock(500).addClockTickListener((offset, this_clock) -> {// Write your code below this line

            // pitch can be handled as frequency in HZ or a MIDI pitches 0-127
            // here we set the note using the MIDI scale
            // the formula below is to set the base pitch (32) and then add to it using the random
            // here we have a range of 32+0 = 32 up to 32+48 = 80  NOTE we are using the minor scale
            // nb. hb.rng is the HappyBrackets random number generator
            int pitch = Pitch.forceToScale(hb.rng.nextInt(48) + 32, Pitch.minor);
            //and then convert it to frequency as the WavePlayer object requires a frequency
            float freq = Pitch.mtof(pitch);

            //make a wave player to generate the sound
            WavePlayer sawWave = new WavePlayer(hb.ac, freq, Buffer.SAW);
            //make an envelop to control the gain
            Envelope e = new Envelope(0.3f);
            //make the gain object and use the envelop to set the value
            Gain g = new Gain(hb.ac, 1, e);
            //add the WavePlayer to the input fo the gain objects
            g.addInput(sawWave);

            //we can add a KillTrigger to kill off the synth voice after a set period of time
            e.addSegment(0f, 2000, new KillTrigger(g));

            //play the sound
            hb.sound(g);
            // Write your code above this line
        });

        clock.start();// End Clock Timer




    }

}
