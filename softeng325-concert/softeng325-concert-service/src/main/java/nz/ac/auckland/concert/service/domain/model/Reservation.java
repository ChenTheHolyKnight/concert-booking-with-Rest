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
	@Version
	@Column(nullable = false,name = "version")
	private Long _version=0L;
	@Id
	@GeneratedValue
	@Column(nullable = false)
	private Long _id;
	@Column(nullable = false)
	private ReservationRequest _request;
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "_reservation")
	private Set<Seat> _seats;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private User _user;
	private boolean _isConfirmed;
	private long _expiryTime;


	public Reservation() {}

	public Reservation( ReservationRequest request, Set<Seat> seats,User user,long expiryTime) {
		_request = request;
		_seats = new HashSet<Seat>(seats);
		_user = user;
		_expiryTime=expiryTime;
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

	public boolean getIsConfirmed() {
		return _isConfirmed;
	}

	public void setConfirmed(boolean confirmed) {
		_isConfirmed = confirmed;
	}

    public User getUser() {
        return _user;
    }

    public long getExpiryTime() {
        return _expiryTime;
    }

    public void setExpiryTime(int _expiryTime) {
        this._expiryTime = _expiryTime;
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
