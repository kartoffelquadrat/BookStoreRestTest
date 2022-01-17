package eu.kartoffelquadrat.schieder;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

/**
 * All Unit tests for StockLocation Resources
 */
public class StockLocationsTest extends RestTestUtils {

    /**
     * Verifies the list of stores listed by default contains the "Lyon" store.
     *
     * @throws UnirestException
     */
    @Test
    public void testStocklocationsGet() throws UnirestException {

        // Try to retrieve comments for default book
        HttpResponse<String> stocklocations = Unirest.get(getServiceURL("/stocklocations")).asString();
        verifyOk(stocklocations);

        // Verify and return comments content
        String body = stocklocations.getBody();
        assert body.contains("Lyon");
    }

    /**
     * Verifies the default stock amount for book 9780739360385 (Harry Potter) is "4".
     *
     * @throws UnirestException
     */
    @Test
    public void testStocklocationsStocklocationGet() throws UnirestException {

        String location = "Lyon";

        // Try to retrieve comments for default book
        HttpResponse<String> stockAmount = Unirest.get(getServiceURL("/stocklocations/" + location)).asString();
        verifyOk(stockAmount);

        // Verify intial stock count for default book
        String[] body = stockAmount.getBody().split(":");
        assert body[0].contains("9780739360385");
        assert body[1].contains("4");
    }


}
