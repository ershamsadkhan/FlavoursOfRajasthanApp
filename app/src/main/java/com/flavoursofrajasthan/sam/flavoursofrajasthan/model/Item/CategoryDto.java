package com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SAM on 6/4/2017.
 */

public class CategoryDto {
    public long Categoryid;
    public String CategoryHeader;
    public String CategoryDescription;

    public ArrayList<ItemDto> itemDtoList;

    public CategoryDto() {
        this.itemDtoList = new ArrayList<ItemDto>();
    }
}
