package qrm.repository;

import qrm.repository.annotation.Query;
import qrm.repository.options.QueryOptions;

import java.util.List;
import java.util.Map;

@Query(dataType = Employee.class)
public interface EmployeeRepository extends BaseRepository<Employee> {

    @Override
    @Query(text="qtc:my-first-query")
    List<Employee> search(Map<String, Object> parameters, QueryOptions queryOptions);

}
