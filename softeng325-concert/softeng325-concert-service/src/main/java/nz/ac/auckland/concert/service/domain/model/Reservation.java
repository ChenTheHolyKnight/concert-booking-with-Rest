package nz.ac.auckland.concert.service.domain.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * DTO class to represent reservations. 
 * 
 * A Reservation describes a reservation in terms of:
 * _id                 the unique identifier for a reservation.
 * _reservationRequest details of the corresponding reservation request, 
 *                     including the number of seats and their type, concert
 *                     identity, and the date/time of the concert for which a 
 *                     reservation was requested.
 * _seats              the seats that have been reserved (represented as a Set
 *                     of Seat objects).
 *
 */
@Entity
public class Reservation {
	@Id
	@GeneratedValue
	private Long _id;
	private ReservationRequest _request;
	private Set<Seat> _seats;

	public Reservation() {}

	public Reservation(Long id, ReservationRequest request, Set<Seat> seats) {
		_id = id;
		_request = request;
		_seats = new HashSet<Seat>(seats);
	}
	
	public Long getId() {
		return _id;
	}
	
	public ReservationRequest getReservationRequest() {
		return _request;
	}
	
	public Set<Seat> getSeats() {
		return Collections.unmodifiableSet(_seats);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Reservation))
            return false;
        if (obj == this)
            return true;

        Reservation rhs = (Reservation) obj;
        return new EqualsBuilder().
            append(_request, rhs._request).
            append(_seats, rhs._seats).
            isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). 
	            append(_request).
	            append(_seats).
	            hashCode();
	}
}
