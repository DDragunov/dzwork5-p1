package testAPIstep;

import static io.restassured.RestAssured.given;

import static Utils.Configuration.getConfigurationValue;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;


public class StepsRM {
    public int IdPers;
    public int IdLastPers;
    public int IdlastEpisode;
    public String MortyLoc;
    public String MortyRace;
    public String LastPersRace;
    public String LastPersLoc;
    public String LastPersName;

    public void getIdPers(String namePers){

        RequestSpecification request = given();
        request
                .baseUri(Utils.Configuration.getConfigurationValue("url_RM"))
                .header("Content-type","application/json")
        ;
        Response response = request

                .when()
                .get("/character/?name=" +namePers)
                .then()
                .log().all()
                .extract()
                .response();

        IdPers = response.body().jsonPath().get("results[0].id");
    }

    public void getInfoPers(){
        RequestSpecification request = given();
        request
                .baseUri(getConfigurationValue("url_RM"))
                .header("Content-type","application/json")
        ;
        Response response = request
                .when()
                .get("/character/" +IdPers)
                .then()
                .log().all()
                .extract()
                .response();

        MortyLoc=new JSONObject(response.getBody().asString()).getJSONObject("location").get("name").toString();
        MortyRace=new JSONObject(response.getBody().asString()).get("species").toString();
        int episode = new JSONObject(response.getBody().asString()).getJSONArray("episode").length()-1;
        IdlastEpisode = Integer.parseInt(new JSONObject(response.getBody().asString()).getJSONArray("episode").get(episode).toString().replaceAll("[^0-9]",""));

    }

    public void getLastPers(){
        RequestSpecification request = given();
        request
                .baseUri(getConfigurationValue("url_RM"))
                .header("Content-type","application/json")
        ;
        Response response = request
                .when()
                .get("/episode/" +IdlastEpisode)
                .then()
                .log().all()
                .extract()
                .response();
        int arrayIdlastPers = new JSONObject(response.getBody().asString()).getJSONArray("characters").length()-1;
        IdLastPers = Integer.parseInt(new JSONObject(response.getBody().asString()).getJSONArray("characters").get(arrayIdlastPers).toString().replaceAll("[^0-9]",""));
    }

    public void getLastPersInfo(){
        RequestSpecification request = given();
        request
                .baseUri(getConfigurationValue("url_RM"))
                .header("Content-type","application/json")
        ;
        Response response = request
                .when()
                .get("/character/" +IdLastPers)
                .then()
                .log().all()
                .extract()
                .response();
        LastPersRace=new JSONObject(response.getBody().asString()).get("species").toString();
        LastPersLoc=new JSONObject(response.getBody().asString()).getJSONObject("location").get("name").toString();
    }

    //Проверки
    public void AssertLocPers()
    {
        Assertions.assertEquals(MortyLoc,LastPersLoc,"Локации не совпадают.");
    }
    public void AssertRacePers()
    {
        Assertions.assertEquals(MortyRace,LastPersRace,"Расы персонажей не идентичны.");
    }
}
