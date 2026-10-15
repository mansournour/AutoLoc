package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.domain.enums.ModePaiement;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IPaiementRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaiementRepositoryTest {

    @Autowired
    IPaiementRepository paiementRepo;

    @Autowired
    IContratRepository contratRepo;

    @Test
    void testPaiementRepository() {
        System.out.println("\n========== TEST PAIEMENT REPOSITORY ==========");

        // 0. Créer un contrat parent (Paiement est ManyToOne → Contrat)
        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("3000.00"));
        contrat.setValide(true);
        Contrat savedContrat = contratRepo.save(contrat);
        System.out.println("Contrat parent créé : id=" + savedContrat.getIdContrat());
        assertNotNull(savedContrat.getIdContrat());

        // 1. Créer un paiement lié au contrat (identifiant null → persist INSERT)
        Paiement paiement = new Paiement();
        paiement.setMontant(new BigDecimal("500.00"));
        paiement.setDatePaiement(LocalDate.now());
        paiement.setModePaiement(ModePaiement.CARTE);
        paiement.setContrat(savedContrat);

        // 2. save() — INSERT
        Paiement saved = paiementRepo.save(paiement);
        System.out.println("Paiement sauvegardé : id=" + saved.getIdPaiement()
                + ", montant=" + saved.getMontant()
                + ", mode=" + saved.getModePaiement());
        assertNotNull(saved);
        assertNotNull(saved.getIdPaiement());

        // 3. findById() — Optional non vide
        Optional<Paiement> found = paiementRepo.findById(saved.getIdPaiement());
        System.out.println("findById présent : " + found.isPresent());
        assertTrue(found.isPresent());
        assertEquals(new BigDecimal("500.00"), found.get().getMontant());
        assertEquals(ModePaiement.CARTE, found.get().getModePaiement());

        // 4. findAll()
        List<Paiement> all = paiementRepo.findAll();
        System.out.println("Nombre de paiements en base : " + all.size());
        assertFalse(all.isEmpty());

        // 5. count()
        long count = paiementRepo.count();
        System.out.println("count() = " + count);
        assertTrue(count > 0);

        // 6. save() — UPDATE (identifiant renseigné → merge SELECT + UPDATE)
        saved.setMontant(new BigDecimal("750.00"));
        saved.setModePaiement(ModePaiement.VIREMENT);
        Paiement updated = paiementRepo.save(saved);
        System.out.println("Paiement après update : montant=" + updated.getMontant()
                + ", mode=" + updated.getModePaiement());
        assertEquals(new BigDecimal("750.00"), updated.getMontant());
        assertEquals(ModePaiement.VIREMENT, updated.getModePaiement());

        // 7. existsById()
        assertTrue(paiementRepo.existsById(saved.getIdPaiement()));

        // 8. saveAndFlush() — écriture immédiate en base
        saved.setMontant(new BigDecimal("1000.00"));
        Paiement flushed = paiementRepo.saveAndFlush(saved);
        System.out.println("saveAndFlush montant=" + flushed.getMontant());
        assertEquals(new BigDecimal("1000.00"), flushed.getMontant());

        // 9. Ajouter un second paiement pour tester saveAll
        Paiement paiement2 = new Paiement();
        paiement2.setMontant(new BigDecimal("200.00"));
        paiement2.setDatePaiement(LocalDate.now());
        paiement2.setModePaiement(ModePaiement.ESPECES);
        paiement2.setContrat(savedContrat);
        List<Paiement> saveAll = paiementRepo.saveAll(List.of(paiement2));
        System.out.println("saveAll → " + saveAll.size() + " paiement(s) insérés");
        assertEquals(1, saveAll.size());
        assertNotNull(saveAll.get(0).getIdPaiement());

        // 10. deleteById() — supprime uniquement le paiement (pas le contrat parent)
        Long idASupprimer = saved.getIdPaiement();
        paiementRepo.deleteById(idASupprimer);
        boolean existsApresDelete = paiementRepo.existsById(idASupprimer);
        System.out.println("existsById après suppression : " + existsApresDelete);
        assertFalse(existsApresDelete);

        // 11. findById sur id inexistant → Optional vide
        Optional<Paiement> absent = paiementRepo.findById(idASupprimer);
        System.out.println("Optional vide après suppression : " + absent.isEmpty());
        assertTrue(absent.isEmpty());

        // Nettoyage : supprimer le second paiement puis le contrat parent
        paiementRepo.deleteById(saveAll.get(0).getIdPaiement());
        contratRepo.deleteById(savedContrat.getIdContrat());
        System.out.println("Nettoyage effectué.");

        System.out.println("========== FIN TEST PAIEMENT ==========\n");
    }
}
