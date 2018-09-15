package nz.ac.auckland.concert.service.services.mapper;

import nz.ac.auckland.concert.common.dto.SeatDTO;
import nz.ac.auckland.concert.service.domain.model.Seat;

public class SeatMapper {
    public static Seat toDomain(SeatDTO seatDTO){
        return new Seat(
                seatDTO.getRow(),
                seatDTO.getNumber()
        );
    }
}
