package lt.viko.eif.ljurevicius.lt.viko.eif.ljurevicius.student.LyricsWS;

import io.spring.guides.gs_producing_web_service.GetLyricsRequest;
import io.spring.guides.gs_producing_web_service.GetLyricsResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

//The @Endpoint annotation registers the class with Spring WS as a potential candidate for processing incoming SOAP messages.

@Endpoint
public class LyricsEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    private LyricsRepository lyricsRepository;

    @Autowired
    public LyricsEndpoint(LyricsRepository lyricsRepository) {
        this.lyricsRepository = lyricsRepository;
    }

    //The @PayloadRoot annotation is then used by Spring WS to pick the handler method, based on the message’s namespace and localPart.
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getLyricsRequest")
    //The @ResponsePayload annotation makes Spring WS map the returned value to the response payload.
    @ResponsePayload

    //The @RequestPayload annotation indicates that the incoming message will be mapped to the method’s request parameter.
    public GetLyricsResponse getLyrics(@RequestPayload GetLyricsRequest request) {
        GetLyricsResponse response = new GetLyricsResponse();

        response.setLyric(lyricsRepository.findTopSong(request.getCountry(), request.getGenre()));

        return response;
    }
}