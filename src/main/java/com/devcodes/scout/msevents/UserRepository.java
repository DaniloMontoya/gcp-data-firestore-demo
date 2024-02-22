package com.devcodes.scout.msevents;

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import reactor.core.publisher.Flux;

/**
 * @author Ing. Danilo Montoya Hernandez;
 * Email: danilo9831montoya@gmail.com
 * @version Id: <b>ms-events</b> 20/02/2024, 6:01 PM
 **/
public interface UserRepository extends FirestoreReactiveRepository<User> {
    Flux<User> findByAge(int age);
}
