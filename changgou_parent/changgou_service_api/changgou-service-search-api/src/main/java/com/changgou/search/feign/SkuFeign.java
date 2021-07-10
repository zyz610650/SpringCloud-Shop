package com.changgou.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@FeignClient(name = "search")
@RequestMapping("/search")
public interface SkuFeign {

    Map search(@RequestParam(required = false) Map searchMap);
}
