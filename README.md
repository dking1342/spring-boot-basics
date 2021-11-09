# Spring Boot Lesson 1: Building a CRUD API

## Initialize Project
Navigate to [Spring Boot Initializer](https://start.spring.io) to begin the initializer. 

### Generator selections
```aidl
---Project Setup---
Project- Maven
Language- Java
Version- Anything above 2.0

---Project Metadata---
Group- com.example
Artifact- crud
-Options-
Name- crud
Description- CRUD API using Spring Boot
Package Name- com.example.crud
Packaging- Jar
Java- select the version of Java you downloaded

---Dependencies---
Spring Web Starter
```

### Generate project

Generate the project by saving to a location on your local computer. Once saved then extract the project from the zip file and put into folder structer that you will use with your IDE. In this example, we will use IntelliJ as that is the most common type of IDE for Java.

## Exploring Project folder structure
Within the project there will be a few folders and files that will be important when beginning the project. First navigate to <code>pom.xml</code> file. This file shows the project configuration and dependencies. This will be important moving on if you add or edit anything in the file as it will have a direct impact on how your project will function.

Next you can navigate to the <code>src package</code>. This is where you will be writing your code for the project. Navigate to the <code>main/java/com/example/crud</code> and you will see the starter file to run your Spring Boot program. If you open that file and run the server then you will notice that IntelliJ will spin up a server with Tomcat on port <code>8080</code>.

The <code>resources package</code> has <code>static</code> and <code>templates</code> packages that are intended for those respective development constructs. The static folder will hold all the static files needed for the project to run and the templates folder will hold the templates files needed to run. This is when you want to have front end code run through your Java program instead of making the front end separately.

## Setup of Src folder
First, create a few package in the <code>src package</code>. The folders will be: 
* api
* model
* service
* dao.

### API Package
The <code>api package</code> will be for the REST endpoints. This is where the front end will look when making a request to the backend. It will contain the <code>API layer</code> and the <code>Controller layer</code>.

### Service Package
The <code>service package</code> will be for the business logic. This is where the <code>service layer</code> will be located.

### Model Package
The <code>model package</code> will be for the database. This is where the <code>data access layer</code> will be located. This will integrate with the database in how it will be configured. The <code>dao package</code> works alongside the model folder by implementing the <code>data access layer</code>.


## Model Package: Data Access Layer
### Person class
First create a class called <code>Person</code>. This will act as the model for the database.
<br>

```aidl
package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.internal.NotNull;

import java.util.UUID;

public class Person {

    private final UUID id;
    @NotNull
    private final String name;

    public Person(@JsonProperty("id") UUID id,@JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
```

### PersonDao interface
Next go to the <code>dao package</code> and make a new interface called <code>PersonDao</code>. This will be the interface where we will define the operations allowed for the contract for anyone that wants to use the implementation. 
```aidl
package com.example.demo.dao;

import com.example.demo.model.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonDao {

    int insertPerson(UUID id, Person person);

    default int insertPerson(Person person){
        UUID id = UUID.randomUUID();
        return insertPerson(id,person);
    }

    List<Person> getAllPeople();

    Optional<Person> getPersonById(UUID id);

    int deletePersonById(UUID id);

    int updatePersonById(UUID id, Person person);
}
```

### FakePersonAccess class
Create a class called <code>FakePersonAccess</code>. This file implements the code in the <code>PersonDao</code> file. You can then interact with the database in this file. 

```aidl
package com.example.demo.dao;

import com.example.demo.model.Person;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("fakeDao")
public class FakePersonDataAccessService implements PersonDao{

    private static List<Person> DB = new ArrayList<>();

    @Override
    public int insertPerson(UUID id, Person person) {
        DB.add(new Person(id,person.getName()));
        return 1;
    }

    @Override
    public List<Person> getAllPeople() {
        return DB;
    }

    @Override
    public Optional<Person> getPersonById(UUID id) {
        return DB.stream().filter(person -> person.getId().equals(id)).findFirst();
    }

    @Override
    public int deletePersonById(UUID id) {
        Optional<Person> personMaybe = getPersonById(id);

        if(!personMaybe.isPresent()) {
            return 0;
        } else {
            DB.remove(personMaybe.get());
            return 1;
        }
    }

    @Override
    public int updatePersonById(UUID id, Person update) {
        return getPersonById(id).map(person -> {
            int indexOfPersonToUpdate = DB.indexOf(person);
            if(indexOfPersonToUpdate >= 0) {
                DB.set(indexOfPersonToUpdate,new Person(id, update.getName()));
                return 1;
            }
            return 0;
        }).orElse(0);
    }

}
```

### PersonDataAccessService class
If you want to add a different implementation to the database then you can create a new class for that. This could be when you have a different database configuration or type: <code>postgres</code>, <code>mongo</code>, etc.

Create a new class called <code>PersonDataAccessService</code>. This will be the fake implementation for a postgres database. When doing this you will need to make sure that the correct configuration is made in the <code>PersonService</code> class otherwise it will not work.
```aidl
package com.example.demo.dao;

import com.example.demo.model.Person;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class PersonDataAccessService implements PersonDao{
    @Override
    public int insertPerson(UUID id, Person person) {
        return 0;
    }

    @Override
    public List<Person> getAllPeople() {
        List<Person> personList = new ArrayList<>();
        personList.add(new Person(UUID.randomUUID(),"FROM POSTGRES DB"));
        return personList; // need to fix this for the List.of method
    }

    @Override
    public Optional<Person> getPersonById(UUID id) {
        return Optional.empty();
    }

    @Override
    public int deletePersonById(UUID id) {
        return 0;
    }

    @Override
    public int updatePersonById(UUID id, Person person) {
        return 0;
    }
}
```

## Service Package: Service Layer
### PersonService class
Create a class called <code>PersonService</code>. This file will be for the business logic of the <code>Person</code>.


```aidl
package com.example.demo.service;

import com.example.demo.dao.PersonDao;
import com.example.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonService {

    private final PersonDao personDao;
//    fakeDao configuration
//    @Autowired
//    public PersonService(@Qualifier("fakeDao") PersonDao personDao) {
//        this.personDao = personDao;
//    }

//    postgres configuration
    @Autowired
    public PersonService(@Qualifier("postgres") PersonDao personDao) {
        this.personDao = personDao;
    }


    public int insertPerson(Person person){
        return personDao.insertPerson(person);
    }

    public List<Person> getAllPeople() {
        return personDao.getAllPeople();
    }

    public Optional<Person> getPersonById(UUID id) {
        return personDao.getPersonById(id);
    }

    public int deletePerson(UUID id) {
        return personDao.deletePersonById(id);
    }

    public int updatePerson(UUID id, Person person) {
        return personDao.updatePersonById(id, person);
    }
}
```

## API Package: API Layer
### PersonController class
Create a class called <code>PersonController</code>. This add on to the <code>PersonService</code> class by implementing the code there. This will be what content gets rendered from the backend to the front end.
```aidl
package com.example.demo.api;

import com.example.demo.model.Person;
import com.example.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/person")
@RestController
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public void insertPerson(@Validated @NonNull @RequestBody Person person) {
        personService.insertPerson(person);
    }

    @GetMapping
    public List<Person> getAllPeople() {
        return personService.getAllPeople();
    }

    @GetMapping(path = "{id}")
    public Person getPersonById(@PathVariable("id") UUID id) {
        return personService.getPersonById(id).orElse(null);
    }

    @DeleteMapping(path = "{id}")
    public void deletePersonById(@PathVariable("id") UUID id) {
        personService.deletePerson(id);
    }

    @PutMapping(path = "{id}")
    public void updatePersonById(@PathVariable("id") UUID id, @Validated @NonNull @RequestBody Person person) {
        personService.updatePerson(id, person);
    }
}
```




## Flow of backend process
### Step One:
* Create model class
* Add model parameters to the class
* Create an interface for the class
* Add interface parameters
* Create implementation class
* Add content for the implementation interacting with the database
### Step Two:
* Create a service class
* Add content to the service class
### Step Three:
* Create a controller class
* Add content to the controller class (get,post, put, delete, etc)
### Step Four:
* Start server (fix any errors if they are present)
* Open <code>Postman</code> or another similar app
* Test the endpoints by url, method, etc. to ensure the correct actions are taking place
### Next Steps
* Repeat steps one through three depending on scope and project needs

## Additional Resources
### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.6/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.6/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.5.6/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

