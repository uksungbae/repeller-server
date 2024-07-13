package school.kku.repellingserver.member.dto;

public record LoginResponse(
        String name
) {
    public static LoginResponse of(String name) {
        return new LoginResponse(name);
    }
}
