package org.ejkim.scvgg.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    String battleTag;
    List<Toon> toons;
    List<GameResult> gameResults;
    HashMap<String, HashMap<String,Object>> avatarsFramed;
    List<Profile> Profiles;
}
