package parkingsystem.felipeschwartz.com.github.model.enums;

public enum VehicleType {
    CAR((short) 1),
    MOTORCYCLE((short) 2),
    TRUCK((short) 3);

    private final short code;

    VehicleType(short code) {
        this.code = code;
    }

    public short getCode() {
        return code;
    }

    public static VehicleType fromCode(Short code) {
        if (code == null) return null;

        for (VehicleType vt : values()) {
            if (vt.code == code) return vt;
        }
        throw new IllegalArgumentException("VehicleType code inválido: " + code);
    }
}