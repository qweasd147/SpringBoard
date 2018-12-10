package com.joo.web.controller;

import com.joo.exception.ValidateException;
import com.joo.model.dto.BoardSearchDto;
import com.joo.service.BoardService;
import com.joo.service.FileService;
import com.joo.web.controller.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1")
@CrossOrigin(origins = "*")
public class BoardController implements BaseController{

    @Autowired
    private BoardService boardService;

    @Autowired
    private FileService fileService;

    @GetMapping("/board")
    public ResponseEntity selectBoardList(@Valid BoardSearchDto searchDto, Pageable pageable, BindingResult br){

        if(br.hasErrors()){
            throw new ValidateException(br, searchDto);
        }

        Map<String, ?> listData = boardService.selectBoardList(searchDto, pageable);

        return successRespResult(listData);
    }
}
