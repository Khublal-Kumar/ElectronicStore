package com.red.ElectronicStore.helper;

import com.red.ElectronicStore.dto.PageableResponse;
import com.red.ElectronicStore.dto.UserDTO;
import com.red.ElectronicStore.entities.User;
//import org.modelmapper.ModelMapper;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class PageResponseHandler {

    public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type){


        List<U> global = page.getContent();

        List<V> globalDTOList = global.stream().map(object-> new ModelMapper().map(object,type)).collect(Collectors.toList());


        PageableResponse<V> globalDTOPageableResponse = new PageableResponse<>();
        globalDTOPageableResponse.setContent(globalDTOList);
        globalDTOPageableResponse.setLastPage(page.isLast());
        globalDTOPageableResponse.setPageNumber(page.getNumber());
        globalDTOPageableResponse.setPageSize(page.getNumberOfElements());
        globalDTOPageableResponse.setTotalPages(page.getTotalPages());
        globalDTOPageableResponse.setTotalElements(page.getTotalElements());

        return globalDTOPageableResponse;

    }


}
