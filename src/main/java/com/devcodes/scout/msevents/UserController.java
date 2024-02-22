package com.devcodes.scout.msevents;


import com.google.cloud.spring.data.firestore.FirestoreReactiveOperations;
import com.google.cloud.spring.data.firestore.FirestoreTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ing. Danilo Montoya Hernandez;
 * Email: danilo9831montoya@gmail.com
 * @version Id: <b>ms-events</b> 20/02/2024, 6:05 PM
 **/
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    private final FirestoreTemplate firestoreTemplate;


    public UserController(UserRepository userRepository, FirestoreTemplate firestoreTemplate) {
        this.userRepository = userRepository;
        this.firestoreTemplate = firestoreTemplate;
    }

    @GetMapping
    private Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/age")
    private Flux<User> getUsersByAge(@RequestParam int age) {
        return userRepository.findByAge(age);
    }

    @GetMapping("/phones")
    private Flux<PhoneNumber> getPhonesByName(@RequestParam String name) {
        return this.firestoreTemplate.withParent(new User(name, 0, null))
                .findAll(PhoneNumber.class);
    }

    @GetMapping("/removeUser")
    private Mono<String> removeUserByName(@RequestParam String name) {
        return this.userRepository.delete(new User(name, 0, null))
                .map(aVoid -> name + "was successfully removed");
    }

    @GetMapping("/removePhonesForUser")
    private Mono<String> removePhonesByName(@RequestParam String name) {
        return this.firestoreTemplate.withParent(new User(name, 0, null))
                .deleteAll(PhoneNumber.class).map(numRemoved -> "successfully removed " + numRemoved + " phone numbers");
    }

    @PostMapping("/saveUser2")
    private Mono<User> saveUser2(@RequestBody User user) {
        FirestoreReactiveOperations userTemplate = this.firestoreTemplate.withParent(user);
        List<PhoneNumber> phones = new ArrayList<>();
        phones.add(new PhoneNumber("3212222"));
        phones.add(new PhoneNumber("5432102"));

        return userTemplate.saveAll(Flux.fromIterable(phones)).then(userRepository.save(user));
    }

    @PostMapping("/saveUser")
    private Mono<User> saveUser(ServerWebExchange serverWebExchange) {
        return serverWebExchange.getFormData()
                .flatMap(formData -> {
                    User user = new User(
                            formData.getFirst("name"),
                            Integer.parseInt(formData.getFirst("age")),
                            createPets(formData.getFirst("pets")));
                    FirestoreReactiveOperations userTemplate = this.firestoreTemplate.withParent(user);
                    List<PhoneNumber> phones = getPhones(formData.getFirst("phones"));
                    return userTemplate.saveAll(Flux.fromIterable(phones)).then(userRepository.save(user));
                });
    }

    private List<PhoneNumber> getPhones(String phones) {
        return phones == null || phones.isEmpty()
                ? Collections.emptyList()
                : Arrays.stream(phones.split(","))
                .map(PhoneNumber::new).collect(Collectors.toList());
    }

    private List<Pet> createPets(String pets) {
        return pets == null || pets.isEmpty()
                ? Collections.emptyList()
                : Arrays.stream(pets.split(","))
                .map(s -> {
                    String[] parts = s.split("-");
                    return new Pet(parts[0], parts[1]);
                }).collect(Collectors.toList());
    }
}
