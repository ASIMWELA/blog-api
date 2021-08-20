package com.personal.website.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.CollectionModel;

import java.util.Collection;

@Builder
@Getter
@AllArgsConstructor
public class PagedResponse {
    PageMetadata pageMetadata;
    CollectionModel<?> data;
}
