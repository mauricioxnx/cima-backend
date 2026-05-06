package com.cima.system.config;

import com.cima.system.entity.Perfil;
import com.cima.system.entity.Utilizador;
import com.cima.system.repository.PerfilRepository;
import com.cima.system.repository.UtilizadorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final PerfilRepository perfilRepository;
    private final UtilizadorRepository utilizadorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedPerfis();
        seedAdminUser();
    }

    private void seedPerfis() {
        List<String[]> perfis = List.of(
                new String[]{"ADMINISTRADOR",      "Acesso total ao sistema"},
                new String[]{"GERENTE_STOCK",      "Gestão de inventário e fornecedores"},
                new String[]{"GERENTE_MANUTENCAO", "Gestão de manutenções e agendamentos"},
                new String[]{"TECNICO",            "Execução e acompanhamento de tarefas de manutenção"}
        );

        for (String[] p : perfis) {
            if (!perfilRepository.existsByNome(p[0])) {
                perfilRepository.save(Perfil.builder()
                        .nome(p[0])
                        .descricao(p[1])
                        .build());
                log.info("Perfil criado: {}", p[0]);
            }
        }
    }

    private void seedAdminUser() {
        if (!utilizadorRepository.existsByEmail("admin@cima.ao")) {
            Perfil adminPerfil = perfilRepository.findByNome("ADMINISTRADOR")
                    .orElseThrow(() -> new RuntimeException("Perfil ADMINISTRADOR não encontrado"));

            utilizadorRepository.save(Utilizador.builder()
                    .nome("Administrador")
                    .email("admin@cima.ao")
                    .senha(passwordEncoder.encode("Admin@2024"))
                    .telefone("+244 900 000 000")
                    .endereco("C.I.M.A – Luanda, Angola")
                    .ativo(true)
                    .perfil(adminPerfil)
                    .build());

            log.info("Utilizador admin criado: admin@cima.ao / Admin@2024");
        }
    }
}
