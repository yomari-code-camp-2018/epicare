package com.example.sagar.sicklab;

/**
 * Created by Sagar on 11/18/2017.
 */

public interface ServerIP {
    String IP = "http://10.42.0.164:8000/";
    String register_head = "register/";
    String login_head = "login?";



    String register_name = "name=";
    String register_type = "&type=";
    String register_password = "&password=";
    String register_email = "&email=";


    String login_email = "email=";
    String login_password = "&password=";


    String report_head = "api/post?";
    String report_disease_id = "disease_id=";
    String report_token = "&token=";
    String report_age = "&age=";
    String report_gender = "&gender=";
    String report_location= "&location=";


    String trending_head = "api/home?";
//    String trending_token = "api_token=";
    String trending_district = "&location=";
//    String trending_latitude = "&latitude=";
//    String trending_longitude = "&longitude=";

//    String history_head = "disease/history?";
//    String unverified_head = "disease/unverified?";

    String suggestion_head = "suggestion?";
    String suggestion_text = "text=";
    String suggestion_disease = "&disease=";
    String suggestion_token = "&api_token=";



    String visualization_head="api/graph";


}
