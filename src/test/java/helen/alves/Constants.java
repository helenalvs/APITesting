package helen.alves;

import io.restassured.http.ContentType;

public interface Constants {
    String APP_BASE_URL = "https://automationexercise.com";
    String APP_BASE_PATH = "/api";

    ContentType CONTENT_TYPE = ContentType.JSON;
    Long MAX_TIMEOUT = 1000000L;
}
