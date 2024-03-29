package nz.ac.auckland.concert.service.domain.model;

import nz.ac.auckland.concert.common.types.Genre;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * DTO class to represent performers. 
 * 
 * A Performer describes a performer in terms of:
 * _id         the unique identifier for a performer.
 * _name       the performer's name.
 * _imageName  the name of an image file for the performer.
 * _genre      the performer's genre.
 * _concertIds identification of each concert in which the performer is 
 *             playing. 
 *             
 */
@Entity
@Table(name = "PERFORMERS")
public class Performer {
	@Id
	@GeneratedValue
	private Long _id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Genre _genre;
	@Column(nullable = false)
	private String _imageName;
	@Column(nullable = false)
	private String _name;



	@ManyToMany(mappedBy = "_performers")
	private Set<Concert> _concerts;
	
	public Performer() {}
	
	public Performer(Long id, String name, String imageName, Genre genre, Set<Concert> concerts) {
		_id = id;
		_name = name;
		_imageName = imageName;
		_genre = genre;
		_concerts = new HashSet<Concert>(concerts);
	}
	
	public Long getId() {
		return _id;
	}
	
	public String getName() {
		return _name;
	}
	
	public String getImageName() {
		return _imageName;
	}

	public Genre getGenere(){
		return _genre;
	}
	
	public Set<Long> getConcertIds() {
		Set<Long> ids=new HashSet<>();
		_concerts.forEach(concert -> {
			ids.add(concert.getId());
		});
		return ids;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Performer))
            return false;
        if (obj == this)
            return true;

        Performer rhs = (Performer) obj;
        return new EqualsBuilder().
            append(_name, rhs._name).
            append(_imageName, rhs._imageName).
            append(_genre, rhs._genre).
            //append(_concerts, rhs._concerts).
            isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). 
	            append(_name).
	            append(_imageName).
	            append(_genre).
	            //append(_concerts).
	            hashCode();
	}
}
