package tn.esprit.autoloc;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.domain.enums.*;
import tn.esprit.autoloc.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CrudDemo implements CommandLineRunner {

    private final IAgenceRepository agenceRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IClientRepository clientRepository;
    private final IContratRepository contratRepository;
    private final IPaiementRepository paiementRepository;

    @Override
    public void run(String... args) {
        System.out.println("\n========================================");
        System.out.println("        DEMO CRUD - AutoLoc");
        System.out.println("========================================\n");

        demoAgence();
        demoVehicule();
        demoClient();
        demoContrat();
    }

    // ─── AGENCE ───────────────────────────────────────────────────────────────
    private void demoAgence() {
        System.out.println("─── AGENCE ──────────────────────────────");

        // CREATE
        Agence agence = new Agence();
        agence.setNom("Agence Tunis Centre");
        agence.setVille("Tunis");
        agence.setAdresse("Avenue Habib Bourguiba");
        agence.setTelephone("71000001");
        agence = agenceRepository.save(agence);
        System.out.println("[CREATE] Agence sauvegardee  : id=" + agence.getIdAgence() + " | " + agence.getNom());

        // READ
        Optional<Agence> found = agenceRepository.findById(agence.getIdAgence());
        found.ifPresent(a -> System.out.println("[READ]   Agence trouvee    : " + a.getNom() + " - " + a.getVille()));

        // UPDATE
        agence.setTelephone("71000099");
        agenceRepository.save(agence);
        System.out.println("[UPDATE] Telephone mis a jour : " + agence.getTelephone());

        // COUNT
        System.out.println("[COUNT]  Nombre d'agences  : " + agenceRepository.count());

        // LIST
        List<Agence> agences = agenceRepository.findAll();
        System.out.println("[LIST]   Toutes les agences (" + agences.size() + ") :");
        agences.forEach(a -> System.out.println("         -> " + a.getNom() + " (" + a.getVille() + ")"));

        System.out.println();
    }

    // ─── VEHICULE ─────────────────────────────────────────────────────────────
    private void demoVehicule() {
        System.out.println("─── VEHICULE ────────────────────────────");

        Agence agence = agenceRepository.findAll().get(0);

        // CREATE
        Vehicule v = new Vehicule();
        v.setImmatriculation("TN-123-AB");
        v.setMarque("Renault");
        v.setModele("Clio");
        v.setCategorie(CategorieVehicule.CITADINE);
        v.setTarifJournalier(BigDecimal.valueOf(80));
        v.setStatut(StatutVehicule.DISPONIBLE);
        v.setAgence(agence);
        v = vehiculeRepository.save(v);
        System.out.println("[CREATE] Vehicule sauvegarde : id=" + v.getIdVehicule() + " | " + v.getMarque() + " " + v.getModele());

        // READ
        vehiculeRepository.findById(v.getIdVehicule())
                .ifPresent(veh -> System.out.println("[READ]   Vehicule trouve   : " + veh.getImmatriculation() + " - " + veh.getStatut()));

        // UPDATE
        v.setStatut(StatutVehicule.LOUE);
        vehiculeRepository.save(v);
        System.out.println("[UPDATE] Statut mis a jour   : " + v.getStatut());

        // COUNT
        System.out.println("[COUNT]  Nombre de vehicules : " + vehiculeRepository.count());

        // LIST
        List<Vehicule> vehicules = vehiculeRepository.findAll();
        System.out.println("[LIST]   Tous les vehicules (" + vehicules.size() + ") :");
        vehicules.forEach(veh -> System.out.println("         -> " + veh.getImmatriculation() + " | " + veh.getMarque() + " | " + veh.getStatut()));

        System.out.println();
    }

    // ─── CLIENT ───────────────────────────────────────────────────────────────
    private void demoClient() {
        System.out.println("─── CLIENT ──────────────────────────────");

        // CREATE
        Client client = new Client();
        client.setNom("Ben Ali");
        client.setPrenom("Mohamed");
        client.setEmail("mohamed.benali@email.com");
        client.setTelephone("22334455");
        client.setNumPermis("P-987654");
        client.setDateInscription(LocalDate.now());
        client = clientRepository.save(client);
        System.out.println("[CREATE] Client sauvegarde   : id=" + client.getIdClient() + " | " + client.getPrenom() + " " + client.getNom());

        // READ
        clientRepository.findById(client.getIdClient())
                .ifPresent(c -> System.out.println("[READ]   Client trouve     : " + c.getEmail()));

        // UPDATE
        client.setEmail("m.benali@gmail.com");
        clientRepository.save(client);
        System.out.println("[UPDATE] Email mis a jour    : " + client.getEmail());

        // COUNT
        System.out.println("[COUNT]  Nombre de clients  : " + clientRepository.count());

        // LIST
        List<Client> clients = clientRepository.findAll();
        System.out.println("[LIST]   Tous les clients (" + clients.size() + ") :");
        clients.forEach(c -> System.out.println("         -> " + c.getPrenom() + " " + c.getNom() + " | " + c.getEmail()));

        System.out.println();
    }

    // ─── CONTRAT + PAIEMENT (cascade) ─────────────────────────────────────────
    private void demoContrat() {
        System.out.println("─── CONTRAT + PAIEMENT (cascade) ────────");

        // CREATE contrat avec paiements en cascade
        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(BigDecimal.valueOf(240));
        contrat.setValide(true);

        Paiement p1 = new Paiement();
        p1.setMontant(BigDecimal.valueOf(120));
        p1.setDatePaiement(LocalDate.now());
        p1.setModePaiement(ModePaiement.CARTE);
        p1.setContrat(contrat);

        Paiement p2 = new Paiement();
        p2.setMontant(BigDecimal.valueOf(120));
        p2.setDatePaiement(LocalDate.now().plusDays(3));
        p2.setModePaiement(ModePaiement.ESPECES);
        p2.setContrat(contrat);

        contrat.setPaiements(List.of(p1, p2));
        contrat = contratRepository.save(contrat);
        System.out.println("[CREATE] Contrat sauvegarde  : id=" + contrat.getIdContrat() + " | montant=" + contrat.getMontantTotal());
        System.out.println("[CASCADE] Paiements crees    : " + paiementRepository.count());

        // READ
        contratRepository.findById(contrat.getIdContrat())
                .ifPresent(c -> System.out.println("[READ]   Contrat trouve    : valide=" + c.isValide() + " | date=" + c.getDateSignature()));

        // UPDATE
        contrat.setValide(false);
        contratRepository.save(contrat);
        System.out.println("[UPDATE] Contrat invalide    : valide=" + contrat.isValide());

        // COUNT
        System.out.println("[COUNT]  Nombre de contrats  : " + contratRepository.count());
        System.out.println("[COUNT]  Nombre de paiements : " + paiementRepository.count());

        // LIST
        List<Contrat> contrats = contratRepository.findAll();
        System.out.println("[LIST]   Tous les contrats (" + contrats.size() + ") :");
        contrats.forEach(c -> System.out.println("         -> id=" + c.getIdContrat() + " | montant=" + c.getMontantTotal() + " | valide=" + c.isValide()));

        // DELETE avec cascade sur paiements
        Long idASupprimer = contrat.getIdContrat();
        contratRepository.deleteById(idASupprimer);
        System.out.println("[DELETE] Contrat supprime    : id=" + idASupprimer);
        System.out.println("[CASCADE DELETE] Paiements restants : " + paiementRepository.count());

        System.out.println("\n========================================");
        System.out.println("          FIN DE LA DEMO CRUD");
        System.out.println("========================================\n");
    }
}
