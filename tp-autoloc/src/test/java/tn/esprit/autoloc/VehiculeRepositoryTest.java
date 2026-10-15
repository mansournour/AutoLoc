package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.StatutVehicule;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VehiculeRepositoryTest {

    @Autowired
    IVehiculeRepository vehiculeRepo;

    @Test
    void testVehiculeRepository() {
        System.out.println("\n========== TEST VEHICULE REPOSITORY ==========");

        // 1. Créer un véhicule
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation("TN-123-AB");
        vehicule.setMarque("Toyota");
        vehicule.setModele("Yaris");
        vehicule.setCategorie(CategorieVehicule.CITADINE);
        vehicule.setTarifJournalier(new BigDecimal("75.00"));
        vehicule.setStatut(StatutVehicule.DISPONIBLE);

        // 2. save()
        Vehicule saved = vehiculeRepo.save(vehicule);
        System.out.println("Véhicule sauvegardé : " + saved);
        assertNotNull(saved);
        assertNotNull(saved.getIdVehicule());

        // 3. findAll()
        List<Vehicule> allVehicules = vehiculeRepo.findAll();
        System.out.println("Tous les véhicules : " + allVehicules);
        assertFalse(allVehicules.isEmpty());

        // 4. count()
        long count = vehiculeRepo.count();
        System.out.println("Nombre total de véhicules : " + count);
        assertTrue(count > 0);

        // 5. Modifier une propriété puis save()
        saved.setTarifJournalier(new BigDecimal("90.00"));
        saved.setStatut(StatutVehicule.MAINTENANCE);
        Vehicule updated = vehiculeRepo.save(saved);
        System.out.println("Véhicule après modification : " + updated);
        assertNotEquals(new BigDecimal("75.00"), updated.getTarifJournalier());
        assertEquals(StatutVehicule.MAINTENANCE, updated.getStatut());

        // 6. deleteById()
        vehiculeRepo.deleteById(saved.getIdVehicule());
        boolean existsApresDelete = vehiculeRepo.existsById(saved.getIdVehicule());
        System.out.println("existsById après suppression : " + existsApresDelete);
        assertFalse(existsApresDelete);

        System.out.println("========== FIN TEST VEHICULE ==========\n");
    }
}
