package com.devcodes.scout.msevents;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ing. Danilo Montoya Hernandez;
 * Email: danilo9831montoya@gmail.com
 * @version Id: <b>ms-events</b> 20/02/2024, 6:02 PM
 **/
@AllArgsConstructor
@Data
@Document(collectionName = "users")
public class User {
    @DocumentId
    String name;

    int age;

    List<Pet> pets;

    User() {
        pets = new ArrayList<>();
    }

}
