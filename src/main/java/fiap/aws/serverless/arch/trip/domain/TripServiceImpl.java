package fiap.aws.serverless.arch.trip.domain;

import fiap.aws.serverless.arch.common.domain.exception.InvalidSuppliedDataException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TripServiceImpl implements TripService {

    public static final String DATE_FORMAT_PATTERN = "yyyy/MM/dd";

    private static int COUNTRY_MIN_LENGTH = 2;
    private static int COUNTRY_MAX_LENGTH = 35;

    private static int CITY_MIN_LENGTH = 2;
    private static int CITY_MAX_LENGTH = 50;

    private static int REASON_MIN_LENGTH = 10;
    private static int REASON_MAX_LENGTH = 150;

    private final TripRepository tripRepository;

    public TripServiceImpl(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Override
    public void validateCountry(String country) throws InvalidSuppliedDataException {
        if (country == null) {
            throw new InvalidSuppliedDataException("Country is mandatory field for a Trip record.");
        }

        country = country.trim();

        if (country.isEmpty()) {
            throw new InvalidSuppliedDataException("Country is mandatory field, it cannot be empty for a Trip record.");
        }

        int countryLength = country.length();
        if (countryLength < COUNTRY_MIN_LENGTH || countryLength > COUNTRY_MAX_LENGTH) {
            throw new InvalidSuppliedDataException(
                    "Country must be greater than or equals [" + COUNTRY_MIN_LENGTH + "] and less than or equals" +
                            " [" + COUNTRY_MAX_LENGTH + "]"
            );
        }
    }

    @Override
    public void validateCity(String city) throws InvalidSuppliedDataException {
        if (city == null) {
            throw new InvalidSuppliedDataException("City is mandatory field for a Trip record.");
        }

        city = city.trim();

        if (city.isEmpty()) {
            throw new InvalidSuppliedDataException("City is mandatory field, it cannot be empty for a Trip record.");
        }

        int cityLength = city.length();
        if (cityLength < CITY_MIN_LENGTH || cityLength > CITY_MAX_LENGTH) {
            throw new InvalidSuppliedDataException(
                    "City must be greater than or equals [" + CITY_MIN_LENGTH + "] and less than or equals" +
                            " [" + CITY_MAX_LENGTH + "]"
            );
        }
    }

    @Override
    public void validateDate(String date) throws InvalidSuppliedDataException {
        if (date == null) {
            throw new InvalidSuppliedDataException("Date is mandatory field for a Trip record.");
        }

        date = date.trim();

        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN));
        } catch (DateTimeParseException dateTimeParseException) {
            throw new InvalidSuppliedDataException(
                    "Invalid date value [" + date + "], the supplied date must follow the pattern" +
                            " [" + DATE_FORMAT_PATTERN + "]."
            );
        }
    }

    @Override
    public void validateReason(String reason) throws InvalidSuppliedDataException {
        if (reason == null) {
            throw new InvalidSuppliedDataException("Reason is mandatory field for a Trip record.");
        }

        reason = reason.trim();

        if (reason.isEmpty()) {
            throw new InvalidSuppliedDataException("Reason is mandatory field, it cannot be empty for a Trip record.");
        }

        int reasonLength = reason.length();
        if (reasonLength < REASON_MIN_LENGTH || reasonLength > REASON_MAX_LENGTH) {
            throw new InvalidSuppliedDataException(
                    "Reason must be greater than or equals [" + REASON_MIN_LENGTH + "] and less than or equals" +
                            " [" + REASON_MAX_LENGTH + "]"
            );
        }
    }

    @Override
    public Trip save(Trip trip) {
        tripRepository.save(trip);

        return trip;
    }

    @Override
    public void validateQueryStringStartDate(String startDate) throws InvalidSuppliedDataException {
        if (startDate == null) {
            throw new InvalidSuppliedDataException("Query string start date is mandatory.");
        }

        startDate = startDate.trim();

        try {
            LocalDate.parse(startDate, DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN));
        } catch (DateTimeParseException dateTimeParseException) {
            throw new InvalidSuppliedDataException(
                    "Invalid query string start date [" + startDate + "], the supplied value must follow the pattern" +
                            " [" + DATE_FORMAT_PATTERN + "]."
            );
        }
    }

    @Override
    public void validateQueryStringEndDate(String endDate) throws InvalidSuppliedDataException {
        if (endDate == null) {
            throw new InvalidSuppliedDataException("Query string end date is mandatory.");
        }

        endDate = endDate.trim();

        try {
            LocalDate.parse(endDate, DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN));
        } catch (DateTimeParseException dateTimeParseException) {
            throw new InvalidSuppliedDataException(
                    "Invalid query string end date [" + endDate + "], the supplied value must follow the pattern" +
                            " [" + DATE_FORMAT_PATTERN + "]."
            );
        }
    }

    @Override
    public List<Trip> listTripsByPeriod(String startDate, String endDate) {
        return tripRepository.listTripsByPeriod(startDate, endDate);
    }

    @Override
    public List<Trip> listTripsByCountry(String country) {
        return tripRepository.listTripsByCountry(country);
    }

}
