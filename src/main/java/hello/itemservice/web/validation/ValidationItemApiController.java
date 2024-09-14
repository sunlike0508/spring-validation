package hello.itemservice.web.validation;

import hello.itemservice.web.validation.form.ItemSaveForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/validation/api/items")
@RequiredArgsConstructor
@Slf4j
public class ValidationItemApiController {

    @PostMapping("/add")
    public Object addItem(@Validated @RequestBody ItemSaveForm form, BindingResult bindingResult) {

        log.info("API 컨트롤러 호출");

        if(bindingResult.hasErrors()) {

            log.info("검증 오류 발생 ={}", bindingResult);
            return bindingResult.getAllErrors();
        }

        log.info("성공");

        return form;
    }
}

