package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.domain.enums.StatutVehicule;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationRepositoryTest {

    @Autowired
    IReservationRepository reservationRepo;

    @Autowired
    IClientRepository clientRepo;

    @Autowired
    IVehiculeRepository vehiculeRepo;

    @Test
    void testReservationRepository() {
        System.out.println("\n========== TEST RESERVATION REPOSITORY ==========");

        // 0a. Créer un client parent (ManyToOne)
        Client client = new Client();
        client.setNom("Mansour");
        client.setPrenom("Nour");
        client.setEmail("nour.mansour@email.com");
        client.setTelephone("25000001");
        client.setNumPermis("P2001");
        client.setDateInscription(LocalDate.now());
        Client savedClient = clientRepo.save(client);
        System.out.println("Client parent créé : id=" + savedClient.getIdClient());
        assertNotNull(savedClient.getIdClient());

        // 0b. Créer un véhicule parent (ManyToOne) — sans agence (nullable)
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation("TN-999-RES");
        vehicule.setMarque("Renault");
        vehicule.setModele("Clio");
        vehicule.setCategorie(CategorieVehicule.CITADINE);
        vehicule.setTarifJournalier(new BigDecimal("60.00"));
        vehicule.setStatut(StatutVehicule.DISPONIBLE);
        Vehicule savedVehicule = vehiculeRepo.save(vehicule);
        System.out.println("Véhicule parent créé : id=" + savedVehicule.getIdVehicule());
        assertNotNull(savedVehicule.getIdVehicule());

        // 1. Créer une réservation (identifiant null → persist INSERT)
        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.now());
        reservation.setDateFin(LocalDate.now().plusDays(5));
        reservation.setStatut(StatutReservation.EN_ATTENTE);
        reservation.setClient(savedClient);
        reservation.setVehicule(savedVehicule);

        // 2. save() — INSERT
        Reservation saved = reservationRepo.save(reservation);
        System.out.println("Réservation sauvegardée : id=" + saved.getIdReservation()
                + ", statut=" + saved.getStatut()
                + ", du " + saved.getDateDebut() + " au " + saved.getDateFin());
        assertNotNull(saved);
        assertNotNull(saved.getIdReservation());

        // 3. findById() — Optional non vide
        Optional<Reservation> found = reservationRepo.findById(saved.getIdReservation());
        System.out.println("findById présent : " + found.isPresent());
        assertTrue(found.isPresent());
        assertEquals(StatutReservation.EN_ATTENTE, found.get().getStatut());
        assertEquals(savedClient.getIdClient(), found.get().getClient().getIdClient());

        // 4. findAll()
        List<Reservation> all = reservationRepo.findAll();
        System.out.println("Nombre de réservations en base : " + all.size());
        assertFalse(all.isEmpty());

        // 5. count()
        long count = reservationRepo.count();
        System.out.println("count() = " + count);
        assertTrue(count > 0);

        // 6. save() — UPDATE (identifiant renseigné → merge SELECT + UPDATE)
        saved.setStatut(StatutReservation.CONFIRMEE);
        saved.setDateFin(LocalDate.now().plusDays(7));
        Reservation updated = reservationRepo.save(saved);
        System.out.println("Réservation après update : statut=" + updated.getStatut()
                + ", dateFin=" + updated.getDateFin());
        assertEquals(StatutReservation.CONFIRMEE, updated.getStatut());
        assertEquals(LocalDate.now().plusDays(7), updated.getDateFin());

        // 7. existsById() — id existant
        assertTrue(reservationRepo.existsById(saved.getIdReservation()));

        // 8. existsById() — id inexistant
        assertFalse(reservationRepo.existsById(99999L));
        System.out.println("existsById(99999) : false — confirmé");

        // 9. saveAndFlush() — écriture immédiate
        saved.setStatut(StatutReservation.TERMINEE);
        Reservation flushed = reservationRepo.saveAndFlush(saved);
        System.out.println("saveAndFlush statut=" + flushed.getStatut());
        assertEquals(StatutReservation.TERMINEE, flushed.getStatut());

        // 10. deleteById() — supprime la réservation (pas le client ni le véhicule)
        Long idASupprimer = saved.getIdReservation();
        reservationRepo.deleteById(idASupprimer);
        boolean existsApresDelete = reservationRepo.existsById(idASupprimer);
        System.out.println("existsById après suppression : " + existsApresDelete);
        assertFalse(existsApresDelete);

        // 11. findById sur id inexistant → Optional vide
        Optional<Reservation> absent = reservationRepo.findById(idASupprimer);
        System.out.println("Optional vide après suppression : " + absent.isEmpty());
        assertTrue(absent.isEmpty());

        // Nettoyage : supprimer les entités parentes
        vehiculeRepo.deleteById(savedVehicule.getIdVehicule());
        clientRepo.deleteById(savedClient.getIdClient());
        System.out.println("Nettoyage effectué.");

        System.out.println("========== FIN TEST RESERVATION ==========\n");
    }
}
