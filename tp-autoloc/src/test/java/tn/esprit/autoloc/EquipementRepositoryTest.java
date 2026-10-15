package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.repository.IEquipementRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EquipementRepositoryTest {

    @Autowired
    IEquipementRepository equipementRepo;

    @Test
    void testEquipementRepository() {
        System.out.println("\n========== TEST EQUIPEMENT REPOSITORY ==========");

        // 1. Créer un équipement
        Equipement equip = new Equipement();
        equip.setLibelle("GPS");

        // 2. save()
        Equipement saved = equipementRepo.save(equip);
        System.out.println("Equipement sauvegardé : " + saved);
        assertNotNull(saved.getIdEquipement());
        assertEquals("GPS", saved.getLibelle());

        // 3. findAll()
        List<Equipement> allEquip = equipementRepo.findAll();
        System.out.println("Tous les équipements : " + allEquip);
        assertFalse(allEquip.isEmpty());

        // 4. findById()
        Optional<Equipement> found = equipementRepo.findById(saved.getIdEquipement());
        System.out.println("findById(" + saved.getIdEquipement() + ") : " + found);
        assertTrue(found.isPresent());
        assertEquals("GPS", found.get().getLibelle());

        // 5. existsById() — id existant
        boolean exists = equipementRepo.existsById(saved.getIdEquipement());
        System.out.println("existsById id existant : " + exists);
        assertTrue(exists);

        // existsById() — id inexistant
        boolean existsNon = equipementRepo.existsById(99999L);
        System.out.println("existsById id inexistant (99999) : " + existsNon);
        assertFalse(existsNon);

        // 6. count()
        long count = equipementRepo.count();
        System.out.println("Nombre total d'équipements : " + count);
        assertTrue(count > 0);

        // 7. deleteById()
        equipementRepo.deleteById(saved.getIdEquipement());
        assertFalse(equipementRepo.existsById(saved.getIdEquipement()));
        System.out.println("Equipement supprimé avec succès.");

        System.out.println("========== FIN TEST EQUIPEMENT ==========\n");
    }
}
