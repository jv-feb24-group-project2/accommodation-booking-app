package ua.rent.masters.easystay.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELED,
    EXPIRED;

    @JsonCreator
    public static BookingStatus fromString(String str) {
        for (BookingStatus status : BookingStatus.values()) {
            if (status.name().equalsIgnoreCase(str)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + str);
    }
}
