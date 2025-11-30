package org.tempsensor.tempsensor.temperatures;

import lombok.Setter;
import org.springframework.data.annotation.Id;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "temperatures")
@Getter
@Setter
public class TempDoc {
    @Id
    private String id;
    private int temp1;
    private int temp2;
    private boolean heaterShouldBeOn;
    private Date timeStamp;

    public TempDoc(String id, int temp1, int temp2, boolean heaterShouldBeOn, Date timeStamp) {
        this.id = id;
        this.temp1 = temp1;
        this.temp2 = temp2;
        this.heaterShouldBeOn = heaterShouldBeOn;
        this.timeStamp = timeStamp;
    }
}
