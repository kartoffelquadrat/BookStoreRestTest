package eu.kartoffelquadrat.schieder;

import com.mashape.unirest.http.HttpResponse;

public class RestTestUtils {

    private final String SERVICE_LOCATION = "http://127.0.0.1:8080/bookstore";

    /**
     * Helper method to construct service URI out of provided relative resource location.
     *
     * @param localResource as the relative service resource. Must start with leading slash.
     * @return URI string for the provided local resource.
     */
    protected String getServiceURL(String localResource) {
        return SERVICE_LOCATION + localResource;
    }

    /**
     * Helper method to inspect HttpResponse and ensure return code is 200 (OK)
     * @param response as a previously received HttpResponse object
     */
    protected void verifyOk(HttpResponse<String> response) {

        assert response.getStatus() == 200;
    }
}
