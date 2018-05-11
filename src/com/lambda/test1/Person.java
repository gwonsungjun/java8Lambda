package com.lambda.test1;

public class Person {
    String firstName;
    String lastName;

    Person() {}

    Person(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return  lastName;
    }
}
