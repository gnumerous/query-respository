package qrm.repository;

import lombok.Data;
import qrm.repository.annotation.Field;
import qrm.repository.annotation.DataStore;

@Data
@DataStore(name = "employee")
public class Employee {

    @Field(name = "id", primaryKey = true)
    private int id;

    @Field(name = "first_name")
    private String firstName;

    @Field(name = "last_name")
    private String lastName;
}
