package lt.viko.eif.ljurevicius.lt.viko.eif.ljurevicius.student.LyricsWS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.sql.*;

/**
 * This is main class to run a program
 * This class initiates Spring boot application

 Project contains these classes:

 *  {@link lt.viko.eif.ljurevicius.lt.viko.eif.ljurevicius.student.LyricsWS.DataSource} gets data either from
 * database if it already exist or from Shazam and lyrics Web APIs,
 *  {@link lt.viko.eif.ljurevicius.lt.viko.eif.ljurevicius.student.LyricsWS.LyricsEndpoint} class for Webservice endpoint
 *  {@link lt.viko.eif.ljurevicius.lt.viko.eif.ljurevicius.student.LyricsWS.LyricsRepository} class which generates
 * result to Webservice request
 *  {@link lt.viko.eif.ljurevicius.lt.viko.eif.ljurevicius.student.LyricsWS.WebServiceConfig} enables soap webservice features
 *
 * @author Linas Jurevičius PI20S VIKO EIF
 *
 */

@SpringBootApplication
public class ProducingWebServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProducingWebServiceApplication.class, args);

		/*Connection conn = null;
		try {
			DataSource ds = new DataSource();
			conn = ds.getSQLConnection();

			if (conn != null) {
				DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
				System.out.println("Driver name: " + dm.getDriverName());
				System.out.println("Driver version: " + dm.getDriverVersion());
				System.out.println("Product name: " + dm.getDatabaseProductName());
				System.out.println("Product version: " + dm.getDatabaseProductVersion());

				String param1 = "DE";
				String param2 = "ROCK";

				String SPsql = "EXEC dbo.usp_WriteLog ?,?";   // for stored proc taking 2 parameters
				//Connection con = SmartPoolFactory.getConnection();   // java.sql.Connection
				PreparedStatement ps = conn.prepareStatement(SPsql);
				ps.setEscapeProcessing(true);
				//ps.setQueryTimeout(<timeout value>);
				ps.setString(1, param1);
				ps.setString(2, param2);
				//ResultSet rs = ps.executeQuery();
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
		}*/
	}
}
