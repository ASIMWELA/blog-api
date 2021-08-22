package com.personal.website.payload;

import com.personal.website.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.CollectionModel;

@Builder
@Getter
@AllArgsConstructor
public class PagedResponse {
    PageMetadata pageMetadata;
    CollectionModel<UserDto> data;
}
