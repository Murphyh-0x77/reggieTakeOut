package com.liutao.reggie.dto;

import com.liutao.reggie.entity.Setmeal;
import com.liutao.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
