package enat.bank.employee;


import lombok.Getter;

@Getter
public enum  Status {

    ACTIVE("A"),
    CLOSED("C");
    private final String code;

    Status(String code) {
        this.code = code;
    }


    public static Status fromCode(String code) {
        for (Status status : Status.values()) {
            if (status.getCode().equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new RuntimeException("Invalid Status code: " + code);
    }
}