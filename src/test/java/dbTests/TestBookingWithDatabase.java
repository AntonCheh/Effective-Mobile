package dbTests;

import commonDb.dao.BookingDao;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import restApi.client.BookingApiClient;
import restApi.client.base.ApiClients;
import restApi.factory.BookingTestDataFactory;
import restApi.pojo.bookers.Booking;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class TestBookingWithDatabase extends BaseDatabaseTest {

    private BookingApiClient bookingClient;
    private BookingDao bookingDao;

    @BeforeClass
    public void setUp() {
        bookingClient = ApiClients.booking();
        bookingDao = new BookingDao();
    }

    @Test
    public void testCreateBookingAndVerifyInDatabase() throws Exception {
        // 1. Создаем бронирование через API
//        Booking booking = BookingTestDataFactory.aBooking()
//                .firstName("Jane")
//                .lastName("Doe")
//                .totalPrice(2000)
//                .depositPaid(true)
//                .bookingDates(new BookingDates("2025-01-01", "2025-01-10"))
//                .additionalNeeds("la-la")
//                .build();

        Booking booking = BookingTestDataFactory.standardBooking();


        var apiResponse = bookingClient.createBooking(booking);
        System.out.println("Created booking: " + apiResponse.getBookingId());

        int bookingId = apiResponse.getBookingId();

        // 2. Сохраняем в БД
        int dbId = bookingDao.saveBooking(bookingId, booking);
        assertNotNull(dbId, "Should save to database");

        // 3. Проверяем, что данные в БД совпадают
        Booking dbBooking = bookingDao.findBookingByBookingId(bookingId);
        assertNotNull(dbBooking);
        assertEquals(dbBooking.getFirstName(), "Jane");
        assertEquals(dbBooking.getLastName(), "Doe");
        assertEquals(dbBooking.getTotalPrice(), Integer.valueOf(2000));
    }

    @Test
    public void testDatabaseIsolation() throws Exception {
        // Проверяем, что перед тестом БД чистая
        var bookings = bookingDao.getAllBookings();
        assertEquals(bookings.size(), 0, "Database should be empty before test");

        // Добавляем данные
        Booking booking = BookingTestDataFactory.aBooking()
                .firstName("Jane")
                .lastName("Doe")
                .build();

        var response = bookingClient.createBooking(booking);
        bookingDao.saveBooking(response.getBookingId(), booking);

        // Проверяем, что данные сохранились
        bookings = bookingDao.getAllBookings();
        assertEquals(bookings.size(), 1);
    }
}
