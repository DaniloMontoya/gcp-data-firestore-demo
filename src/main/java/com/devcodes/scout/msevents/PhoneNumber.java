package com.devcodes.scout.msevents;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ing. Danilo Montoya Hernandez;
 * Email: danilo9831montoya@gmail.com
 * @version Id: <b>ms-events</b> 20/02/2024, 6:12 PM
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumber {
    @DocumentId
    private String number;
}
