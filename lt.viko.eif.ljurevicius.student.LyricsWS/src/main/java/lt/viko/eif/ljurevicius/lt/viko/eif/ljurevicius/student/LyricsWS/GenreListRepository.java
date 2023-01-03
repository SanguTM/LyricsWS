package lt.viko.eif.ljurevicius.lt.viko.eif.ljurevicius.student.LyricsWS;

import io.spring.guides.gs_producing_web_service.Countries;
import io.spring.guides.gs_producing_web_service.Genres;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
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
public class GenreListRepository {

    private static final Map<String, Genres> genres = new HashMap<>();

    /* Spring calls the methods annotated with @PostConstruct only once,
     just after the initialization of bean properties.
     Keep in mind that these methods will run even if there's nothing to initialize.*/

    //@PostConstruct
    // A method annotated with @EventListener can return a non-void type.
    // If the value returned is non-null, the eventing mechanism will publish a new event for it.

    /**
     * Method returns response body to webservice.
     */

    @ResponseBody
    public Genres getGenresList() {

        return genres.get("genres");
    }

    /**
     * After method was called from outside class, this method fills result map.
     */
    //@EventListener({DataSource.class})
    @PostConstruct
    public void initData() {

        Genres g = new Genres();
        g.setDANCE("DANCE");
        g.setPOP("POP");
        g.setHIPHOP("HIP_HOP_RAP");

        genres.put("genres", g);
    }
}