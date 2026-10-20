package fr.miage.projet.exemple.adapter.in.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.JsonPath;

/** Test d'intégration de bout en bout : HTTP → contrôleur → service → JPA → H2 (schéma Flyway). */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional // chaque test est annulé à la fin : les tests restent indépendants
class NoteApiIT {

    @Autowired
    private MockMvc mockMvc;

    private long creerNote(String titre, String contenu) throws Exception {
        String reponse = mockMvc.perform(post("/api/v1/notes").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titre": "%s", "contenu": "%s"}
                                """.formatted(titre, contenu)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/v1/notes/")))
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(reponse, "$.id")).longValue();
    }

    @Test
    void creeUneNoteEtCalculeSesMotsFrequents() throws Exception {
        long id = creerNote("Essai", "pomme poire pomme");

        mockMvc.perform(get("/api/v1/notes/{id}/mots-frequents", id).param("k", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mot").value("pomme"))
                .andExpect(jsonPath("$[0].occurrences").value(2));
    }

    @Test
    void noteInconnueDonne404AuFormatProblemJson() throws Exception {
        mockMvc.perform(get("/api/v1/notes/999999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Note introuvable"));
    }

    @Test
    void kInvalideDonne400() throws Exception {
        long id = creerNote("Essai", "texte");

        mockMvc.perform(get("/api/v1/notes/{id}/mots-frequents", id).param("k", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Donnée invalide"));
    }

    @Test
    void requeteMalFormeeDonne400() throws Exception {
        mockMvc.perform(post("/api/v1/notes").contentType(MediaType.APPLICATION_JSON).content("""
                {"titre": ""}
                """))
                .andExpect(status().isBadRequest());
    }
}
