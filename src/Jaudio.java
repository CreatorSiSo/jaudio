import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.LineEvent.Type;

public class Jaudio {
    public static void main(String[] args) {
        List<Track> tracks = Arrays
                .asList(args)
                .stream()
                .filter(arg -> !arg.startsWith("-"))
                .map(uriString -> new Track(uriString))
                .toList();

        System.err.println(tracks);

        Player player = new Player();
        player.play(tracks, 0);

    }
}

class Player {
    // boolean playing = false;
    // Optional<Track> current;
    // List<Track> playlist;

    void play(List<Track> playlist, int trackIndex) {
        // this.playlist = playlist;
        // if (trackIndex >= playlist.size()) {
        //     this.current = Optional.empty();
        // } else {
        //     this.current = Optional.of(playlist.get(trackIndex));
        // }
        for (Track track : playlist) {
            try {
                playClip(track.source);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    void start() {
    }

    void stop() {
    }

    void toggle() {
    }

    void previous() {
    }

    void next() {
    }

    void seek(double absolute) {
    }

    private static void playClip(File file)
            throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {

        class AudioListener implements LineListener {
            private boolean done = false;

            @Override
            public synchronized void update(LineEvent event) {
                Type eventType = event.getType();
                if (eventType == Type.STOP || eventType == Type.CLOSE) {
                    done = true;
                    notifyAll();
                }
            }

            public synchronized void waitUntilDone() throws InterruptedException {
                while (!done) {
                    wait();
                }
            }
        }

        AudioListener listener = new AudioListener();
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        try {
            Clip clip = AudioSystem.getClip();
            clip.addLineListener(listener);
            clip.open(audioInputStream);
            try {
                clip.start();
                listener.waitUntilDone();
            } finally {
                clip.close();
            }
        } finally {
            audioInputStream.close();
        }
    }
}

class Track {
    File source;
    // String name;

    Track(String sourcePath) {
        this.source = new File(sourcePath);
    }
}
