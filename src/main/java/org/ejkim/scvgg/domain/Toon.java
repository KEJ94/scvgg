package org.ejkim.scvgg.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Toon {
    int gamesLastWeek;
    int gatewayId;
    int guid;
    String toon;
}
