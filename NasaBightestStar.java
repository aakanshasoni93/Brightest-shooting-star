import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import org.json.simple.parser.ParseException;


class NasaBightestStar{
    private JSONArray data;
    public Main() throws IOException, ParseException {
        data = processResponse();
    }

    private JSONObject makeGetCall() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        URL urlForGetRequest = new URL("https://ssd-api.jpl.nasa.gov/fireball.api?date-min=2017-01-01&req-alt=true&sort=energy&req-loc=true");
        String readLine = null;
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
        int responseCode = conection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conection.getInputStream()));
            StringBuilder response = new StringBuilder();
            while ((readLine = in .readLine()) != null) {
                response.append(readLine);
            } in .close();
            return (JSONObject) parser.parse(response.toString());
        } else {
            throw new IOException("Get not worked");
        }
    }

    private JSONArray processResponse() throws IOException, ParseException {
        JSONObject json = makeGetCall();
        return (JSONArray) json.get("data");
    }


    public JSONArray fireball(double lat, double lon){
        JSONArray ar = (JSONArray) data.get(0);
        int n = ar.size();
        String[] ans = new String[n];
        for (int i=data.size()-1; i>0; i--){
            double clat = getLat((JSONArray) data.get(i));
            double clon = getLon((JSONArray) data.get(i));
            if(clat <= lat + 15 && clat >= lat -15 && clon <= lon + 15 && clon >= lon -15){
                return (JSONArray) data.get(i);
            }
        }
        return null;
    }
    private double getLat(JSONArray fireball){
        String lat = (String) fireball.get(3);
        String latd = (String) fireball.get(4);
        if (latd.equals("S")){
            return -1*Double.parseDouble(lat);
        } else {
            return -1*Double.parseDouble(lat);
        }
    }

    private double getLon(JSONArray fireball){
        String lon = (String) fireball.get(5);
        String lond = (String) fireball.get(6);
        if (lond.equals("E")){
            return -1*Double.parseDouble(lon);
        } else {
            return -1*Double.parseDouble(lon);
        }
    }

    public static void main(String[] args) throws ParseException, IOException {
        Main fire = new Main();
        System.out.println(fire.fireball(1,2).toString());
    }
}
