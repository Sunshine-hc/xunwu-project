package com.match.service.search;

import com.match.ApplicationTests;
import com.match.service.ServiceMultiResult;
import com.match.service.ServiceResult;
import com.match.web.form.RentSearch;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SearchServiceTests extends ApplicationTests {
    @Autowired
    private ISearchService searchService;
    @Test
    public void testIndex(){
        Long targetHouseId = 15L;
        searchService.index(targetHouseId);
    }

    @Test
    public void testRemove(){
        Long targetHouseId = 15L;
        searchService.remove(targetHouseId);
    }

    @Test
    public void testsuggest(){
        ServiceResult<List<String>> result = searchService.suggest("融泽");
        result.getResult().forEach(s -> {
            System.out.println("提示"+s);
        });
    }
    @Test
    public void testQuery(){
        RentSearch rentSearch = new RentSearch();
        rentSearch.setCityEnName("bj");
        rentSearch.setStart(0);
        rentSearch.setSize(10);
        ServiceMultiResult<Long> serviceResult = searchService.query(rentSearch);
        Assert.assertEquals(8,serviceResult.getTotal());
    }
}