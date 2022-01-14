package eu.kartoffelquadrat.schieder;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

/**
 * All Unit tests for Comments Resources
 */
public class CommentsTest extends RestTestUtils {

    /**
     * Test retrieval of comments for default book
     */
    @Test
    public void testIsbnsIsbnCommentsGet() throws UnirestException {

        // Try to retrieve comments for default book
        HttpResponse<String> comments = Unirest.get(getServiceURL("/isbns/9780739360385/comments")).asString();
        verifyOk(comments);

        // Verify and return comments content
        String body = comments.getBody();
        assert body.contains("A must read!");
        assert body.contains("Amazing book!");
    }

    /**
     * Test adding a comment (is tested on a new book with random ISBN, to avoid state collisions on test re-run)
     */
    @Test
    public void testIsbnsIsbnCommentsPost() throws UnirestException, InterruptedException {

        // Add fake book to operate on (so this test can be repeated)
        String randomIsbn = getRandomIsbn();
        HttpResponse addBookAck = addTestBook(randomIsbn);
        verifyOk(addBookAck);

        // Give the server enough time to actually add the book to the internal state
        //Thread.sleep(1000);

        // Try to add comment
        String commentBody = "So much better than the movie";
        HttpResponse<String> addComment = Unirest.post(getServiceURL("/isbns/"+randomIsbn+"/comments")).body(commentBody).asString();
        verifyOk(addComment);

        // Verify comments for new book.
        HttpResponse<String> comments = Unirest.get(getServiceURL("/isbns/"+randomIsbn+"/comments")).asString();
        assert comments.getBody().contains(commentBody);
    }

}
