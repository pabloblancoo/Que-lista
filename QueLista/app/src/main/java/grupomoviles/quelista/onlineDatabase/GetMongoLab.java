package grupomoviles.quelista.onlineDatabase;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Nauce on 17/11/15.
 */
public class GetMongoLab {

    static String server_output = null;
    static String temp_output = null;

    private String GetUrl;

    public GetMongoLab(String GetUrl) {
        this.GetUrl = GetUrl;
    }

    public BasicDBList peticion() {
        try {
            URL url = new URL(GetUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json;charset=UTF-8");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream()), HTTP.UTF_8));

            while ((temp_output = br.readLine()) != null) {
                server_output = temp_output;
            }

            // create a basic db list
            String mongoarray = "{ artificial_basicdb_list: " + server_output + "}";
            Object o = com.mongodb.util.JSON.parse(mongoarray);

            DBObject dbObj = (DBObject) o;

            return (BasicDBList) dbObj.get("artificial_basicdb_list");
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
