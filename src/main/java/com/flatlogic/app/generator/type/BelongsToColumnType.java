package com.flatlogic.app.generator.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BelongsToColumnType {

    AVATAR("avatar"), IMAGE("image");

    private final String type;

}
