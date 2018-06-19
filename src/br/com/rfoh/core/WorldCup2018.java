package br.com.rfoh.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WorldCup2018 {

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url)
			throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(
					new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	public static void main(String[] args) throws JSONException, IOException {
		JSONObject json = readJsonFromUrl(
				"https://api.fifa.com/api/v1/calendar/matches?idseason=254645&idcompetition=17&count=100");
		JSONArray results = (JSONArray) json.get("Results");
		System.out.println("----- RESULTADOS DOS JOGOS -----");

		for (int i = 0; i < results.length(); i++) {
			JSONObject result = (JSONObject) results.get(i);
			Object home = result.get("Home");
			Object away = result.get("Away");
			if (!home.toString().equals("null")
					|| !away.toString().equals("null")) {
				JSONObject homeJson = (JSONObject) home;
				JSONObject awayJson = (JSONObject) away;
				System.out.println(" # JOGO " + String.valueOf(i + 1) + ": "
						+ ((JSONObject) ((JSONArray)homeJson.get("TeamName")).get(0)).get("Description")
						+" "
						+ homeJson.get("Score") + " x "						
						+ awayJson.get("Score")
						+" "
						+ ((JSONObject) ((JSONArray)awayJson.get("TeamName")).get(0)).get("Description"));
			} else {
				System.out.println(" # JOGO " + String.valueOf(i + 1) + ": "
						+ result.get("HomeTeamScore") + " x "						
						+ result.get("AwayTeamScore"));				
			}
		}
	}

}
