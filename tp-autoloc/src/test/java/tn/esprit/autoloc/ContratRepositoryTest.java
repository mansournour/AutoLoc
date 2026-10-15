package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.domain.enums.ModePaiement;
import tn.esprit.autoloc.repository.IContratRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ContratRepositoryTest {

    @Autowired
    IContratRepository contratRepo;

    @Test
    void testContratRepository() {
        System.out.println("\n========== TEST CONTRAT REPOSITORY ==========");

        // 1. Créer un contrat (identifiant null → persist INSERT)
        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("1500.00"));
        contrat.setValide(true);

        // 2. save() — INSERT
        Contrat saved = contratRepo.save(contrat);
        System.out.println("Contrat sauvegardé : id=" + saved.getIdContrat()
                + ", montant=" + saved.getMontantTotal());
        assertNotNull(saved);
        assertNotNull(saved.getIdContrat());

        // 3. findById() — doit retourner un Optional non vide
        Optional<Contrat> found = contratRepo.findById(saved.getIdContrat());
        System.out.println("findById présent : " + found.isPresent());
        assertTrue(found.isPresent());
        assertEquals(new BigDecimal("1500.00"), found.get().getMontantTotal());

        // 4. findAll()
        List<Contrat> all = contratRepo.findAll();
        System.out.println("Nombre de contrats en base : " + all.size());
        assertFalse(all.isEmpty());

        // 5. count()
        long count = contratRepo.count();
        System.out.println("count() = " + count);
        assertTrue(count > 0);

        // 6. save() — UPDATE (identifiant renseigné → merge)
        saved.setMontantTotal(new BigDecimal("2000.00"));
        saved.setValide(false);
        Contrat updated = contratRepo.save(saved);
        System.out.println("Contrat après update : montant=" + updated.getMontantTotal()
                + ", valide=" + updated.isValide());
        assertEquals(new BigDecimal("2000.00"), updated.getMontantTotal());
        assertFalse(updated.isValide());

        // 7. existsById()
        assertTrue(contratRepo.existsById(saved.getIdContrat()));

        // 8. saveAndFlush() — écriture immédiate en base
        saved.setMontantTotal(new BigDecimal("2500.00"));
        Contrat flushed = contratRepo.saveAndFlush(saved);
        System.out.println("saveAndFlush montant=" + flushed.getMontantTotal());
        assertEquals(new BigDecimal("2500.00"), flushed.getMontantTotal());

        // 9. deleteById() — cascade REMOVE appliquée sur les paiements
        Long idASupprimer = saved.getIdContrat();
        contratRepo.deleteById(idASupprimer);
        boolean existsApresDelete = contratRepo.existsById(idASupprimer);
        System.out.println("existsById après suppression : " + existsApresDelete);
        assertFalse(existsApresDelete);

        // 10. findById sur id inexistant → Optional vide
        Optional<Contrat> absent = contratRepo.findById(idASupprimer);
        System.out.println("Optional vide après suppression : " + absent.isEmpty());
        assertTrue(absent.isEmpty());

        System.out.println("========== FIN TEST CONTRAT ==========\n");
    }
}
