package lt.viko.eif.ljurevicius.lt.viko.eif.ljurevicius.student.LyricsWS;

import io.spring.guides.gs_producing_web_service.GetCountryListRequest;
import io.spring.guides.gs_producing_web_service.GetCountryListResponse;
import io.spring.guides.gs_producing_web_service.GetGenreListRequest;
import io.spring.guides.gs_producing_web_service.GetGenreListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

//The @Endpoint annotation registers the class with Spring WS as a potential candidate for processing incoming SOAP messages.

@Endpoint
public class GenreListEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    private GenreListRepository genreListRepository;

    @Autowired
    public GenreListEndpoint(GenreListRepository genreListRepository) {
        this.genreListRepository = genreListRepository;
    }

    //The @PayloadRoot annotation is then used by Spring WS to pick the handler method, based on the message’s namespace and localPart.
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getGenreListRequest")
    //The @ResponsePayload annotation makes Spring WS map the returned value to the response payload.
    @ResponsePayload

    //The @RequestPayload annotation indicates that the incoming message will be mapped to the method’s request parameter.
    public GetGenreListResponse getLyrics(@RequestPayload GetGenreListRequest request) {
        GetGenreListResponse response = new GetGenreListResponse();

        response.setGenreList(genreListRepository.getGenresList());

        return response;
    }
}