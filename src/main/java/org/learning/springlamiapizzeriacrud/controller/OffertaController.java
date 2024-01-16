package org.learning.springlamiapizzeriacrud.controller;

import org.learning.springlamiapizzeriacrudrelazioni.model.Offerta;
import org.learning.springlamiapizzeriacrudrelazioni.model.Pizza;
import org.learning.springlamiapizzeriacrudrelazioni.repository.OffertaRepository;
import org.learning.springlamiapizzeriacrudrelazioni.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/offert")
public class OffertaController {
    @Autowired
    private PizzaRepository pizzaRepository;
    @Autowired
    private OffertaRepository offertaRepository;
    @GetMapping("/create")
    public String create(@RequestParam(name = "pizzaId", required = true) Integer pizzaId, Model model){
        Optional<Pizza> result= pizzaRepository.findById(pizzaId);
        if (result.isPresent()) {
            Pizza pizzaWithOffert= result.get();
            model.addAttribute("pizza", pizzaWithOffert);
            Offerta newOffert= new Offerta();

            newOffert.setPizza(pizzaWithOffert);
            newOffert.setStartDate(LocalDate.now());
            newOffert.setEndDate(LocalDate.now().plusDays(30));
            model.addAttribute("offerta", newOffert);
            return "offerte/create";


        }else {throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "pizza with id " + pizzaId + " not found");

        }

    }
    @PostMapping("/create")
    public String store(Offerta fromOfferta){
        Offerta storedOfferta = offertaRepository.save(fromOfferta);
        return "redirect:/pizz/show/"+ storedOfferta.getPizza().getId();
    }

}
