#!/bin/bash
## Scripted calls to all unit tests that ensure order is respected.

function testEndpoint {
  mvn -Dtest="$1" test | grep ', Time' | cut -d ":" -f 6
}


# Call all individual tests in required order
echo "BOOKSTORE TEST RESULTS" >> RESULTS
testEndpoint AssortmentTest#testIsbnsGet bookstore
testEndpoint StockLocationsTest#testStocklocationsGet bookstore
testEndpoint CommentsTest#testIsbnsIsbnCommentsGet bookstore
testEndpoint StockLocationsTest#testStocklocationsStocklocationIsbnsGet bookstore
testEndpoint AssortmentTest#testIsbnsIsbnGet bookstore
testEndpoint StockLocationsTest#testStocklocationsStocklocationGet bookstore  

testEndpoint AssortmentTest#testIsbnsIsbnPut bookstore
testEndpoint StockLocationsTest#testStocklocationsStocklocationIsbnsPost bookstore
testEndpoint CommentsTest#testIsbnsIsbnCommentsPost bookstore
testEndpoint CommentsTest#testIsbnsIsbnCommentsDelete bookstore
testEndpoint CommentsTest#testIsbnsIsbnCommentsCommentPost bookstore
testEndpoint CommentsTest#testIsbnsIsbnCommentsCommentDelete bookstore
