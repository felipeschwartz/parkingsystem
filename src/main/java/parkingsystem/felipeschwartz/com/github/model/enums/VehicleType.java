package parkingsystem.felipeschwartz.com.github.model.enums;

public enum VehicleType {
    CAR(1),
    MOTORCYCLE(2),
    TRUCK(3);

    private final int code;

    VehicleType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static VehicleType fromCode(Integer code) {
        if (code == null) return null;

        for (VehicleType value : VehicleType.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid VehicleType code: " + code);
    }
}
