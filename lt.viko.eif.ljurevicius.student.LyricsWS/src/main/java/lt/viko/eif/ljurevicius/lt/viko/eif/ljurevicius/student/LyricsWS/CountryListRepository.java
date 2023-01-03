package lt.viko.eif.ljurevicius.lt.viko.eif.ljurevicius.student.LyricsWS;

import io.spring.guides.gs_producing_web_service.Countries;
import io.spring.guides.gs_producing_web_service.Lyric;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
@Component is an annotation that allows Spring to automatically detect our custom beans.

In other words, without having to write any explicit code, Spring will:

    Scan our application for classes annotated with @Component
    Instantiate them and inject any specified dependencies into them
    Inject them wherever needed

 */

@Component
public class CountryListRepository {

    private static final Map<String, Countries> countries = new HashMap<>();

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
    public Countries getCountryList() {

        return countries.get("countries");
    }

    /**
     * After method was called from outside class, this method fills result map.
     */
    //@EventListener({DataSource.class})
    @PostConstruct
    public void initData() {

        Countries c = new Countries();
        c.setGermany("DE");
        c.setGreatBritain("UK");
        c.setPoland("PL");
        c.setItaly("IT");
        c.setPortugal("PL");

        countries.put("countries", c);
    }
}