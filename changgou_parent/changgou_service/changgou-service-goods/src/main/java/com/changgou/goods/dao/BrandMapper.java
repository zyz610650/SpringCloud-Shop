package com.changgou.goods.dao;
import com.changgou.goods.pojo.Brand;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:shenkunlin
 * @Description:Brand的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface BrandMapper extends Mapper<Brand> {

    /**
     * 查询分类对应的品牌集合
     * @param categoryId
     * @return
     */
    @Select("select * from tb_brand t1,tb_category_brand t2 where t1.id=t2.brand_id and t2.category_id=#{categoryId}")
    List<Brand> findByCategory(Integer categoryId);
}
