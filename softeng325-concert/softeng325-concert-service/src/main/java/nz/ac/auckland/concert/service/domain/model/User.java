package nz.ac.auckland.concert.service.domain.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

/**
 * Model class to represent users.
 * 
 * A User describes a user in terms of:
 * _username  the user's unique username.
 * _password  the user's password.
 * _firstname the user's first name.
 * _lastname  the user's family name.
 *
 */
@Entity
public class User {
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false)
	private String _username;
	@Column(nullable = false)
	private String _password;
	private String _firstname;
	private String _lastname;
    @Column(name = "uuid")
	private String _uuid;


    @OneToOne(cascade = CascadeType.ALL)
    private CreditCard _creditCard;
	
	protected User() {}
	
	public User(String username, String password, String lastname, String firstname,String uuid) {
		_username = username;
		_password = password;
		_lastname = lastname;
		_firstname = firstname;
		_uuid=uuid;
	}

	public String getUsername() {
		return _username;
	}
	
	public String getPassword() {
		return _password;
	}
	
	public String getFirstname() {
		return _firstname;
	}
	
	public String getLastname() {
		return _lastname;
	}

	public String getUUID(){
		return _uuid;
	}

    public CreditCard getCreditCard() {
        return _creditCard;
    }

    public void setCreditCard(CreditCard creditCard){
	    _creditCard=creditCard;
    }
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof User))
            return false;
        if (obj == this)
            return true;

        User rhs = (User) obj;
        return new EqualsBuilder().
            append(_username, rhs._username).
            append(_password, rhs._password).
            append(_firstname, rhs._firstname).
            append(_lastname, rhs._lastname).
            isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). 
	            append(_username).
	            append(_password).
	            append(_firstname).
	            append(_password).
	            hashCode();
	}
}
