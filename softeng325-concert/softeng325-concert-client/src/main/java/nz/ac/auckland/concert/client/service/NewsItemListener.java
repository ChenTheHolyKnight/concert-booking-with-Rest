package nz.ac.auckland.concert.client.service;

import nz.ac.auckland.concert.common.dto.NewsDTO;

public interface NewsItemListener {
    void newsItemReceived(NewsDTO newsItem);
}
