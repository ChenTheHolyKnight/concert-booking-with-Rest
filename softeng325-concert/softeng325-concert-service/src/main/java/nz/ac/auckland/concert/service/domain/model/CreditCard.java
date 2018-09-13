package nz.ac.auckland.concert.service.domain.model;

import nz.ac.auckland.concert.common.dto.CreditCardDTO.Type;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * DTO class to represent credit cards. 
 * 
 * A CreditCard describes a credit card in terms of:
 * _type       type of credit card, Visa or Mastercard.
 * _name       the name of the person who owns the credit card.
 * _number     16-digit credit card number. 
 * _expiryDate the credit card's expiry date. 
 *
 */
@Entity
public class CreditCard {

	@Id
	@GeneratedValue
	private long id;
	@Enumerated
	private Type _type;
	private String _name;
	private String _number;
	private LocalDate _expiryDate;

	@OneToOne(cascade = CascadeType.ALL,mappedBy = "_creditCard")
	private User _user;
	
	public CreditCard() {}
	
	public CreditCard(Type type, String name, String number, LocalDate expiryDate,User user) {
		_type = type;
		_name = name;
		_number = number;
		_expiryDate = expiryDate;
		_user = user;
	}
	
	public Type getType() {
		return _type;
	}
	
	public String getName() {
		return _name;
	}
	
	public String getNumber() {
		return _number;
	}

	public LocalDate getExpiryDate() {
		return _expiryDate;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CreditCard))
            return false;
        if (obj == this)
            return true;

        CreditCard rhs = (CreditCard) obj;
        return new EqualsBuilder().
            append(_type, rhs._type).
            append(_name, rhs._name).
            append(_number, rhs._number).
            append(_expiryDate, rhs._expiryDate).
            isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). 
	            append(_type).
	            append(_name).
	            append(_number).
	            append(_expiryDate).
	            hashCode();
	}
}
