package online.yudream.racecore.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RaceWorld {
    private String name;
    private String desc;
}
