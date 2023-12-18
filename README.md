# the Product Pricing Service
It's a micro-service used to provide a few restful apis for price related.

## How to login the console of H2 database
Once start the SpringBoot application, open a browser with http://localhost:8080/h2-console/login.do and then login with the username/password defined in the application.yml
- note: the database name in the url, my example is "pricedb"

## Unit test Coverage
Ran all test cases under project/src/test/java and got overall coverage:
- Class: 100%, Method: 98%, Line: 98%

## Test Result

mvn clean test; cat target/customReports/result.txt

TEST SUMMARY
- Tests run: 23, Failures: 0, Errors: 0, Skipped: 0
- Tests: 2, Success: 2, Total time: 0.139s

TEST RESULT
- http00.json: Success (0.079s)
- http01.json: Success (0.06s)

## database initialization
Normally, we will prepare schema.sql file and it will be loaded when the SpringBoot startups.
For simplification in the test, I used another approach which is to ask Spring JPA to automatically generate and run DDL based on the definition of Entity class

Usually, we can use Flyway (https://flywaydb.org) (Version control for our database).

## high level solution to get timetable for concrete product
if there are multiple offers are applicable for a given product: say N 
- get all unique dates from start_date and end_date of each offer 
- sort above unique dates
- get all date periods based on above unique dates
- for each date period, go through each offer and apply the price if it is applicable (based on priority, start_date and end_date)
- if any neighbouring periods have the same price, merge them into one.
- see details in the file - flow-for-timetable-offer.jpg

## To be enhanced in the real world
- support pagination for getAllOffers, getTimeTableProduct
- return 404, 400 for related invalid/bad requests

