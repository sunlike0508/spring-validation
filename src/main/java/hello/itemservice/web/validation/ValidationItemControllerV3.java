package hello.itemservice.web.validation;

import java.util.List;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String addItemV1(@Validated @ModelAttribute Item item, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if(item.getQuantity() != null && item.getPrice() != null) {

            if(item.getQuantity() < 9999) {
                int resultPrice = item.getPrice() * item.getQuantity();

                if(resultPrice < 10000) {
                    bindingResult.reject("totalPriceMin", new Object[] {10000, resultPrice}, null);
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


    @PostMapping("/add")
    public String addItemV2(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if(item.getQuantity() != null && item.getPrice() != null) {

            if(item.getQuantity() < 9999) {
                int resultPrice = item.getPrice() * item.getQuantity();

                if(resultPrice < 10000) {
                    bindingResult.reject("totalPriceMin", new Object[] {10000, resultPrice}, null);
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


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/editForm";
    }


    //@PostMapping("/{itemId}/edit")
    public String editV1(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult) {

        if(item.getQuantity() != null && item.getPrice() != null) {

            if(item.getQuantity() < 9999) {
                int resultPrice = item.getPrice() * item.getQuantity();

                if(resultPrice < 10000) {
                    bindingResult.reject("totalPriceMin", new Object[] {10000, resultPrice}, null);
                }
            }
        }

        if(bindingResult.hasErrors()) {
            log.info("errors= {}", bindingResult);
            return "validation/v3/editForm";
        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }


    @PostMapping("/{itemId}/edit")
    public String editV2(@PathVariable Long itemId, @Validated(UpdateCheck.class) @ModelAttribute Item item,
            BindingResult bindingResult) {

        if(item.getQuantity() != null && item.getPrice() != null) {

            if(item.getQuantity() < 9999) {
                int resultPrice = item.getPrice() * item.getQuantity();

                if(resultPrice < 10000) {
                    bindingResult.reject("totalPriceMin", new Object[] {10000, resultPrice}, null);
                }
            }
        }

        if(bindingResult.hasErrors()) {
            log.info("errors= {}", bindingResult);
            return "validation/v3/editForm";
        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }
}

