
package com.etapa09.cadastro.controller;

import com.etapa09.cadastro.model.Pessoa;
import com.etapa09.cadastro.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;

@Controller
public class PessoaController {

    @Autowired
    private PessoaRepository repository;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
    // Isso faz com que todo campo de texto vazio ("") seja convertido para null
    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
}
    
    // Redireciona a raiz para a listagem
    @GetMapping("/")
    public String index() {
        return "index";
        //return "redirect:/listagem";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

        
    @GetMapping("/cadastro")
    public String novaPessoa(Model model) {
    Pessoa p = new Pessoa();
    // p.setId(0); // NÃO faça isso, deixe como null
    model.addAttribute("pessoa", p);
    return "cadastro";
}

    // Salva ou Atualiza (o Spring detecta se tem ID)
      
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("pessoa") Pessoa pessoa, 
                     @RequestParam(value = "id", required = false) Integer id,
                     RedirectAttributes attributes) {
    
    // Se o ID vier vazio do formulário, o objeto 'pessoa' 
    // já estará com id = null devido ao binding automático.
    repository.save(pessoa);
    attributes.addFlashAttribute("mensagem", "Dados salvos com sucesso!");
    return "redirect:/listagem";
    }
    
    
    // Lista todas as pessoas
    @GetMapping("/listagem")
    public String listar(Model model) {
        model.addAttribute("pessoas", repository.findAll());
        return "listagem";
    }

    // Carrega dados para edição
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model, RedirectAttributes attributes) {
        return repository.findById(id)
                .map(pessoa -> {
                    model.addAttribute("pessoa", pessoa);
                    return "cadastro";
                })
                .orElseGet(() -> {
                    attributes.addFlashAttribute("erro", "Pessoa não encontrada!");
                    return "redirect:/listagem";
                });
    }

    // Deleta uma pessoa
    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable int id, RedirectAttributes attributes) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            attributes.addFlashAttribute("mensagem", "Pessoa excluída com sucesso!");
        } else {
            attributes.addFlashAttribute("erro", "Erro ao excluir: ID inexistente.");
        }
        return "redirect:/listagem";
    }
}