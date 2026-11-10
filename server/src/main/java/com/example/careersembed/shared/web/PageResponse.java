package com.example.careersembed.shared.web;

import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/** Paginated list in the length-aware paginator format the client expects. */
public record PageResponse<T>(
    int currentPage,
    List<T> data,
    String firstPageUrl,
    Integer from,
    int lastPage,
    String lastPageUrl,
    String nextPageUrl,
    String path,
    int perPage,
    String prevPageUrl,
    Integer to,
    long total) {

  public static final int DEFAULT_PER_PAGE = 10;

  public static <E, T> PageResponse<T> of(Page<E> page, Function<E, T> mapper) {
    String path = ServletUriComponentsBuilder.fromCurrentRequestUri().replaceQuery(null).toUriString();
    int current = page.getNumber() + 1;
    int last = Math.max(page.getTotalPages(), 1);
    List<T> data = page.getContent().stream().map(mapper).toList();
    Integer from = data.isEmpty() ? null : (int) page.getPageable().getOffset() + 1;
    Integer to = data.isEmpty() ? null : (int) page.getPageable().getOffset() + data.size();
    return new PageResponse<>(
        current,
        data,
        pageUrl(path, 1),
        from,
        last,
        pageUrl(path, last),
        current < last ? pageUrl(path, current + 1) : null,
        path,
        page.getSize(),
        current > 1 ? pageUrl(path, current - 1) : null,
        to,
        page.getTotalElements());
  }

  private static String pageUrl(String path, int page) {
    return path + "?page=" + page;
  }
}
