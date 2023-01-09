package eu.kartoffelquadrat.bsresttest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

/**
 * All Unit tests for Comments Resources.
 * @author Maximilian Schiedermeier
 */
public class CommentsTest extends RestTestUtils {

  /**
   * Test retrieval of comments for default book
   */
  @Test
  public void testIsbnsIsbnCommentsGet() throws UnirestException {

    // Try to retrieve comments for default book
    HttpResponse<String> comments =
        Unirest.get(getServiceURL("/isbns/9780739360385/comments")).asString();
    verifyOk(comments);

    // Verify and return comments content
    String body = comments.getBody();
    assert body.contains("A must read!");
    assert body.contains("Amazing book!");
  }

  /**
   * Test adding a comment (is tested on a new book with random ISBN, to avoid state collisions on
   * test re-run)
   */
  @Test
  public void testIsbnsIsbnCommentsPost() throws UnirestException {


    // Try to add comment
    String isbn = "9780739360385";
    String commentBody = "So much better than the movie";
    HttpResponse<String> addComment =
        Unirest.post(getServiceURL("/isbns/" + isbn + "/comments"))
            .header("Content-Type", "application/json").body(commentBody).asString();
    verifyOk(addComment);

    // If Read verification enabled: Verify comments for new book.
    if (RestTestUtils.isReadVerficationsRequested()) {
      HttpResponse<String> comments =
          Unirest.get(getServiceURL("/isbns/" + isbn + "/comments")).asString();
      assert comments.getBody().contains(commentBody);
    } else {
      System.out.println("READ VERIFICATIONS SKIPPED TO REDUCE TEST CROSS DEPENDENCIES.");
    }
  }

  /**
   * Test removing all comments of a book.
   */
  @Test
  public void testIsbnsIsbnCommentsDelete() throws UnirestException {

    String isbn = "9780739360385";

    // delete all comments and verify no more comments are stored
    HttpResponse<String> deleteResponse =
        Unirest.delete(getServiceURL("/isbns/" + isbn + "/comments")).asString();
    verifyOk(deleteResponse);


    // If Read verification enabled: Verify no more comments stored for book
    if (RestTestUtils.isReadVerficationsRequested()) {
      HttpResponse<String> comments =
          Unirest.get(getServiceURL("/isbns/" + isbn + "/comments")).asString();
      verifyOk(comments);
      assert (comments.getBody().equals("{}"));
    } else {
      System.out.println("READ VERIFICATIONS SKIPPED TO REDUCE TEST CROSS DEPENDENCIES.");
    }


  }

  /**
   * Test modifying an existing comment. First creates a new comment for a random book to avoid
   * collisions on test re-execution.
   */
  @Test
  public void testIsbnsIsbnCommentsCommentPost() throws UnirestException {

//    String randomIsbn = ;
    long randomIsbn = Long.parseLong("9780553382563");

    // Try to modify the comment
    // Find comment ID
    HttpResponse<String> comments =
        Unirest.get(getServiceURL("/isbns/" + randomIsbn + "/comments")).asString();
    long commentId = Long.parseLong(comments.getBody().split(":")[0].substring(1).replace("\"", ""));

    // Modify comment
    String newComment = "NEWCOMMENT";
    HttpResponse<String> addCommentReply =
        Unirest.post(getServiceURL("/isbns/" + randomIsbn + "/comments/" + commentId))
            .header("Content-Type", "application/json").body(newComment).asString();
    verifyOk(addCommentReply);


    // If Read verification enabled: Verify comment
    if (RestTestUtils.isReadVerficationsRequested()) {
      HttpResponse<String> updatedCommentReply =
          Unirest.get(getServiceURL("/isbns/" + randomIsbn + "/comments")).asString();
      verifyOk(updatedCommentReply);
      assert updatedCommentReply.getBody().contains(newComment);
    } else {
      System.out.println("READ VERIFICATIONS SKIPPED TO REDUCE TEST CROSS DEPENDENCIES.");
    }

  }

  /**
   * Test removing a specific comment by ID.
   */
  @Test
  public void testIsbnsIsbnCommentsCommentDelete() throws UnirestException {

    // Add a new comment to a book with no comments (requires delete all commnets to be executed first)
    long randomIsbn = Long.parseLong("9780553382563");

    // Try to delete the first comment
    // Find comment ID
    HttpResponse<String> comments =
        Unirest.get(getServiceURL("/isbns/" + randomIsbn + "/comments")).asString();
    long commentId = Long.parseLong(comments.getBody().split(":")[0].substring(1).replace("\"", ""));

    // Actually try to delete it
    HttpResponse<String> deleteCommentReply =
        Unirest.delete(getServiceURL("/isbns/" + randomIsbn + "/comments/" + commentId)).asString();
    verifyOk(deleteCommentReply);


    if (RestTestUtils.isReadVerficationsRequested()) {
      // Verify no more comments stored for book
      HttpResponse<String> commentsAfterDeletion =
          Unirest.get(getServiceURL("/isbns/" + randomIsbn + "/comments")).asString();
      verifyOk(commentsAfterDeletion);
      assert (commentsAfterDeletion.getBody().equals("{}"));
    } else {
      System.out.println("READ VERIFICATIONS SKIPPED TO REDUCE TEST CROSS DEPENDENCIES.");
    }
  }
}
