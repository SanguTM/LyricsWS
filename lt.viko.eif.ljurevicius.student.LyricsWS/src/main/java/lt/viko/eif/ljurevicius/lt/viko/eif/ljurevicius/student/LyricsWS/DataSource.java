package lt.viko.eif.ljurevicius.lt.viko.eif.ljurevicius.student.LyricsWS;

import io.spring.guides.gs_producing_web_service.Countries;
import okhttp3.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.json.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;


import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DataSource {


    /**
     * Method to get data from Shazam Web API. It generates HTTP request, parse needed data from JSON response and
     * returns it for further use
     */
    public String[] GetShazamData(String Country, String Genre) {
        String songTitle = null;
        String songArtist = null;

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://shazam-core.p.rapidapi.com/v1/charts/genre-country?").newBuilder()

                .addQueryParameter("country_code", Country)
                .addQueryParameter("limit", "1")
                .addQueryParameter("genre_code", Genre);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)//("https://shazam-core.p.rapidapi.com/v1/charts/country?country_code=DE&limit=1")
                .get()
                .addHeader("X-RapidAPI-Host", "shazam-core.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", "febdade88cmsh9a214671ed9dc08p14c2fdjsnb928a00f30d7")
                .build();

        try {
            Response response = client.newCall(request).execute();

            JSONArray jsonarray = new JSONArray(response.body().string());

            //https://stackoverflow.com/questions/17441246/org-json-jsonarray-cannot-be-converted-to-jsonobject
            String title = null;
            String subtitle = null;
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                //String id = jsonobject.getString("id");
                title = jsonobject.getString("title");
                subtitle = jsonobject.getString("subtitle");

                //System.out.println(id);
                System.out.println(title);
                System.out.println(subtitle);
            }

            songTitle = title;
            songArtist = subtitle;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[]{songTitle, songArtist};
    }

    /**
     * Method to get data from Lyrics Plus Web API. It generates HTTP request, parse needed data from JSON response and
     * returns it for further use
     */
    public static String GetLyricsData(String songTitle, String songArtist) {

        OkHttpClient client_lyrics = new OkHttpClient();

        String URL_Lyrics = "https://lyrics-plus.p.rapidapi.com/lyrics/" + songTitle + "/" + songArtist;

        Request request_lyrics = new Request.Builder()
                .url(URL_Lyrics)
                .get()
                .addHeader("X-RapidAPI-Host", "lyrics-plus.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", "febdade88cmsh9a214671ed9dc08p14c2fdjsnb928a00f30d7")
                .build();

        String lyrics = null;
        try {
            Response response_lyrics = client_lyrics.newCall(request_lyrics).execute();

            //https://stackoverflow.com/questions/69295880/json-java-lang-string-cannot-be-converted-to-jsonobject
            String jsonString = response_lyrics.body().string();//assign your JSON String here
            JSONObject obj = new JSONObject(jsonString);
            lyrics = obj.getString("lyrics");

            //String lyrics = obj.getJSONObject("lyrics").getString("lyrics");

            System.out.println(lyrics);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LyricsRepository lr = new LyricsRepository();

        List<String> songInfo = new ArrayList<>();
        songInfo.add(songArtist);
        songInfo.add(songTitle);
        songInfo.add(lyrics);

        lr.initData((ArrayList<String>) songInfo);

        //LyricsRepository lr = new LyricsRepository();
        //lr.initData(songArtist, songTitle, lyrics);

        return lyrics;
    }

    /**
     * Method to create new connection to Microsoft SQL database
     */
    //https://www.codejava.net/java-se/jdbc/connect-to-microsoft-sql-server-via-jdbc
    //https://www.mssqltips.com/sqlservertip/4709/connecting-a-java-program-to-sql-server/
    public static Connection getSQLConnection()
    {
        Connection conn = null;
        try {
            String path = ".\\db.ini";
            String content;
            content = Files.readString(Path.of(path));


            //String dbURL = "jdbc:sqlserver://SANGU-PC;databaseName=Lyrics";
            //String user = "sa";
            //String pass = "qwer7894";
            //conn = DriverManager.getConnection(dbURL, user, pass);
            System.out.println(content);
            conn = DriverManager.getConnection(content);

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        } /*finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println(ex.getMessage());
            }
        }*/ catch (IOException e) {
            e.printStackTrace();
        }

        return conn;
    }

    /**
     * Method to call Stored procedure and write request and response data to Log table in the database
     */
    public static void writeLog(String requestCountry, String requestGenre, String responseLyrics, String responseArtist,
                                String responseSong) {
        Connection conn = null;
        try {
            DataSource ds = new DataSource();
            conn = ds.getSQLConnection();

            if (conn != null) {

                //https://stackoverflow.com/questions/6113674/how-do-i-execute-a-ms-sql-server-stored-procedure-in-java-jsp-returning-table-d
                String SPsql = "EXEC dbo.usp_WriteLog ?,?,?,?,?";   // for stored proc taking 5 parameters

                PreparedStatement ps = conn.prepareStatement(SPsql);
                ps.setEscapeProcessing(true);
                //ps.setQueryTimeout(<timeout value>);
                ps.setString(1, requestCountry);
                ps.setString(2, requestGenre);
                ps.setString(3, responseArtist);
                ps.setString(4, responseSong);
                ps.setString(5, responseLyrics);
                ps.executeUpdate();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Method to call Stored procedure. Stored procedure checks how much time passed from the last request with
     * same parameters and if needed, returns data from the database.
     */
    public static int getLyricsFromDB(String requestCountry, String requestGenre) {
        Connection conn = null;
        try {
            DataSource ds = new DataSource();
            conn = ds.getSQLConnection();

            if (conn != null) {

                String SPsql = "EXEC dbo.usp_GetData ?,?,?,?,?,?";

                CallableStatement cstmt = conn.prepareCall(SPsql);
                cstmt.setString(1, requestCountry);
                cstmt.setString(2, requestGenre);
                cstmt.registerOutParameter(3, Types.NVARCHAR);
                cstmt.registerOutParameter(4, Types.NVARCHAR);
                cstmt.registerOutParameter(5, Types.NVARCHAR);
                cstmt.registerOutParameter(6, Types.INTEGER);
                cstmt.execute();

                String songArtist = cstmt.getString(3);
                String songTitle = cstmt.getString(4);
                String lyrics = cstmt.getString(5);
                int alreadyExist = Integer.parseInt(cstmt.getString(6));

                alreadyExist = 0;

                if (alreadyExist == 1)
                {
                    System.out.println("RESULT FROM DATABASE");
                    System.out.println(songArtist);
                    System.out.println(songTitle);
                    System.out.println(lyrics);

                    List<String> songInfo = new ArrayList<>();
                    songInfo.add(songArtist);
                    songInfo.add(songTitle);
                    songInfo.add(lyrics);

                    LyricsRepository lr = new LyricsRepository();
                    lr.initData((ArrayList<String>) songInfo);

                    return alreadyExist;
                }
                else
                {
                    return alreadyExist;
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println(ex.getMessage());
            }
        }
        return 0;
    }
}
