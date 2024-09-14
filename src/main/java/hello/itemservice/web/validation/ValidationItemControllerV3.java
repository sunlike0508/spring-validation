package hello.itemservice.web.validation;

import java.util.List;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
@Slf4j
public class ValidationItemControllerV3 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    // 해당 컨트롤에 속한 api가 호출될때마다 검증기가 적용된다.
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(itemValidator);
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v3/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v3/addForm";
    }

    //@PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if(!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다"));
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", "가격은 1000원 부터 1000000까지 허용"));
        }

        if(item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", "수량은 9999개까지 입니다"));
        }

        if(item.getQuantity() != null &&  item.getPrice() !=null) {

            if(item.getQuantity() < 9999) {
                int resultPrice = item.getPrice() * item.getQuantity();

                if(resultPrice < 10000) {
                    bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10000원 이상어야 합니다. 현재 값 = " + resultPrice));
                }
            }
        }

        if(bindingResult.hasErrors()) {
            log.info("errors= {}", bindingResult);
            return "validation/v3/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemv3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if(!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수입니다"));
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null,"가격은 1000원 부터 1000000까지 허용"));
        }

        if(item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 9999개까지 입니다"));
        }

        if(item.getQuantity() != null &&  item.getPrice() !=null) {

            if(item.getQuantity() < 9999) {
                int resultPrice = item.getPrice() * item.getQuantity();

                if(resultPrice < 10000) {
                    bindingResult.addError(new ObjectError("item", null, null,"가격 * 수량의 합은 10000원 이상어야 합니다. 현재 값 = " + resultPrice));
                }
            }
        }

        if(bindingResult.hasErrors()) {
            log.info("errors= {}", bindingResult);
            return "validation/v3/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if(!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 10000000},null));
        }

        if(item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
        }

        if(item.getQuantity() != null &&  item.getPrice() !=null) {

            if(item.getQuantity() < 9999) {
                int resultPrice = item.getPrice() * item.getQuantity();

                if(resultPrice < 10000) {
                    bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice},null));
                }
            }
        }

        if(bindingResult.hasErrors()) {
            log.info("errors= {}", bindingResult);
            return "validation/v3/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        log.info(bindingResult.getObjectName());
        log.info("target = {} ", bindingResult.getTarget());

        if(!StringUtils.hasText(item.getItemName())) {
            bindingResult.rejectValue("itemName", "required");
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.rejectValue("price", "range", new Object[]{1000, 10000000},null);
        }

        if(item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        if(item.getQuantity() != null &&  item.getPrice() !=null) {

            if(item.getQuantity() < 9999) {
                int resultPrice = item.getPrice() * item.getQuantity();

                if(resultPrice < 10000) {
                    bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice},null);
                }
            }
        }

        if(bindingResult.hasErrors()) {
            log.info("errors= {}", bindingResult);
            return "validation/v3/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        itemValidator.validate(item, bindingResult);

        if(bindingResult.hasErrors()) {
            log.info("errors= {}", bindingResult);
            return "validation/v3/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }


    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            log.info("errors= {}", bindingResult);
            return "validation/v3/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }

}

