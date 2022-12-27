package com.flatlogic.app.ecomerce.controller.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class AutocompleteData {

    private final UUID id;

    private final String label;

}
