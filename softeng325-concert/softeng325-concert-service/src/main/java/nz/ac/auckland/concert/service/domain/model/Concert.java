package nz.ac.auckland.concert.service.domain.model;

import nz.ac.auckland.concert.common.dto.ConcertDTO;
import nz.ac.auckland.concert.common.types.PriceBand;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "CONCERTS")
public class Concert {

    @Id
    @GeneratedValue
    private Long _id;

    @Column(nullable = false,name = "Title")
    private String _title;


    @ElementCollection
    @CollectionTable(
            name = "CONCERT_DATES",
            joinColumns = @JoinColumn(name = "CONCERT_ID", nullable = false)
    )
    @Column(nullable = false)
    private Set<LocalDateTime> _dates;

    @ElementCollection
    @MapKeyColumn(name = "PRICE_BAND", table = "CONCERT_TARIFS")
    @MapKeyEnumerated(EnumType.STRING)
    @CollectionTable(
            name = "CONCERT_TARIFS",
            joinColumns = @JoinColumn(name = "CONCERT_ID", nullable = false)
    )
    @Column(nullable = false)
    private Map<PriceBand, BigDecimal> _tariff;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "CONCERT_PERFORMER",
            joinColumns = @JoinColumn(name = "CONCERT_ID", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "PERFORMER_ID", nullable = false)
    )
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

    public Set<Long> getPerformerIds() {
        Set<Long> ids=new HashSet<>();
        _performers.forEach(e->{
            ids.add(e.getId());
        });
        return ids;
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
