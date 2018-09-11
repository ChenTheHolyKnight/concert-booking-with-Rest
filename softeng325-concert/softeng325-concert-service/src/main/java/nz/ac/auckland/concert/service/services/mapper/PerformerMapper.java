package nz.ac.auckland.concert.service.services.mapper;

import nz.ac.auckland.concert.common.dto.ConcertDTO;
import nz.ac.auckland.concert.common.dto.PerformerDTO;
import nz.ac.auckland.concert.service.domain.model.Concert;
import nz.ac.auckland.concert.service.domain.model.Performer;

public class PerformerMapper {
    public static PerformerDTO toDTO(Performer performer){
        return new PerformerDTO(
                performer.getId(),
                performer.getName(),
                performer.getImageName(),
                performer.getGenere(),
                performer.getConcertIds()
        );
    }
}
