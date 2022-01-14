package eu.kartoffelquadrat.schieder;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;
import java.lang.Math;
import java.util.Random;

/**
 * All Unit tests for Assortment Resources
 */
public class AssortmentTest
        extends RestTestUtils {

    /**
     * Verify that GET on /isbns returns 200 and expected catalogue. Every resource is covered by exactly one test.
     */
    @Test
    public void testIsbnsGet() throws UnirestException {

        // Try to retrieve catalogue
        HttpResponse<String> catalogue = Unirest.get(getServiceURL("/isbns")).asString();
        verifyOk(catalogue);

        // Verify and return catalogue content
        String body = catalogue.getBody();
        assert body.contains("9780739360385");
        assert body.contains("9780553382563");
        assert body.contains("9781977791122");
        assert body.contains("9780262538473");
    }

    /**
     * Verify that GET on /isbns/{isbn} returns 200 and expected book details
     */
    @Test
    public void testIsbnsIsbnGet() throws UnirestException {

        // Try to retrieve catalogue
        HttpResponse<String> bookDetails = Unirest.get(getServiceURL("/isbns/9780739360385")).asString();
        verifyOk(bookDetails);

        // Verify catalogue content
        assert bookDetails.getBody().contains("priceInCents");
        assert bookDetails.getBody().contains("bookAbstract");
    }

    /**
     * Verify that PUT on /isbns/{isbn} returns 200 and allows adding a book to catalogue. Also verifies the new isbn
     * appears in list and subsequently removes it, to leave server is original state.
     */
    @Test
    public void testIsbnsIsbnPut() throws UnirestException {

        // Using a random ISBN to avoid clash on multiple test run.
        String isbn = Integer.toString(Math.abs(new Random().nextInt()));
        System.out.println(isbn);

        // JSON body for the book to add.
        String body = "{\n" +
                "  \"isbn\": " + isbn + ",\n" +
                "  \"title\": \"The Uninhabitable Earth\",\n" +
                "  \"author\": \"David Wallace-Wells\",\n" +
                "  \"priceInCents\": 2447,\n" +
                "  \"bookAbstract\": \"It is worse, much worse, than you think. The slowness of climate change is a fairy tale, perhaps as pernicious as the one that says it isn’t happening at all, and comes to us bundled with several others in an anthology of comforting delusions: that global warming is an Arctic saga, unfolding remotely; that it is strictly a matter of sea level and coastlines, not an enveloping crisis sparing no place and leaving no life un-deformed.\"\n" +
                "}";

        // Try to add book to backend
        HttpResponse<String> addBookReply = Unirest.put(getServiceURL("/isbns/" + isbn)).header("Content-Type", "application/json; charset=utf-8")
                .body(body).asString();
        verifyOk(addBookReply);

        // Verify catalogue content (must now contain the new book)
        String catalogue = Unirest.get(getServiceURL("/isbns")).asString().getBody();
        assert catalogue.contains(isbn);
    }
}
