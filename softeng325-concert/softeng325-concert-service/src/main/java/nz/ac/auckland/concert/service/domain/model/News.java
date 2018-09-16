package nz.ac.auckland.concert.service.domain.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class News {
    @Id
    @GeneratedValue
    private Long _id;

    private String _content;

    private LocalDateTime _date;


    protected News() {
    }

    public News(LocalDateTime date, String content) {
        _date = date;
        _content = content;
    }

    public Long getId() {
        return _id;
    }

    public LocalDateTime getDate() {
        return _date;
    }

    public void setDate(LocalDateTime date) {
        this._date = date;
    }

    public String getContent() {
        return _content;
    }

    public void setContent(String content) {
        this._content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof News))
            return false;
        if (obj == this)
            return true;

        News rhs = (News) obj;
        return new EqualsBuilder().
                append(_date, rhs._date).
                append(_content, rhs._content).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(_date).
                append(_content).
                hashCode();
    }
}
