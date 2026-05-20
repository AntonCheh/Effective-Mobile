package commonDb.dao;

import commonDb.utils.DatabaseManager;
import restApi.pojo.bookers.Booking;
import restApi.pojo.bookers.BookingDates;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO для работы с бронированиями в БД
 */
public class BookingDao {

    /**
     * Сохранить бронирование в БД
     */
    public int saveBooking(int bookingId, Booking booking) throws SQLException {
        String sql = """
            INSERT INTO bookings (booking_id, firstname, lastname, totalprice, 
                                  depositpaid, checkin_date, checkout_date, additionalneeds)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        return DatabaseManager.insertAndGetId(sql,
                bookingId,
                booking.getFirstName(),
                booking.getLastName(),
                booking.getTotalPrice(),
                booking.getDepositPaid(),
                booking.getBookingDates() != null ? booking.getBookingDates().getCheckIn() : null,
                booking.getBookingDates() != null ? booking.getBookingDates().getCheckOut() : null,
                booking.getAdditionalNeeds()
        );
    }

    /**
     * Найти бронирование по ID
     */
    public Booking findBookingByBookingId(int bookingId) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE booking_id = ?";
        ResultSet rs = DatabaseManager.executeQuery(sql, bookingId);

        if (rs.next()) {
            // ✅ Правильный способ создания объекта с Lombok @Data
            Booking booking = new Booking();
            booking.setFirstName(rs.getString("firstname"));
            booking.setLastName(rs.getString("lastname"));
            booking.setTotalPrice(rs.getInt("totalprice"));
            booking.setDepositPaid(rs.getBoolean("depositpaid"));
            booking.setAdditionalNeeds(rs.getString("additionalneeds"));

            // Создаем и заполняем BookingDates
            BookingDates dates = new BookingDates();
            dates.setCheckIn(rs.getString("checkin_date"));
            dates.setCheckOut(rs.getString("checkout_date"));
            booking.setBookingDates(dates);

            return booking;
        }
        return null;
    }

    /**
     * Получить все бронирования из БД
     */
    public List<Booking> getAllBookings() throws SQLException {
        String sql = "SELECT * FROM bookings";
        ResultSet rs = DatabaseManager.executeQuery(sql);

        List<Booking> bookings = new ArrayList<>();
        while (rs.next()) {
            // ✅ Правильный способ создания объекта
            Booking booking = new Booking();
            booking.setFirstName(rs.getString("firstname"));
            booking.setLastName(rs.getString("lastname"));
            booking.setTotalPrice(rs.getInt("totalprice"));
            booking.setDepositPaid(rs.getBoolean("depositpaid"));
            booking.setAdditionalNeeds(rs.getString("additionalneeds"));

            BookingDates dates = new BookingDates();
            dates.setCheckIn(rs.getString("checkin_date"));
            dates.setCheckOut(rs.getString("checkout_date"));
            booking.setBookingDates(dates);

            bookings.add(booking);
        }
        return bookings;
    }

    /**
     * Обновить бронирование
     */
    public int updateBooking(int bookingId, Booking booking) throws SQLException {
        String sql = """
            UPDATE bookings 
            SET firstname = ?, lastname = ?, totalprice = ?, depositpaid = ?, additionalneeds = ?
            WHERE booking_id = ?
        """;

        return DatabaseManager.executeUpdate(sql,
                booking.getFirstName(),
                booking.getLastName(),
                booking.getTotalPrice(),
                booking.getDepositPaid(),
                booking.getAdditionalNeeds(),
                bookingId
        );
    }

    /**
     * Удалить бронирование
     */
    public int deleteBooking(int bookingId) throws SQLException {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";
        return DatabaseManager.executeUpdate(sql, bookingId);
    }

    /**
     * Проверить, существует ли бронирование в БД
     */
    public boolean exists(int bookingId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings WHERE booking_id = ?";
        ResultSet rs = DatabaseManager.executeQuery(sql, bookingId);
        rs.next();
        return rs.getInt(1) > 0;
    }
}