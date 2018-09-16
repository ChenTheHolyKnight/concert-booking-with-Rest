package nz.ac.auckland.concert.common.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.*;
import java.time.LocalDateTime;

@XmlRootElement(name = "news_dto")
@XmlAccessorType(XmlAccessType.FIELD)
public class NewsDTO {

    @XmlAttribute(name = "id")
    private Long _id;
    @XmlElement(name = "content")
    private String _content;
    @XmlElement(name = "date")
    private LocalDateTime _date;


    protected NewsDTO() {
    }

    public NewsDTO(LocalDateTime date, String content) {
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
        if (!(obj instanceof NewsDTO))
            return false;
        if (obj == this)
            return true;

        NewsDTO rhs = (NewsDTO) obj;
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
