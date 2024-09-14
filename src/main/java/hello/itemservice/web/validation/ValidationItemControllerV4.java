package hello.itemservice.web.validation;

import java.util.List;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.web.validation.form.ItemSaveForm;
import hello.itemservice.web.validation.form.ItemUpdateForm;
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
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
@Slf4j
public class ValidationItemControllerV4 {

    private final ItemRepository itemRepository;


    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v4/items";
    }


    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/item";
    }


    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v4/addForm";
    }


    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm itemSaveForm, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if(itemSaveForm.getQuantity() != null && itemSaveForm.getPrice() != null) {

            if(itemSaveForm.getQuantity() < 9999) {
                int resultPrice = itemSaveForm.getPrice() * itemSaveForm.getQuantity();

                if(resultPrice < 10000) {
                    bindingResult.reject("totalPriceMin", new Object[] {10000, resultPrice}, null);
                }
            }
        }

        if(bindingResult.hasErrors()) {
            log.info("errors= {}", bindingResult);
            return "validation/v4/addForm";
        }

        Item item = new Item(itemSaveForm.getItemName(), itemSaveForm.getPrice(), itemSaveForm.getQuantity());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/editForm";
    }


    @PostMapping("/{itemId}/edit")
    public String editV1(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm itemUpdateForm,
            BindingResult bindingResult) {

        if(itemUpdateForm.getQuantity() != null && itemUpdateForm.getPrice() != null) {

            if(itemUpdateForm.getQuantity() < 9999) {
                int resultPrice = itemUpdateForm.getPrice() * itemUpdateForm.getQuantity();

                if(resultPrice < 10000) {
                    bindingResult.reject("totalPriceMin", new Object[] {10000, resultPrice}, null);
                }
            }
        }

        if(bindingResult.hasErrors()) {
            log.info("errors= {}", bindingResult);
            return "validation/v4/editForm";
        }

        Item item = new Item(itemUpdateForm.getItemName(), itemUpdateForm.getPrice(), itemUpdateForm.getQuantity());

        itemRepository.update(itemId, item);
        return "redirect:/validation/v4/items/{itemId}";
    }
}

