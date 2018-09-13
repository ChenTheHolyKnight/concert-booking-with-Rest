package nz.ac.auckland.concert.service.services.mapper;

import nz.ac.auckland.concert.common.dto.CreditCardDTO;
import nz.ac.auckland.concert.service.domain.model.CreditCard;
import nz.ac.auckland.concert.service.domain.model.User;

public class CreditCardMapper {
    public static CreditCard ToDomain(CreditCardDTO creditCardDTO, User user){
        return new CreditCard(
          creditCardDTO.getType(),
          creditCardDTO.getName(),
          creditCardDTO.getNumber(),
          creditCardDTO.getExpiryDate(),
          user
        );
    }
}
