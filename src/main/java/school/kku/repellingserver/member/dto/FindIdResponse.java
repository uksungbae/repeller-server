package school.kku.repellingserver.member.dto;

public record FindIdResponse(
        String loginId
) {

    public static FindIdResponse of(String loginId) {
        return new FindIdResponse(loginId);
    }
}
