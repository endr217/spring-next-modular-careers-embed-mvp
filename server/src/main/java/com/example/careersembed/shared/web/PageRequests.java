package com.example.careersembed.shared.web;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/** Builds a pageable from a 1-based "page" query parameter. */
public final class PageRequests {

  private PageRequests() {}

  public static Pageable of(int page, Sort sort) {
    return PageRequest.of(Math.max(page, 1) - 1, PageResponse.DEFAULT_PER_PAGE, sort);
  }
}
