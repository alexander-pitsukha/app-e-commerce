package com.flatlogic.app.ecomerce.controller.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RowsData<R> {

    List<R> rows;

    int count;

}
