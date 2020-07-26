package fiap.aws.serverless.arch.trip.domain;

import fiap.aws.serverless.arch.common.domain.exception.InvalidSuppliedDataException;

public interface TripService {

    void validateCountry(String country) throws InvalidSuppliedDataException;

    void validateCity(String city) throws InvalidSuppliedDataException;

    void validateDate(String date) throws InvalidSuppliedDataException;

    void validateReason(String reason) throws InvalidSuppliedDataException;

    Trip save(Trip trip);

}
