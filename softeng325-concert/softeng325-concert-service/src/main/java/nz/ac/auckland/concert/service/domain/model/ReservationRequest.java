package nz.ac.auckland.concert.service.domain.model;

import nz.ac.auckland.concert.common.types.PriceBand;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

/**
 * DTO class to represent reservation requests. 
 * 
 * A ReservationRequest describes a request to reserve seats in terms of:
 * _numberOfSeats the number of seats to try and reserve.
 * _seatType      the priceband (A, B or C) in which to reserve the seats.
 * _concertId     the identity of the concert for which to reserve seats.
 * _date          the date/time of the concert for which seats are to be 
 *                reserved.
 *
 */
@Embeddable
public class ReservationRequest {

	private int _numberOfSeats;
	@Enumerated
	private PriceBand _seatType;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Concert _concert;
	private LocalDateTime _date;
	
	public ReservationRequest() {}
	
	public ReservationRequest(int numberOfSeats, PriceBand seatType, Concert concert, LocalDateTime date) {
		_numberOfSeats = numberOfSeats;
		_seatType = seatType;
		_concert = concert;
		_date = date;
	}
	
	public int getNumberOfSeats() {
		return _numberOfSeats;
	}
	
	public PriceBand getSeatType() {
		return _seatType;
	}
	
	public Concert getConcert() {
		return _concert;
	}
	
	public LocalDateTime getDate() {
		return _date;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ReservationRequest))
            return false;
        if (obj == this)
            return true;

        ReservationRequest rhs = (ReservationRequest) obj;
        return new EqualsBuilder().
            append(_numberOfSeats, rhs._numberOfSeats).
            append(_seatType, rhs._seatType).
            append(_concert, rhs._concert).
            append(_date, rhs._date).
            isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). 
	            append(_numberOfSeats).
	            append(_seatType).
	            append(_concert).
	            append(_date).
	            hashCode();
	}


}
