package a101.phorest.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoService {

    //private final String redirect_uri = "https://phorest.site";
    private final String redirect_uri = "http://localhost:8399/api/user/kakao/";

    public String getToken(String code,String status) throws IOException {
        //인가코드로 토큰 받기
        String host = "https://kauth.kakao.com/oauth/token";

        String uri = redirect_uri + status;

        String token = "";
        try {

            URL url = new URL(host);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=4656da19556d6f608f3a297dd7c7b994");
            sb.append("&redirect_uri=" + uri);
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("result = " + result);

            // json parsing
            JSONParser parser = new JSONParser();
            JSONObject elem = (JSONObject) parser.parse(result);

            String access_token = elem.get("access_token").toString();
            String refresh_token = elem.get("refresh_token").toString();
            System.out.println("refresh_token = " + refresh_token);
            System.out.println("access_token = " + access_token);

            token = access_token;


            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        return token;
    }

    public Map<String, String> getUserInfo(String access_token) throws IOException {
        // 사용자 정보 가져오기
        String host = "https://kapi.kakao.com/v2/user/me";
        Map<String, String> result = new HashMap<>();
        try {
            URL url = new URL(host);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + access_token);
            urlConnection.setRequestMethod("GET");

            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);


            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String res = "";
            while((line=br.readLine())!=null)
            {
                res+=line;
            }

            System.out.println("res = " + res);


            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(res);
            String id = obj.get("id").toString();

            JSONObject kakao_account = (JSONObject) obj.get("kakao_account");
            //String phone_number = kakao_account.get("phone_number").toString();

            JSONObject profile = (JSONObject) kakao_account.get("profile");
            String nickname = profile.get("nickname").toString();
            String profile_image = profile.get("profile_image_url").toString();

            result.put("id", id);
            result.put("nickname", nickname);
            result.put("profile_image", profile_image);
            //result.put("phone_number", phone_number);
            //result.put("age_range", age_range);

            br.close();


        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String getAgreementInfo(String access_token)
    {
        //  동의 내역 확인하기
        String result = "";
        String host = "https://kapi.kakao.com/v2/user/scopes";
        try{
            URL url = new URL(host);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization", "Bearer "+access_token);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            while((line=br.readLine())!=null)
            {
                result+=line;
            }

            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            // result is json format
            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}