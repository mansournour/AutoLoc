package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.StatutVehicule;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MaintenanceRepositoryTest {

    @Autowired
    IMaintenanceRepository maintenanceRepo;

    @Autowired
    IVehiculeRepository vehiculeRepo;

    @Test
    void testMaintenanceRepository() {
        System.out.println("\n========== TEST MAINTENANCE REPOSITORY ==========");

        // Préparer un véhicule (relation ManyToOne obligatoire)
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation("TN-999-ZZ");
        vehicule.setMarque("Renault");
        vehicule.setModele("Clio");
        vehicule.setCategorie(CategorieVehicule.CITADINE);
        vehicule.setTarifJournalier(new BigDecimal("60.00"));
        vehicule.setStatut(StatutVehicule.MAINTENANCE);
        Vehicule savedVehicule = vehiculeRepo.save(vehicule);

        // 1. Créer une maintenance
        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(LocalDate.now());
        maintenance.setDateFin(LocalDate.now().plusDays(3));
        maintenance.setDescription("Révision générale + vidange");
        maintenance.setVehicule(savedVehicule);

        // 2. save()
        Maintenance saved = maintenanceRepo.save(maintenance);
        System.out.println("Maintenance sauvegardée : " + saved);
        assertNotNull(saved.getIdMaintenance());

        // 3. findAll()
        List<Maintenance> allMaint = maintenanceRepo.findAll();
        System.out.println("Toutes les maintenances : " + allMaint);
        assertFalse(allMaint.isEmpty());

        // 4. findById()
        Optional<Maintenance> found = maintenanceRepo.findById(saved.getIdMaintenance());
        System.out.println("findById(" + saved.getIdMaintenance() + ") : " + found);
        assertTrue(found.isPresent());

        // 5. existsById()
        boolean exists = maintenanceRepo.existsById(saved.getIdMaintenance());
        System.out.println("existsById : " + exists);
        assertTrue(exists);

        // 6. count()
        long count = maintenanceRepo.count();
        System.out.println("Nombre total de maintenances : " + count);
        assertTrue(count > 0);

        // 7. deleteById()
        maintenanceRepo.deleteById(saved.getIdMaintenance());
        assertFalse(maintenanceRepo.existsById(saved.getIdMaintenance()));
        System.out.println("Maintenance supprimée avec succès.");

        // Nettoyage
        vehiculeRepo.deleteById(savedVehicule.getIdVehicule());

        System.out.println("========== FIN TEST MAINTENANCE ==========\n");
    }
}
