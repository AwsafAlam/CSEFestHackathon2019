package io.github.utshaw.myhealth.remote;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "http://api.kolpobd.com/v1/index.php/userlogin";
    public static final String BASE_URL_HR = "http://api.kolpobd.com/v1/index.php/";
//    public static final String BASE_URL = "http://api.kolpobd.com/v1/index.php/updateuser";


    public static APIService getAPIService() {
//        return RetrofitClient.getClient().create(APIService.class);

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}