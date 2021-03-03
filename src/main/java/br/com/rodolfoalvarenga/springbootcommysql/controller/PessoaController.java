package br.com.rodolfoalvarenga.springbootcommysql.controller;

import br.com.rodolfoalvarenga.springbootcommysql.controller.dto.PessoaRq;
import br.com.rodolfoalvarenga.springbootcommysql.controller.dto.PessoaRs;
import br.com.rodolfoalvarenga.springbootcommysql.model.Pessoa;
import br.com.rodolfoalvarenga.springbootcommysql.repository.PessoaRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {

    private final PessoaRepository pessoaRepository;

    public PessoaController(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @GetMapping("/")
    public List<PessoaRs> findAll() {
        var pessoas = pessoaRepository.findAll();
        return pessoas
                .stream()
                .map(PessoaRs::converter)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PessoaRs findById(@PathVariable("id") Long id) {
        var pessoa = pessoaRepository.getOne(id);
        return PessoaRs.converter(pessoa);
    }

    @PostMapping("/")
    public void savePerson(@RequestBody PessoaRq pessoa) {
        var p = new Pessoa();
        p.setNome(pessoa.getNome());
        p.setSobrenome(pessoa.getSobrenome());
        pessoaRepository.save(p);
    }

    @PutMapping("/{id}")
    public void updatePerson(@PathVariable("id") Long id, @RequestBody PessoaRq pessoa) throws Exception {
        var p = pessoaRepository.findById(id);

        if (p.isPresent()) {
            var pessoaSave = p.get();
            pessoaSave.setNome(pessoa.getNome());
            pessoaSave.setSobrenome(pessoa.getSobrenome());
            pessoaRepository.save(pessoaSave);
        } else {
            throw new Exception("Pessoa não encontrada");
        }
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable("id") Long id) throws Exception {
        var p = pessoaRepository.findById(id);

        if (p.isPresent()) {
            var pessoaSave = p.get();
            pessoaRepository.delete(pessoaSave);
        } else {
            throw new Exception("Pessoa não encontrada");
        }
    }
}
