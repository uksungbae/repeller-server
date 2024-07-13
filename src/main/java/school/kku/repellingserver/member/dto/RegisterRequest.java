package school.kku.repellingserver.member.dto;

public record RegisterRequest(
        String loginId,
        String password,
        String name,
        String email
) {

    public static RegisterRequest of(String loginId, String password, String name, String email) {
        return new RegisterRequest(loginId, password, name, email);
    }
}
