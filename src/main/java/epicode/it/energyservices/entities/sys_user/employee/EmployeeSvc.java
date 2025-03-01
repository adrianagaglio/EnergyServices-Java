package epicode.it.energyservices.entities.sys_user.employee;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeSvc {
    private final EmployeeRepo employeeRepo;

    public List<Employee> getAll() {
        return employeeRepo.findAll();
    }

    public Page<Employee> getAllPageable(Pageable pageable) {
        return employeeRepo.findAll(pageable);
    }

    public Employee getById(Long id) {
        return employeeRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee not found"));
    }

    public Employee save(Employee e) {
        return employeeRepo.save(e);
    }

    public int count() {
        return (int) employeeRepo.count();
    }

    public String delete(Long id) {
        Employee e = getById(id);
        employeeRepo.delete(e);
        return "Employee deleted successfully";
    }

    public String delete(Employee e) {
        Employee foundEmployee = getById(e.getId());
        employeeRepo.delete(foundEmployee);
        return "Employee deleted successfully";
    }
}
