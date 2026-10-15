package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.repository.IClientRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientRepositoryTest {

    @Autowired
    IClientRepository clientRepo;

    @Test
    void testClientRepository() {
        System.out.println("\n========== TEST CLIENT REPOSITORY ==========");

        // 1. Créer deux clients
        Client client1 = new Client();
        client1.setNom("Ben Ali");
        client1.setPrenom("Sami");
        client1.setEmail("sami.benali@email.com");
        client1.setTelephone("22000001");
        client1.setNumPermis("P1001");
        client1.setDateInscription(LocalDate.now());

        Client client2 = new Client();
        client2.setNom("Trabelsi");
        client2.setPrenom("Lina");
        client2.setEmail("lina.trabelsi@email.com");
        client2.setTelephone("22000002");
        client2.setNumPermis("P1002");
        client2.setDateInscription(LocalDate.now());

        // 2. save()
        Client saved1 = clientRepo.save(client1);
        Client saved2 = clientRepo.save(client2);
        System.out.println("Client 1 sauvegardé : " + saved1);
        System.out.println("Client 2 sauvegardé : " + saved2);
        assertNotNull(saved1.getIdClient());
        assertNotNull(saved2.getIdClient());

        // 3. findAll()
        List<Client> allClients = clientRepo.findAll();
        System.out.println("Tous les clients : " + allClients);
        assertTrue(allClients.size() >= 2);

        // 4. findById()
        Optional<Client> found = clientRepo.findById(saved1.getIdClient());
        System.out.println("findById(" + saved1.getIdClient() + ") : " + found);
        assertTrue(found.isPresent());
        assertEquals("Ben Ali", found.get().getNom());

        // 5. existsById() — id existant
        boolean existsOui = clientRepo.existsById(saved1.getIdClient());
        System.out.println("existsById id existant : " + existsOui);
        assertTrue(existsOui);

        // existsById() — id inexistant
        boolean existsNon = clientRepo.existsById(99999L);
        System.out.println("existsById id inexistant (99999) : " + existsNon);
        assertFalse(existsNon);

        // 6. count()
        long count = clientRepo.count();
        System.out.println("Nombre total de clients : " + count);
        assertTrue(count >= 2);

        // Nettoyage
        clientRepo.deleteById(saved1.getIdClient());
        clientRepo.deleteById(saved2.getIdClient());

        System.out.println("========== FIN TEST CLIENT ==========\n");
    }
}
