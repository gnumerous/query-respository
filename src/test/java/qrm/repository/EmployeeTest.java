package qrm.repository;

import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qrm.repository.options.QueryOptions;

import java.util.Collection;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class EmployeeTest {

    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setup() {
        var proxyBuilder = new ProxyBuilder<EmployeeRepository, Employee>();
        this.employeeRepository = proxyBuilder.createProxy(EmployeeRepository.class, Employee.class);

    }
    @Test
    void myFirstEmployee() {
        Collection<Employee> results = employeeRepository.search(Map.of("last_name", "Hammerstein"), QueryOptions.EMPTY);
        results.forEach(System.out::println);
        assertThat(results).hasSize(3);
    }

    @Test
    void singleFetch() {
        Employee numberNine = employeeRepository.fetchById(100);
        assertThat(numberNine.getId()).isEqualTo(100);
    }
}
