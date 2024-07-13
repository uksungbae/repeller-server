package school.kku.repellingserver.member.dto;

public record LoginRequest(String loginId, String password) {

    public static LoginRequest of(String loginId, String password) {
        return new LoginRequest(loginId, password);
    }
}
