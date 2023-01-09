
BASEDIR=$(pwd)
CODEDIR="/Users/schieder/Code/BookStoreManuallyRestified"
STARTUPGRACE=6

## Kills the process running on port 8080, if there is one.
function killApp8080 {

  # Get ID of process running on 8080, if there is one
  PID=$(lsof -ti:8080)

  # If there is a service running, kill it
  if [[ -n "$PID" ]]; then
    kill "$PID"
  fi
}

function restartBackend {
    cd $CODEDIR
    killApp8080
    mvn spring-boot:run >/dev/null 2>/dev/null & 
    sleep $STARTUPGRACE
    cd $BASEDIR
}

echo "All tests, without R verification"
restartBackend
mvn -Dtest= AssortmentTest#testIsbnsGet test
restartBackend
mvn -Dtest=AssortmentTest#testIsbnsIsbnGet test
restartBackend
mvn -Dtest=AssortmentTest#testIsbnsIsbnPut test
restartBackend
mvn -Dtest=StockLocationsTest#testStocklocationsGet test
restartBackend
mvn -Dtest=StockLocationsTest#testStocklocationsStocklocationGet test
restartBackend
mvn -Dtest=StockLocationsTest#testStocklocationsStocklocationIsbnsGet test
restartBackend
mvn -Dtest=StockLocationsTest#testStocklocationsStocklocationIsbnsPost test
restartBackend
mvn -Dtest=CommentsTest#testIsbnsIsbnCommentsGet test
restartBackend
mvn -Dtest=CommentsTest#testIsbnsIsbnCommentsPost test
restartBackend
mvn -Dtest=CommentsTest#testIsbnsIsbnCommentsDelete test
restartBackend
mvn -Dtest=CommentsTest#testIsbnsIsbnCommentsCommentPost test
restartBackend
mvn -Dtest=CommentsTest#testIsbnsIsbnCommentsCommentDelete test


echo "Tests of CUD with R verification"
restartBackend
mvn -Dtest= AssortmentTest#testIsbnsGet test -Dreadverif=true
restartBackend
mvn -Dtest=AssortmentTest#testIsbnsIsbnGet test -Dreadverif=true
restartBackend
mvn -Dtest=AssortmentTest#testIsbnsIsbnPut test -Dreadverif=true
restartBackend
mvn -Dtest=StockLocationsTest#testStocklocationsGet test -Dreadverif=true
restartBackend
mvn -Dtest=StockLocationsTest#testStocklocationsStocklocationGet test -Dreadverif=true
restartBackend
mvn -Dtest=StockLocationsTest#testStocklocationsStocklocationIsbnsGet test -Dreadverif=true
restartBackend
mvn -Dtest=StockLocationsTest#testStocklocationsStocklocationIsbnsPost test -Dreadverif=true
restartBackend
mvn -Dtest=CommentsTest#testIsbnsIsbnCommentsGet test -Dreadverif=true
restartBackend
mvn -Dtest=CommentsTest#testIsbnsIsbnCommentsPost test -Dreadverif=true
restartBackend
mvn -Dtest=CommentsTest#testIsbnsIsbnCommentsDelete test -Dreadverif=true
restartBackend
mvn -Dtest=CommentsTest#testIsbnsIsbnCommentsCommentPost test -Dreadverif=true
restartBackend
mvn -Dtest=CommentsTest#testIsbnsIsbnCommentsCommentDelete test -Dreadverif=true
killApp8080
