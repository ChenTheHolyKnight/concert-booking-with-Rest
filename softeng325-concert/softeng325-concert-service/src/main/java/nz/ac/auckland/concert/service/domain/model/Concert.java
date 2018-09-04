package nz.ac.auckland.concert.service.domain.model;

import nz.ac.auckland.concert.common.dto.ConcertDTO;
import nz.ac.auckland.concert.common.types.PriceBand;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Entity
public class Concert {

    @Id
    @GeneratedValue
    private Long _id;
    @Column(nullable = false,name = "Title")
    private String _title;
    private Set<LocalDateTime> _dates;
    @ElementCollection
    private Map<PriceBand, BigDecimal> _tariff;


    private Set<Performer> _performers;

    public Concert(){

    }


    public Concert(Long _id, String _title, Set<LocalDateTime> _dates, Map<PriceBand, BigDecimal> _tariff, Set<Performer> performers) {
        this._id = _id;
        this._title = _title;
        this._dates = _dates;
        this._tariff = _tariff;
        this._performers = performers;
    }

    public Long getId() {
        return _id;
    }

    public void setId(Long _id) {
        this._id = _id;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String _title) {
        this._title = _title;
    }

    public Set<LocalDateTime> getDates() {
        return _dates;
    }

    public Map<PriceBand, BigDecimal> getTariff() {
        return _tariff;
    }

    public Set<Performer> getPerformerIds() {
        return _performers;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConcertDTO))
            return false;
        if (obj == this)
            return true;

        Concert rhs = (Concert) obj;
        return new EqualsBuilder().
                append(_title, rhs._title).
                append(_dates, rhs._dates).
                append(_tariff, rhs._tariff).
                //append(_performerIds, rhs._performerIds).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(_title).
                append(_dates).
                append(_tariff).
                //append(_performerIds).
                hashCode();
    }

}
