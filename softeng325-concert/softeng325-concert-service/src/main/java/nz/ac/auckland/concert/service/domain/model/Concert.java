package nz.ac.auckland.concert.service.domain.model;

import nz.ac.auckland.concert.common.types.PriceBand;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Entity
public class Concert {

    @Id
    @GeneratedValue
    private Long _id;
    private String _title;
    private Set<LocalDateTime> _dates;
    private Map<PriceBand, BigDecimal> _tariff;
    private Set<Long> _performerIds;

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public Set<LocalDateTime> get_dates() {
        return _dates;
    }

    public Map<PriceBand, BigDecimal> get_tariff() {
        return _tariff;
    }

    public Set<Long> get_performerIds() {
        return _performerIds;
    }

}
