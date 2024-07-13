package school.kku.repellingserver.farm.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FarmType {
    ONCHARD("과수원"),
    GINGSENG_FIELD("인삼밭"),
    RICE_FARM("벼농장");

    private final String name;
}
