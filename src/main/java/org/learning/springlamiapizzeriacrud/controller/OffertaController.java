package org.learning.springlamiapizzeriacrud.controller;

import jakarta.validation.Valid;
import org.learning.springlamiapizzeriacrud.model.Offerta;
import org.learning.springlamiapizzeriacrud.model.Pizza;
import org.learning.springlamiapizzeriacrud.repository.OffertaRepository;
import org.learning.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/offerte")
public class OffertaController {
    @Autowired
    private PizzaRepository pizzaRepository;
    @Autowired
    private OffertaRepository offertaRepository;

    @GetMapping("/create")
    public String create(@RequestParam(name = "pizzaId", required = true) Integer pizzaId, Model model) {
        Optional<Pizza> result = pizzaRepository.findById(pizzaId);
        if (result.isPresent()) {
            Pizza pizzaWithOffert = result.get();
            model.addAttribute("pizza", pizzaWithOffert);
            Offerta newOffert = new Offerta();

            newOffert.setPizza(pizzaWithOffert);
            model.addAttribute("offerta", newOffert);
            return "offerte/create";


        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "pizza with id " + pizzaId + " not found");

        }

    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("offerta") Offerta fromOfferta, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // se ci sono errori ritorno il template del form

            return "offerte/create";
        }


        Offerta storedOfferta = offertaRepository.save(fromOfferta);
        return "redirect:/pizze/show/" + storedOfferta.getPizza().getId();
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        // verifico se il Book è presente su db
        Optional<Offerta> result = offertaRepository.findById(id);
        if (result.isPresent()) {
            // se c'è lo cancello
            offertaRepository.deleteById(id);
            // mando un messaggio di successo con la redirect
            redirectAttributes.addFlashAttribute("redirectMessage",
                    "offerta " + result.get().getTitle() + " deleted!");
            return "redirect:/pizze/show/" + result.get().getPizza().getId();
        } else {
            // se non c'è sollevo un'eccezione
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with di " + id + " not found");
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Optional<Offerta> result = offertaRepository.findById(id);
        if (result.isPresent()) {


            model.addAttribute("offerta", result.get());
            return "offerte/edit";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "offerta with id " + id + " not found");
        }
    }


    @PostMapping("/edit/{id}")
    public String update( @Valid @ModelAttribute("offerta") Offerta fromOfferta, @PathVariable Integer id, BindingResult bindingResult) {
        Optional<Offerta> result = offertaRepository.findById(id);
        if (result.isPresent()) {
            Offerta offertaToEdit = result.get();
            if (bindingResult.hasErrors()) {
                return "/offerte/edit";
            }


            Offerta savedOfferta = offertaRepository.save(fromOfferta);
            return "redirect:/pizze/show/" + result.get().getPizza().getId();

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Offerta with id " + id + " not found");
        }
    }

}
