package nz.ac.auckland.concert.service.domain.model;

import nz.ac.auckland.concert.common.types.PriceBand;
import nz.ac.auckland.concert.common.types.SeatNumber;
import nz.ac.auckland.concert.common.types.SeatRow;
import nz.ac.auckland.concert.service.domain.jpa.SeatNumberConverter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * DTO class to represent seats at the concert venue. 
 * 
 * A Seat describes a seat in terms of:
 * _row    the row of the seat.
 * _number the number of the seat.
 *
 */
@Entity
public class Seat {
	@Version
	@Column(nullable = false, name = "version")
	private Long _version=0L;
	@Id
	@GeneratedValue
    @Column(nullable = false)
	private Long _id;
	@Enumerated
    @Column(nullable = false)
	private SeatRow _row;
	@Column(nullable = false)
	@Convert(converter = SeatNumberConverter.class)
	private SeatNumber _number;

    private LocalDateTime _date;
    private PriceBand _seatType;

	@ManyToOne(cascade = CascadeType.ALL)
	private Reservation _reservation;
	
	public Seat() {}
	
	public Seat(SeatRow row, SeatNumber number,PriceBand seatType,Reservation reservation,LocalDateTime date) {
		_row = row;
		_number = number;
		_reservation=reservation;
		_date=date;
		_seatType=seatType;
	}
	
	public SeatRow getRow() {
		return _row;
	}
	
	public SeatNumber getNumber() {
		return _number;
	}

	public Long getId(){
		return _id;
	}

	public Reservation getReservation(){
		return _reservation;
	}

    public LocalDateTime getDate() {
        return _date;
    }

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Seat))
            return false;
        if (obj == this)
            return true;

        Seat rhs = (Seat) obj;
        return new EqualsBuilder().
            append(_row, rhs._row).
            append(_number, rhs._number).
            isEquals();
	}

	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). 
	            append(_row).
	            append(_number).
	            hashCode();
	}
	
	@Override
	public String toString() {
		return _row + _number.toString();
	}
}
