package fiap.aws.serverless.arch.trip.domain;

import fiap.aws.serverless.arch.common.domain.exception.InvalidSuppliedDataException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.class)
public class TripServiceImplTest {

    private static String TODAY = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

    private TripService tripService;

    @Mock
    private TripRepository tripRepository;

    @Before
    public void setUp() throws Exception {
        tripService = new TripServiceImpl(tripRepository);

        doNothing().when(tripRepository).save(any(Trip.class));
    }

    @Test(expected = InvalidSuppliedDataException.class)
    public void check_for_invalid_trip_date() throws InvalidSuppliedDataException {
        tripService.validateDate("2020-07-26");
    }

    @Test
    public void check_for_valid_trip_date() throws InvalidSuppliedDataException {
        tripService.validateDate("2020/07/26");
    }

    @Test
    public void do_save() {
        // given
        Trip tripBaseInstance = new Trip(
                "brasil",
                "goiânia",
                TODAY,
                "getting back to home =]"
        );

        // when
        Trip savedTrip = tripService.save(tripBaseInstance);

        // then
        assertThat(savedTrip, equalTo(tripBaseInstance));
    }
}
