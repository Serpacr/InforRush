package com.inforush.config;

import com.inforush.model.Post;
import com.inforush.model.Tag;
import com.inforush.model.User;
import com.inforush.repository.PostRepository;
import com.inforush.repository.TagRepository;
import com.inforush.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataLoader {

    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, TagRepository tagRepository,
                      PostRepository postRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner seedData() {
        return args -> {
            Tag shonen   = upsertTag("Shonen", "shonen");
            Tag isekai   = upsertTag("Isekai", "isekai");
            Tag noticias = upsertTag("Notícias", "noticias");
            Tag classicos = upsertTag("Clássicos", "classicos");
            upsertTag("Seinen", "seinen");
            upsertTag("Lançamentos", "lancamentos");

            if (!userRepository.existsByEmail("admin@inforush.com")) {
                User admin = new User();
                admin.setName("Admin InfoRush");
                admin.setEmail("admin@inforush.com");
                admin.setPassword(passwordEncoder.encode(System.getenv().getOrDefault("ADMIN_PASSWORD", "admin123")));
                admin.setRole(User.Role.ADMIN);
                userRepository.save(admin);

                if (!postRepository.existsBySlug("demon-slayer-nova-temporada")) {
                    Post p1 = new Post();
                    p1.setTitle("Demon Slayer: Nova Temporada Confirmada!");
                    p1.setSlug("demon-slayer-nova-temporada");
                    p1.setContent("A Ufotable confirmou oficialmente a produção de uma nova temporada.\nO arco do Castelo Infinito promete batalhas épicas e animações de altíssima qualidade.\nOs fãs podem esperar estreia ainda esse ano.");
                    p1.setExcerpt("A Ufotable confirma nova temporada de Demon Slayer adaptando o arco do Castelo Infinito.");
                    p1.setPublished(true);
                    p1.setAuthor(admin);
                    p1.setTags(Set.of(shonen, noticias));
                    postRepository.save(p1);
                }

                if (!postRepository.existsBySlug("top-10-animes-isekai")) {
                    Post p2 = new Post();
                    p2.setTitle("Top 10 Animes Isekai de Todos os Tempos");
                    p2.setSlug("top-10-animes-isekai");
                    p2.setContent("Re:Zero, Sword Art Online, Mushoku Tensei, Overlord...\nO gênero Isekai domina as temporadas. Veja nossa lista completa dos 10 melhores!");
                    p2.setExcerpt("Nossa lista definitiva dos 10 melhores animes isekai de todos os tempos.");
                    p2.setPublished(true);
                    p2.setAuthor(admin);
                    p2.setTags(Set.of(isekai));
                    postRepository.save(p2);
                }

                if (!postRepository.existsBySlug("dragon-ball-historia-completa")) {
                    Post p3 = new Post();
                    p3.setTitle("Dragon Ball: A História Completa");
                    p3.setSlug("dragon-ball-historia-completa");
                    p3.setContent("Criado por Akira Toriyama em 1984, Dragon Ball é um fenômeno cultural.\nDe Dragon Ball Z ao Super, a franquia continua influenciando gerações inteiras.");
                    p3.setExcerpt("Uma análise da franquia Dragon Ball desde 1984 até os dias atuais.");
                    p3.setPublished(true);
                    p3.setAuthor(admin);
                    p3.setTags(Set.of(shonen, classicos));
                    postRepository.save(p3);
                }

                System.out.println("✅ Seed concluído! Login: admin@inforush.com / admin123");
            }
        };
    }

    private Tag upsertTag(String name, String slug) {
        return tagRepository.findBySlug(slug).orElseGet(() -> {
            Tag t = new Tag();
            t.setName(name);
            t.setSlug(slug);
            return tagRepository.save(t);
        });
    }
}

