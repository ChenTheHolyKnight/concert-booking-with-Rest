package nz.ac.auckland.concert.service.services.mapper;

import nz.ac.auckland.concert.common.dto.SeatDTO;
import nz.ac.auckland.concert.service.domain.model.Reservation;
import nz.ac.auckland.concert.service.domain.model.Seat;

public class SeatMapper {
    public static SeatDTO toDTO(Seat seat){
        return new SeatDTO(
                seat.getRow(),
                seat.getNumber()
        );
    }
}
