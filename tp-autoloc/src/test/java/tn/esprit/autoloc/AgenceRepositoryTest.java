package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AgenceRepositoryTest {

    @Autowired
    IAgenceRepository agenceRepo;

    @Test
    void testAgenceRepository() {
        System.out.println("\n========== TEST AGENCE REPOSITORY ==========");

        // 1. Créer une instance d'Agence
        Agence agence = new Agence();
        agence.setNom("Agence Tunis Centre");
        agence.setVille("Tunis");
        agence.setAdresse("12 Avenue Habib Bourguiba");
        agence.setTelephone("71000000");

        // 2. save()
        Agence saved = agenceRepo.save(agence);
        System.out.println("Agence sauvegardée : " + saved);
        assertNotNull(saved);
        assertNotNull(saved.getIdAgence());

        // 3. findAll()
        List<Agence> allAgences = agenceRepo.findAll();
        System.out.println("Toutes les agences : " + allAgences);
        assertFalse(allAgences.isEmpty());

        // 4. findById()
        Optional<Agence> found = agenceRepo.findById(saved.getIdAgence());
        System.out.println("findById(" + saved.getIdAgence() + ") : " + found);
        assertTrue(found.isPresent());
        assertEquals(saved.getIdAgence(), found.get().getIdAgence());

        // 5. existsById()
        boolean exists = agenceRepo.existsById(saved.getIdAgence());
        System.out.println("existsById(" + saved.getIdAgence() + ") : " + exists);
        assertTrue(exists);

        // 6. count()
        long count = agenceRepo.count();
        System.out.println("Nombre total d'agences : " + count);
        assertTrue(count > 0);

        // 7. deleteById() puis vérifier
        agenceRepo.deleteById(saved.getIdAgence());
         boolean existsApresDelete = agenceRepo.existsById(saved.getIdAgence());
        System.out.println("existsById après suppression : " + existsApresDelete);
       assertFalse(existsApresDelete);

        System.out.println("========== FIN TEST AGENCE ==========\n");
    }
}
