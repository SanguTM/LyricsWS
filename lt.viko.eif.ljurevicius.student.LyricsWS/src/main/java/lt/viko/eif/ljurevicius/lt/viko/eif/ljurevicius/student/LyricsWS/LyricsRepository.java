package lt.viko.eif.ljurevicius.lt.viko.eif.ljurevicius.student.LyricsWS;

import io.spring.guides.gs_producing_web_service.Lyric;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
@Component is an annotation that allows Spring to automatically detect our custom beans.

In other words, without having to write any explicit code, Spring will:

    Scan our application for classes annotated with @Component
    Instantiate them and inject any specified dependencies into them
    Inject them wherever needed

 */

@Component
public class LyricsRepository {

    private static final Map<String, Lyric> lyrics = new HashMap<>();

    /* Spring calls the methods annotated with @PostConstruct only once,
     just after the initialization of bean properties.
     Keep in mind that these methods will run even if there's nothing to initialize.*/

    //@PostConstruct
    // A method annotated with @EventListener can return a non-void type.
    // If the value returned is non-null, the eventing mechanism will publish a new event for it.

    /**
     * After method was called from outside class, this method fills result map.
     */
    @EventListener({DataSource.class})
        public void initData(ArrayList<String> songInfo) {

        String Title = songInfo.get(0);
        String Artist = songInfo.get(1);
        String Lyrics = songInfo.get(2);

        Lyric topSong = new Lyric();
        topSong.setArtist(Artist);
        topSong.setSong(Title);
        topSong.setLyrics(Lyrics);

        lyrics.put("Song", topSong);
    }

    /**
     * Method returns response body to webservice.
     */
    @ResponseBody
    public Lyric findTopSong(String country, String genre) {

        DataSource ds = new DataSource();

        int alreadyExist = ds.getLyricsFromDB(country, genre);

        if (alreadyExist == 0)
        {
            String[] result = ds.GetShazamData(country, genre);

            String songTitle=result[0];
            String songArtist=result[1];

            System.out.println(songTitle);
            System.out.println(songArtist);

            String lyric = ds.GetLyricsData(songTitle, songArtist);

            ds.writeLog(country, genre, lyric, songArtist, songTitle);
        }
        return lyrics.get("Song");
    }
}