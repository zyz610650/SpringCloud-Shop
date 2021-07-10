package com.changgou.canal.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.changgou.content.feign.*;
import com.changgou.content.pojo.Content;
import com.changgou.entity.Result;
import com.xpand.starter.canal.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address: $
 * @idea:
 */
@CanalEventListener
public class CanalDataEventListener {

    @Autowired
    private ContentFeign contentFeign; //Api里的fein

    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    /***
     * 自定义数据修改监听
     * @param eventType
     * @param rowData
     */
    @ListenPoint(destination = "example", schema = "changgou_content", table = {"tb_content_category", "tb_content"}, eventType = {
            CanalEntry.EventType.UPDATE,
            CanalEntry.EventType.DELETE,
            CanalEntry.EventType.INSERT
    })
    public void onEventCustomUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {

      try{
          String categoryId=getColumnValue(eventType,rowData);
          Result<List<Content>> categoryResult = contentFeign.findByCategory(Long.valueOf(categoryId));
          stringRedisTemplate.boundValueOps("content_"+categoryId).set(JSON.toJSONString(categoryResult.getData()));
      }catch (Exception e)
      {
          e.printStackTrace();
      }

    }

    private String getColumnValue(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        String categoryId="";
        String columnName="category_id";
        if (eventType==CanalEntry.EventType.DELETE){
            for (CanalEntry.Column column:rowData.getBeforeColumnsList())
            {
                if (column.getName().equalsIgnoreCase(columnName))
                {
                    categoryId=column.getValue();
                }
            }
        }else {
            for (CanalEntry.Column column :rowData.getAfterColumnsList())
            {
                if (column.getName().equalsIgnoreCase(columnName))
                {
                    categoryId=column.getValue();
                }
            }
        }
        return categoryId;
    }
}
