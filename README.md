#Book recommendation API

##Task description

Provide a simple book recommendation service.
The service should be capable of providing a list of 20 recommendations for a given user. A book recommendation is based on user’s feedback. This feedback can either be:

 - Liked
 - Disliked

###Requirements:

 - The following ​ books dataset​ should be used
 - The list of recommendations should contain exactly 20 entries (if possible)

###In particular we’ll be looking at:
 - Code quality + architecture
 - API design
 - Testing


##REST API description

GET /books/recommended - returns 20 books mostly similar to ones `liked` by user; if there're not enough books similar to `liked` ones, fulfill the list by `neutral` books if possible; does not show books similar to `disliked` ones

Response format:
'[
  {
    "id":<id>,
    "title":<title>,
    "author":<author_title>,
    "genre":<genre_title>,
    "assess":<assess_value>
  },
...
]'

TODO: consider to implement `weighted` recommendations based not only on current user's assessments but also on other users ones


GET /books/all?p=<page_number>&l=<page_length>&by=<order_by>&order=<order>&filter=<filter_expr> - returns paginated list of all books

Response format is similar to recommended book list


POST /book/like -d "{"id":<book_id>}" - like book; if it's already disliked, change the user's assess to `liked`

Response is empty if HTTP status is OK and is '{"error":<error>}'


DELETE /book/like -d "{"id":<book_id>}" - unlike book; if it's already liked or disliked, change the user's assess to `neutral`

Response is similar to `like` call response


POST /book/dislike -d "{"id":<book_id>}" - dislike book; if it's already liked, change the user's assess to `disliked`

Response is similar to `like` call response


DELETE /book/dislike -d "{"id":<book_id>}" - undislike book; if it's already liked or disliked, change the user's assess to `neutral`

Response is similar to `like` call response



##Database description

user(id varchar(100), name varchar(150))

book(id varchar(100), title varchar(250), author_id varchar(100) genre_id varchar(100)

author(id varchar(100), title varchar(250))

genre(id varchar(100), title varchar(250))

assess(id varchar(100), value smallint, book_id varchar(100), user_id varchar(100))


##Prerequisites

  - Java 11
  - Maven 3
  - Docker


```
( [[ -z `docker volume list | grep booksvolume` ]] && echo 'will create booksvolume' && docker volume create booksvolume && docker volume list ) ; \
echo 'Continuing' ; \
docker run --rm -d --name=booksdb -e "POSTGRES_DB=booksdb" -e "POSTGRES_USER=dbuser" -e "POSTGRES_PASSWORD=dbpassword" -p 5432:5432 -v booksvolume:/var/lib/postgresql/data postgres:11.1
```

###For debugging and testing purposes only

`docker run --rm -it --name=psql postgres:11.1 psql -h 172.17.0.1 -U dbuser -d booksdb`

`SELECT set_config('log_statement', 'all', true);`

`create database "booksdb-test" owner dbuser encoding 'UTF8';`


##Test

`mvn test`

TODO: increase services coverage, add integration tests


##Build

`mvn package`


##Run

`java -jar ./target/books-0.0.1-SNAPSHOT.jar`

##Test Manually


`curl -iv -X GET -H 'user: 1' http://127.0.0.1:8080/books/recommended`

`curl -iv -X GET -H 'user: 2' http://127.0.0.1:8080/books/recommended`

`curl -iv -X POST -H 'user: 1' -H 'Content-type: application/json' http://127.0.0.1:8080/book/like -d '{"id":528994972,"assess":"dislike"}'`

`curl -iv -X GET -H 'user: 1' http://127.0.0.1:8080/books/recommended`

(book 528994972 disappears from recommendations)

`curl -iv -X DELETE -H 'user: 1' -H 'Content-type: application/json' http://127.0.0.1:8080/book/like -d '{"id":528994972,"assess":"dislike"}'`

(book 528994972 appears in recommendations again)


or use swagger UI at http://localhost:8080/swagger-ui.html