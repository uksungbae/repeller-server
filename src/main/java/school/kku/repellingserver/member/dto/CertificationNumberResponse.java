package school.kku.repellingserver.member.dto;

public record CertificationNumberResponse(
        String certificationNumber
) {
    public static CertificationNumberResponse of(String certificationNumber) {
        return new CertificationNumberResponse(certificationNumber);
    }
}
