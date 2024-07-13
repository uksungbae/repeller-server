package school.kku.repellingserver.gateway.dto;

public record SerialIdExistResponse(

        boolean isSerialIdExists
) {
    public static SerialIdExistResponse of(boolean isSerialIdExists) {
        return new SerialIdExistResponse(isSerialIdExists);
    }
}
