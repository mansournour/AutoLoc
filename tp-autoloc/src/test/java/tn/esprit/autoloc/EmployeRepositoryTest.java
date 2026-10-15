package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.enums.RoleEmploye;
import tn.esprit.autoloc.repository.IEmployeRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmployeRepositoryTest {

    @Autowired
    IEmployeRepository employeRepo;

    @Test
    void testEmployeRepository() {
        System.out.println("\n========== TEST EMPLOYE REPOSITORY ==========");

        // 1. Créer un employé
        Employe emp = new Employe();
        emp.setNom("Mansour");
        emp.setPrenom("Nour");
        emp.setRole(RoleEmploye.AGENT);

        // 2. save()
        Employe saved = employeRepo.save(emp);
        System.out.println("Employé sauvegardé : " + saved);
        assertNotNull(saved.getIdEmploye());

        // 3. findAll()
        List<Employe> allEmps = employeRepo.findAll();
        System.out.println("Tous les employés : " + allEmps);
        assertFalse(allEmps.isEmpty());

        // 4. findById()
        Optional<Employe> found = employeRepo.findById(saved.getIdEmploye());
        System.out.println("findById(" + saved.getIdEmploye() + ") : " + found);
        assertTrue(found.isPresent());
        assertEquals("Mansour", found.get().getNom());

        // 5. existsById() — id existant
        boolean exists = employeRepo.existsById(saved.getIdEmploye());
        System.out.println("existsById id existant : " + exists);
        assertTrue(exists);

        // existsById() — id inexistant
        boolean existsNon = employeRepo.existsById(99999L);
        System.out.println("existsById id inexistant (99999) : " + existsNon);
        assertFalse(existsNon);

        // 6. count()
        long count = employeRepo.count();
        System.out.println("Nombre total d'employés : " + count);
        assertTrue(count > 0);

        // 7. deleteById()
        employeRepo.deleteById(saved.getIdEmploye());
        assertFalse(employeRepo.existsById(saved.getIdEmploye()));
        System.out.println("Employé supprimé avec succès.");

        System.out.println("========== FIN TEST EMPLOYE ==========\n");
    }
}
