package ua.rent.masters.easystay.job;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.rent.masters.easystay.model.Accommodation;
import ua.rent.masters.easystay.model.Booking;
import ua.rent.masters.easystay.service.AccommodationService;
import ua.rent.masters.easystay.service.BookingService;
import ua.rent.masters.easystay.service.NotificationService;

@Component
@RequiredArgsConstructor
public class ScheduledNotification {
    private static final String NO_EXPIRED_BOOKINGS_TODAY = "No expired bookings today";
    private final NotificationService notificationService;
    private final BookingService bookingService;
    private final AccommodationService accommodationService;

    @Scheduled(cron = "0 0 9 * * *")
    public void scheduledNotification() {
        notificationService.sendToAllManagers(getMessage());
    }

    private String getMessage() {
        List<Booking> expiredBookings = bookingService.getExpiredBookings();
        String message = NO_EXPIRED_BOOKINGS_TODAY;
        if (!expiredBookings.isEmpty()) {
            List<Long> accommodationIds =
                    expiredBookings.stream().map(Booking::getAccommodationId).toList();
            Map<Long, Accommodation> releasedAccommodation =
                    accommodationService.findAllByIds(accommodationIds).stream()
                                        .collect(Collectors.toMap(Accommodation::getId, a -> a));
            message = generateMessage(expiredBookings, releasedAccommodation);
        }
        return message;
    }

    private String generateMessage(
            List<Booking> expiredBookings,
            Map<Long, Accommodation> releasedAccommodation
    ) {
        StringBuilder messageBuilder = new StringBuilder();
        for (Booking booking : expiredBookings) {
            Accommodation accommodation =
                    releasedAccommodation.get(booking.getAccommodationId());
            if (accommodation != null) {
                messageBuilder.append("Accommodation ID: ").append(accommodation.getId())
                              .append(System.lineSeparator())
                              .append("Type: ").append(accommodation.getType())
                              .append(System.lineSeparator())
                              .append("Location: ").append(accommodation.getLocation())
                              .append(System.lineSeparator())
                              .append("Rooms: ").append(accommodation.getRooms())
                              .append(System.lineSeparator())
                              .append("Released date: ").append(booking.getCheckOutDate())
                              .append(System.lineSeparator())
                              .append("======================")
                              .append(System.lineSeparator()).append(System.lineSeparator());
            }
        }
        return messageBuilder.toString().trim();
    }
}
